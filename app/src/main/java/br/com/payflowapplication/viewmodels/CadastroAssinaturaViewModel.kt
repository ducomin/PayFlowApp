package br.com.payflowapplication.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade
import br.com.payflowapplication.model.Streaming
import java.time.LocalDate
import br.com.payflowapplication.data.repository.AssinaturaRepository
import br.com.payflowapplication.data.repository.StreamingRepository
import br.com.payflowapplication.view.components.CurrencyVisualTransformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CadastroAssinaturaUiState(
    val nomeServico: String = "",
    val valor: String = "",
    val modalidade: Modalidade = Modalidade.MENSAL,
    val diaVencimento: String = "",
    val categoria: CategoriaAssinatura? = CategoriaAssinatura.OUTROS,
    val urlServico: String = "",
    val dataInicio: LocalDate? = null,
    val nomeError: String? = null,
    val valorError: String? = null,
    val vencimentoError: String? = null,
    val categoriaError: String? = null,
    val urlError: String? = null,
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val isEditMode: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
    // Streaming autocomplete state
    val streamingSuggestions: List<Streaming> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val showSuggestions: Boolean = false
)

@HiltViewModel
class CadastroAssinaturaViewModel @Inject constructor(
    private val repository: AssinaturaRepository,
    private val streamingRepository: StreamingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val assinaturaId: Long = savedStateHandle.get<Long>("assinaturaId") ?: 0L

    private val _uiState = MutableStateFlow(CadastroAssinaturaUiState())
    val uiState: StateFlow<CadastroAssinaturaUiState> = _uiState.asStateFlow()

    /** Debounce job for async duplicate-name check while the user types */
    private var checkNomeJob: Job? = null

    /** Debounce job for streaming suggestion search */
    private var searchStreamingJob: Job? = null

    init {
        if (assinaturaId != 0L) loadAssinatura(assinaturaId)
    }

    private fun loadAssinatura(id: Long) {
        viewModelScope.launch {
            repository.getById(id)?.let { assinatura ->
                val centavos = (assinatura.valor * 100).toLong()
                _uiState.update {
                    it.copy(
                        nomeServico = assinatura.nomeServico,
                        valor = if (centavos > 0) centavos.toString() else "",
                        modalidade = assinatura.modalidade,
                        diaVencimento = assinatura.diaVencimento.toString(),
                        categoria = assinatura.categoria,
                        urlServico = assinatura.urlServico ?: "",
                        dataInicio = assinatura.dataInicio,
                        isEditMode = true,
                        hasUnsavedChanges = false
                    )
                }
            }
        }
    }

    // ── Field handlers ────────────────────────────────────────────────────────

    fun onNomeChange(value: String) {
        // Update field immediately and clear previous error
        _uiState.update { it.copy(nomeServico = value, nomeError = null, hasUnsavedChanges = true) }

        // Debounce: wait 300 ms after the user stops typing, then check duplicates
        checkNomeJob?.cancel()
        checkNomeJob = viewModelScope.launch {
            delay(300L)
            val trimmed = value.trim()
            if (trimmed.isBlank()) return@launch
            val duplicate = repository.existsByNome(trimmed, excludeId = assinaturaId)
            if (duplicate) {
                _uiState.update {
                    it.copy(nomeError = "Já existe uma assinatura com o nome \"$trimmed\"")
                }
            }
        }

        // Debounce: search streamings for autocomplete suggestions
        searchStreamingJob?.cancel()
        if (value.length < 2) {
            _uiState.update { it.copy(streamingSuggestions = emptyList(), showSuggestions = false) }
            return
        }
        searchStreamingJob = viewModelScope.launch {
            delay(300L)
            _uiState.update { it.copy(isLoadingSuggestions = true) }
            val results = streamingRepository.search(value)
            _uiState.update {
                it.copy(
                    streamingSuggestions = results,
                    isLoadingSuggestions = false,
                    showSuggestions = results.isNotEmpty(),
                    nomeError = if (results.isEmpty()) "Nome de serviço não encontrado" else it.nomeError
                )
            }
        }
    }

    fun onStreamingSelected(streaming: Streaming) {
        _uiState.update {
            it.copy(
                nomeServico = streaming.nome,
                urlServico = it.urlServico, // preserve existing URL if already set
                nomeError = null,
                showSuggestions = false,
                streamingSuggestions = emptyList(),
                hasUnsavedChanges = true
            )
        }        // Also cancel pending jobs since a suggestion was picked
        checkNomeJob?.cancel()
        searchStreamingJob?.cancel()
    }

    fun onDismissSuggestions() {
        _uiState.update { it.copy(showSuggestions = false) }
    }

    fun onValorChange(digits: String) {
        val cleaned = digits.filter { it.isDigit() }.trimStart('0').take(13)
        _uiState.update { it.copy(valor = cleaned, valorError = null, hasUnsavedChanges = true) }
    }

    fun onModalidadeChange(value: Modalidade) =
        _uiState.update { it.copy(modalidade = value, hasUnsavedChanges = true) }

    fun onVencimentoChange(value: String) =
        _uiState.update { it.copy(diaVencimento = value, vencimentoError = null, hasUnsavedChanges = true) }

    fun onCategoriaChange(value: CategoriaAssinatura) =
        _uiState.update { it.copy(categoria = value, categoriaError = null, hasUnsavedChanges = true) }

    fun onUrlChange(value: String) =
        _uiState.update { it.copy(urlServico = value, urlError = null, hasUnsavedChanges = true) }

    // ── Save ──────────────────────────────────────────────────────────────────
    fun salvar() {
        viewModelScope.launch {
            // Step 1 — sync format validation
            if (!validate()) return@launch

            val state = _uiState.value

            // Step 2 — async duplicate check (catches race condition if debounce hasn't fired yet)
            val duplicate = repository.existsByNome(state.nomeServico.trim(), excludeId = assinaturaId)
            if (duplicate) {
                _uiState.update {
                    it.copy(nomeError = "Já existe uma assinatura com o nome \"${state.nomeServico.trim()}\"")
                }
                return@launch
            }

            // Step 3 — persist
            _uiState.update { it.copy(isSaving = true) }
            val assinatura = Assinatura(
                id = assinaturaId,
                nomeServico = state.nomeServico.trim(),
                valor = CurrencyVisualTransformation.digitsToDouble(state.valor),
                modalidade = state.modalidade,
                diaVencimento = state.diaVencimento.toInt(),
                categoria = state.categoria!!,
                urlServico = state.urlServico.trim(),
                dataInicio = state.dataInicio ?: LocalDate.now()
            )
            repository.save(assinatura)
            _uiState.update { it.copy(isSaving = false, savedSuccessfully = true, hasUnsavedChanges = false) }
        }
    }

    fun resetSavedFlag() = _uiState.update {
        if (!it.isEditMode) CadastroAssinaturaUiState() else it.copy(savedSuccessfully = false)
    }

    // ── Format-only validation (sync) ────────────────────────────────────────

    private fun validate(): Boolean {
        val state = _uiState.value

        // If debounce already set a duplicate error, respect it
        val nomeError = when {
            state.nomeServico.isBlank() -> "Nome obrigatório"
            state.nomeError != null     -> state.nomeError   // keep debounce error
            else                        -> null
        }
        val valorError = when {
            state.valor.isBlank() -> "Valor obrigatório"
            CurrencyVisualTransformation.digitsToDouble(state.valor) <= 0.0 -> "Valor deve ser maior que zero"
            else -> null
        }
        val vencimentoError = when {
            state.diaVencimento.isBlank()              -> "Vencimento obrigatório"
            state.diaVencimento.toIntOrNull() == null  -> "Dia inválido"
            state.diaVencimento.toInt() !in 1..31      -> "Dia deve ser entre 1 e 31"
            else -> null
        }
        val categoriaError = if (state.categoria == null) "Categoria obrigatória" else null
        val urlError = if (state.urlServico.isNotBlank() &&
            !state.urlServico.startsWith("http://") &&
            !state.urlServico.startsWith("https://")
        ) "URL deve começar com http:// ou https://" else null

        val hasError = listOf(nomeError, valorError, vencimentoError, categoriaError, urlError)
            .any { it != null }

        _uiState.update {
            it.copy(
                nomeError = nomeError,
                valorError = valorError,
                vencimentoError = vencimentoError,
                categoriaError = categoriaError,
                urlError = urlError
            )
        }
        return !hasError
    }
}
