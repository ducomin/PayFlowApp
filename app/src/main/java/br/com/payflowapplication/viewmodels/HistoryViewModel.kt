package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.repository.HistoryRepository
import br.com.payflowapplication.model.Assinatura
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val historico: List<Assinatura> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistorico()
    }

    private fun loadHistorico() {
        viewModelScope.launch {
            repository.getHistorico()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = "Falha ao carregar histórico: ${exception.message}",
                        isLoading = false
                    )
                }
                .collect { historicoList ->
                    _uiState.value = _uiState.value.copy(
                        historico = historicoList,
                        isLoading = false
                    )
                }
        }
    }
}

