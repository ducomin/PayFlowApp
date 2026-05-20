package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.repository.AssinaturaRepository
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

// ─── Threshold for "low usage" (MVP: simulated via random seed per id) ────────
private const val BAIXO_USO_THRESHOLD = 0.30f

/** Usage score for an Assinatura (0..1). In MVP we simulate it deterministically. */
fun simulatedUsage(assinatura: Assinatura): Float {
    // deterministic but varied: hash the id into a 0..1 range
    val seed = ((assinatura.id * 1_234_567L + 7) % 100).toInt()
    return seed / 100f
}

// ─── UI State ─────────────────────────────────────────────────────────────────

sealed interface HomeDashboardUiState {
    data object Loading : HomeDashboardUiState
    data object Empty : HomeDashboardUiState
    data class Error(val message: String) : HomeDashboardUiState
    data class Success(
        val nomeUsuario: String,
        val mesReferencia: String,
        val totalMensal: Double,
        val totalAtivas: Int,
        val totalPoucoUsadas: Int,
        val totalVenceHoje: Int,
        val valorPoucoUsadas: Double,
        val assinaturasFiltradas: List<AssinaturaUiItem>,
        val queryBusca: String,
        val categoriaFiltro: CategoriaAssinatura?,  // null = "Todos"
    ) : HomeDashboardUiState
}

data class AssinaturaUiItem(
    val assinatura: Assinatura,
    val usage: Float, // 0..1
    val poucoUsada: Boolean,
    val venceHoje: Boolean,
    val vencimentoLabel: String,
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
class HomeDashboardViewModel @Inject constructor(
    private val repository: AssinaturaRepository
) : ViewModel() {

    private val _queryBusca = MutableStateFlow("")
    private val _categoriaFiltro = MutableStateFlow<CategoriaAssinatura?>(null)

    private val _uiState = MutableStateFlow<HomeDashboardUiState>(HomeDashboardUiState.Loading)
    val uiState: StateFlow<HomeDashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getAtivas(),
                _queryBusca,
                _categoriaFiltro
            ) { lista, query, categoria ->
                buildUiState(lista, query, categoria)
            }
                .catch { e -> _uiState.value = HomeDashboardUiState.Error(e.message ?: "Erro") }
                .collect { state -> _uiState.value = state }
        }
    }

    fun onQueryBuscaChange(query: String) {
        _queryBusca.update { query }
    }

    fun onCategoriaFiltroChange(categoria: CategoriaAssinatura?) {
        _categoriaFiltro.update { categoria }
    }

    fun onDeleteAssinatura(assinatura: Assinatura) {
        viewModelScope.launch {
            repository.delete(assinatura)
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun buildUiState(
        lista: List<Assinatura>,
        query: String,
        categoria: CategoriaAssinatura?
    ): HomeDashboardUiState {
        val cal = Calendar.getInstance()
        val todayDay = cal.get(Calendar.DAY_OF_MONTH)
        val monthNames = listOf(
            "janeiro","fevereiro","março","abril","maio","junho",
            "julho","agosto","setembro","outubro","novembro","dezembro"
        )
        val mesLabel = "${monthNames[cal.get(Calendar.MONTH)]} ${cal.get(Calendar.YEAR)}"

        // Build enriched items
        val todosItens = lista.map { ass ->
            val usage = simulatedUsage(ass)
            val poucoUsada = usage < BAIXO_USO_THRESHOLD
            val venceHoje = ass.diaVencimento == todayDay
            val vencLabel = buildVencimentoLabel(ass, todayDay)
            AssinaturaUiItem(
                assinatura = ass,
                usage = usage,
                poucoUsada = poucoUsada,
                venceHoje = venceHoje,
                vencimentoLabel = vencLabel
            )
        }

        if (todosItens.isEmpty()) return HomeDashboardUiState.Empty

        // Summary metrics
        val totalMensal = todosItens.sumOf { item ->
            if (item.assinatura.modalidade == Modalidade.ANUAL)
                item.assinatura.valor / 12.0
            else
                item.assinatura.valor
        }
        val poucoUsadasList = todosItens.filter { it.poucoUsada }
        val valorPoucoUsadas = poucoUsadasList.sumOf { item ->
            if (item.assinatura.modalidade == Modalidade.ANUAL)
                item.assinatura.valor / 12.0
            else
                item.assinatura.valor
        }
        val venceHojeCount = todosItens.count { it.venceHoje }

        // Filter
        val filtrados = todosItens.filter { item ->
            val matchQuery = query.isBlank() ||
                item.assinatura.nomeServico.contains(query, ignoreCase = true)
            val matchCategoria = categoria == null ||
                item.assinatura.categoria == categoria
            matchQuery && matchCategoria
        }

        return HomeDashboardUiState.Success(
            nomeUsuario = "Usuário",
            mesReferencia = mesLabel,
            totalMensal = totalMensal,
            totalAtivas = todosItens.size,
            totalPoucoUsadas = poucoUsadasList.size,
            totalVenceHoje = venceHojeCount,
            valorPoucoUsadas = valorPoucoUsadas,
            assinaturasFiltradas = filtrados,
            queryBusca = query,
            categoriaFiltro = categoria,
        )
    }

    private fun buildVencimentoLabel(ass: Assinatura, todayDay: Int): String {
        return if (ass.modalidade == Modalidade.ANUAL) {
            "Anual · vence dia ${ass.diaVencimento}"
        } else {
            val diff = ass.diaVencimento - todayDay
            when {
                diff == 0 -> "Vence hoje"
                diff == 1 -> "Vence amanhã"
                diff > 1  -> "Vence em $diff dias"
                else      -> "Venceu dia ${ass.diaVencimento}"
            }
        }
    }
}





