package br.com.payflowapplication.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.repository.AssinaturaRepository
import br.com.payflowapplication.data.repository.StreamingRepository
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.Pagamento
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

sealed interface DetalheUiState {
    object Loading : DetalheUiState
    data class Success(
        val assinatura: Assinatura,
        val totalPago: Double,
        val historico: List<Pagamento>,
        val diasParaVencer: Int,
        val proximoVencimento: String,
        val desde: String,
        val mesesAtiva: Int
    ) : DetalheUiState
    data class Error(val message: String) : DetalheUiState
}

@HiltViewModel
class DetalheViewModel @Inject constructor(
    private val repository: AssinaturaRepository,
    private val streamingRepository: StreamingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalheUiState>(DetalheUiState.Loading)
    val uiState: StateFlow<DetalheUiState> = _uiState.asStateFlow()

    private val assinaturaId: Long = savedStateHandle.get<Long>("assinaturaId") ?: 0L

    init {
        observarAssinatura()
    }

    private fun observarAssinatura() {
        viewModelScope.launch {
            repository.getByIdFlow(assinaturaId)
                .catch { e -> _uiState.value = DetalheUiState.Error(e.message ?: "Erro ao carregar") }
                .collect { assinatura ->
                    if (assinatura != null) {
                        carregarDetalhesAdicionais(assinatura)
                    } else {
                        _uiState.value = DetalheUiState.Error("Assinatura não encontrada")
                    }
                }
        }
    }

    private suspend fun carregarDetalhesAdicionais(assinatura: Assinatura) {
        try {
            val pagamentos = streamingRepository.getPagamentos(assinatura.nomeServico)
            val totalPago = pagamentos.sumOf { it.valor }

            val hoje = Calendar.getInstance()
            val vencimento = Calendar.getInstance()
            
            var dia = assinatura.diaVencimento
            val maxDia = vencimento.getActualMaximum(Calendar.DAY_OF_MONTH)
            if (dia > maxDia) dia = maxDia
            
            vencimento.set(Calendar.DAY_OF_MONTH, dia)
            
            if (vencimento.before(hoje)) {
                vencimento.add(Calendar.MONTH, 1)
            }
            
            val diffMillis = vencimento.timeInMillis - hoje.timeInMillis
            val diasParaVencer = (diffMillis / (1000 * 60 * 60 * 24)).toInt()

            val sdfDisplay = SimpleDateFormat("dd/MM/yy", Locale.US)
            val proximoVencimento = sdfDisplay.format(vencimento.time)

            val oldestPayment = pagamentos.minByOrNull { it.dataCobranca }
            val desde = if (oldestPayment != null) {
                try {
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(oldestPayment.dataCobranca)
                    if (date != null) SimpleDateFormat("MMM yyyy", Locale("pt", "BR")).format(date).replaceFirstChar { it.uppercase() }
                    else "Mar 2024"
                } catch (e: Exception) { "Mar 2024" }
            } else "Mar 2024"
            
            val mesesAtiva = if (pagamentos.isNotEmpty()) pagamentos.size else 14

            _uiState.value = DetalheUiState.Success(
                assinatura = assinatura,
                totalPago = totalPago,
                historico = pagamentos,
                diasParaVencer = diasParaVencer,
                proximoVencimento = proximoVencimento,
                desde = desde,
                mesesAtiva = mesesAtiva
            )
        } catch (e: Exception) {
            _uiState.value = DetalheUiState.Error(e.message ?: "Erro ao carregar dados remotos")
        }
    }

    fun cancelarAssinatura(onFinished: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            if (state is DetalheUiState.Success) {
                val assinaturaCancelada = state.assinatura.copy(
                    ativa = false,
                    dataFim = LocalDate.now()
                )
                repository.save(assinaturaCancelada)
                onFinished()
            }
        }
    }
}
