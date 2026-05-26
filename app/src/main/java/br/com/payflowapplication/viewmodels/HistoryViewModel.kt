package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.repository.HistoryRepository
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.CategoriaAssinatura
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

enum class HistorySortOption(val label: String) {
    DATE_DESC("Mais recentes"),
    DATE_ASC("Mais antigas"),
    VALUE_DESC("Maior valor"),
    VALUE_ASC("Menor valor")
}

data class HistoryUiState(
    val historico: List<Assinatura> = emptyList(),
    val selectedCategory: CategoriaAssinatura? = null,
    val sortOption: HistorySortOption = HistorySortOption.DATE_DESC,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<CategoriaAssinatura?>(null)
    private val _sortOption = MutableStateFlow(HistorySortOption.DATE_DESC)

    val uiState: StateFlow<HistoryUiState> = combine(
        repository.getHistorico(),
        _selectedCategory,
        _sortOption
    ) { historico, category, sort ->
        val filteredList = if (category != null) {
            historico.filter { it.categoria == category }
        } else {
            historico
        }

        val sortedList = when (sort) {
            HistorySortOption.DATE_DESC -> filteredList.sortedByDescending { it.dataFim }
            HistorySortOption.DATE_ASC -> filteredList.sortedBy { it.dataFim }
            HistorySortOption.VALUE_DESC -> filteredList.sortedByDescending { it.valor }
            HistorySortOption.VALUE_ASC -> filteredList.sortedBy { it.valor }
        }

        HistoryUiState(
            historico = sortedList,
            selectedCategory = category,
            sortOption = sort,
            isLoading = false
        )
    }.catch { exception ->
        emit(HistoryUiState(
            error = "Falha ao carregar histórico: ${exception.message}",
            isLoading = false
        ))
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState(isLoading = true)
    )

    fun onCategoryFilterChange(category: CategoriaAssinatura?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun onSortOptionChange(sortOption: HistorySortOption) {
        _sortOption.value = sortOption
    }
}
