package br.com.payflowapplication.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade
import br.com.payflowapplication.repository.AssinaturaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CadastroAssinaturaUiState(
    // form fields
    val nomeServico: String = "",
    val valor: String = "",
    val modalidade: Modalidade = Modalidade.MENSAL,
    val diaVencimento: String = "",
    val categoria: CategoriaAssinatura? = null,
    val urlServico: String = "",
    // field errors
    val nomeError: String? = null,
    val valorError: String? = null,
    val vencimentoError: String? = null,
    val categoriaError: String? = null,
    val urlError: String? = null,
    // screen state
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val isEditMode: Boolean = false,
    val hasUnsavedChanges: Boolean = false
)

@HiltViewModel
class CadastroAssinaturaViewModel @Inject constructor(
    private val repository: AssinaturaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val assinaturaId: Long = savedStateHandle.get<Long>("assinaturaId") ?: 0L

    private val _uiState = MutableStateFlow(CadastroAssinaturaUiState())
    val uiState: StateFlow<CadastroAssinaturaUiState> = _uiState.asStateFlow()

    init {
        if (assinaturaId != 0L) {
            loadAssinatura(assinaturaId)
        }
    }

    private fun loadAssinatura(id: Long) {
        viewModelScope.launch {
            repository.getById(id)?.let { assinatura ->
                _uiState.update {
                    it.copy(
                        nomeServico = assinatura.nomeServico,
                        valor = assinatura.valor.toString(),
                        modalidade = assinatura.modalidade,
                        diaVencimento = assinatura.diaVencimento.toString(),
                        categoria = assinatura.categoria,
                        urlServico = assinatura.urlServico,
                        isEditMode = true,
                        hasUnsavedChanges = false
                    )
                }
            }
        }
    }

    fun onNomeChange(value: String) =
        _uiState.update { it.copy(nomeServico = value, nomeError = null, hasUnsavedChanges = true) }

    fun onValorChange(value: String) =
        _uiState.update { it.copy(valor = value, valorError = null, hasUnsavedChanges = true) }

    fun onModalidadeChange(value: Modalidade) =
        _uiState.update { it.copy(modalidade = value, hasUnsavedChanges = true) }

    fun onVencimentoChange(value: String) =
        _uiState.update { it.copy(diaVencimento = value, vencimentoError = null, hasUnsavedChanges = true) }

    fun onCategoriaChange(value: CategoriaAssinatura) =
        _uiState.update { it.copy(categoria = value, categoriaError = null, hasUnsavedChanges = true) }

    fun onUrlChange(value: String) =
        _uiState.update { it.copy(urlServico = value, urlError = null, hasUnsavedChanges = true) }

    fun salvar() {
        if (!validate()) return

        val state = _uiState.value
        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            val assinatura = Assinatura(
                id = assinaturaId,
                nomeServico = state.nomeServico.trim(),
                valor = state.valor.toDouble(),
                modalidade = state.modalidade,
                diaVencimento = state.diaVencimento.toInt(),
                categoria = state.categoria!!,
                urlServico = state.urlServico.trim()
            )
            repository.save(assinatura)
            _uiState.update { it.copy(isSaving = false, savedSuccessfully = true, hasUnsavedChanges = false) }
        }
    }

    fun resetSavedFlag() = _uiState.update {
        // Se não é modo edição, limpa todos os campos após salvar
        if (!it.isEditMode) {
            CadastroAssinaturaUiState()
        } else {
            it.copy(savedSuccessfully = false)
        }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        var valid = true

        val nomeError = if (state.nomeServico.isBlank()) "Nome obrigatório" else null
        val valorError = when {
            state.valor.isBlank() -> "Valor obrigatório"
            state.valor.toDoubleOrNull() == null -> "Valor inválido"
            state.valor.toDouble() <= 0 -> "Valor deve ser maior que zero"
            else -> null
        }
        val vencimentoError = when {
            state.diaVencimento.isBlank() -> "Vencimento obrigatório"
            state.diaVencimento.toIntOrNull() == null -> "Dia inválido"
            state.diaVencimento.toInt() !in 1..31 -> "Dia deve ser entre 1 e 31"
            else -> null
        }
        val categoriaError = if (state.categoria == null) "Categoria obrigatória" else null
        val urlError = if (state.urlServico.isNotBlank() &&
            !state.urlServico.startsWith("http://") &&
            !state.urlServico.startsWith("https://")
        ) "URL deve começar com http:// ou https://" else null

        if (listOf(nomeError, valorError, vencimentoError, categoriaError, urlError).any { it != null }) {
            valid = false
        }

        _uiState.update {
            it.copy(
                nomeError = nomeError,
                valorError = valorError,
                vencimentoError = vencimentoError,
                categoriaError = categoriaError,
                urlError = urlError
            )
        }
        return valid
    }
}

