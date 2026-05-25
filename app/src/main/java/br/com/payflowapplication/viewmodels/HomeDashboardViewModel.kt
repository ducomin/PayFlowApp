package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.repository.AssinaturaRepository
import br.com.payflowapplication.data.repository.StreamingRepository
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade
import br.com.payflowapplication.view.components.AssinaturaUso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

// ─── Threshold for "low usage" ────────────────────────────────────────────────
private const val BAIXO_USO_THRESHOLD = 0.30f

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
        val assinaturasPoucoUsadas: List<AssinaturaUso>,
        val queryBusca: String,
        val categoriaFiltro: CategoriaAssinatura?,
    ) : HomeDashboardUiState
}

data class AssinaturaUiItem(
    val assinatura: Assinatura,
    val usage: Float,          // 0..1  (dias_utilizados / total_dias_no_mes)
    val poucoUsada: Boolean,
    val venceHoje: Boolean,
    val vencimentoLabel: String,
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
class HomeDashboardViewModel @Inject constructor(
    private val assinaturaRepository: AssinaturaRepository,
    private val streamingRepository: StreamingRepository,
) : ViewModel() {

    private val _queryBusca     = MutableStateFlow("")
    private val _categoriaFiltro = MutableStateFlow<CategoriaAssinatura?>(null)

    private val _uiState = MutableStateFlow<HomeDashboardUiState>(HomeDashboardUiState.Loading)
    val uiState: StateFlow<HomeDashboardUiState> = _uiState.asStateFlow()

    /**
     * In-session cache: key = "nomeServico:anomes" (lowercase), value = usage 0..1.
     * Avoids hitting the API again for the same service within the same session.
     */
    private val usageCache = mutableMapOf<String, Float>()

    init {
        viewModelScope.launch {
            combine(
                assinaturaRepository.getAtivas(),
                _queryBusca,
                _categoriaFiltro,
            ) { lista, query, categoria ->
                Triple(lista, query, categoria)
            }
                .catch { e -> _uiState.value = HomeDashboardUiState.Error(e.message ?: "Erro") }
                .collect { (lista, query, categoria) ->
                    // Emit loading skeleton on first load only
                    if (_uiState.value is HomeDashboardUiState.Loading) {
                        _uiState.value = HomeDashboardUiState.Loading
                    }
                    val state = buildUiState(lista, query, categoria)
                    _uiState.value = state
                }
        }
    }

    fun onQueryBuscaChange(query: String)             = _queryBusca.update { query }
    fun onCategoriaFiltroChange(c: CategoriaAssinatura?) = _categoriaFiltro.update { c }
    fun onDeleteAssinatura(ass: Assinatura) {
        viewModelScope.launch { assinaturaRepository.delete(ass) }
    }

    // ─── Build state ──────────────────────────────────────────────────────────

    private suspend fun buildUiState(
        lista: List<Assinatura>,
        query: String,
        categoria: CategoriaAssinatura?,
    ): HomeDashboardUiState {
        if (lista.isEmpty()) return HomeDashboardUiState.Empty

        val cal      = Calendar.getInstance()
        val todayDay = cal.get(Calendar.DAY_OF_MONTH)
        val anomes   = currentAnomes(cal)
        val mesLabel = currentMesLabel(cal)

        // ── Fetch usage for all subscriptions in parallel ─────────────────────
        val usages: Map<Long, Float> = fetchUsagesParallel(lista, anomes)

        // ── Build enriched items ──────────────────────────────────────────────
        val todosItens = lista.map { ass ->
            val usage     = usages[ass.id] ?: 0f
            val poucoUsada = usage < BAIXO_USO_THRESHOLD
            val venceHoje  = ass.diaVencimento == todayDay
            AssinaturaUiItem(
                assinatura     = ass,
                usage          = usage,
                poucoUsada     = poucoUsada,
                venceHoje      = venceHoje,
                vencimentoLabel = buildVencimentoLabel(ass, todayDay),
            )
        }

        // ── Metrics ───────────────────────────────────────────────────────────
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
        // Build AssinaturaUso list for the banner chart
        // usage (0..1) * 30 → approximate days used this month
        val assinaturasPoucoUsadas = poucoUsadasList.map { item ->
            AssinaturaUso(
                nome     = item.assinatura.nomeServico,
                diasUso  = (item.usage * 30).toInt(),
                valorMes = if (item.assinatura.modalidade == Modalidade.ANUAL)
                               item.assinatura.valor / 12.0
                           else
                               item.assinatura.valor,
            )
        }

        // ── Filter ──────────────────��─────────────────────────────────────────
        val filtrados = todosItens.filter { item ->
            val matchQuery    = query.isBlank() ||
                item.assinatura.nomeServico.contains(query, ignoreCase = true)
            val matchCategoria = categoria == null ||
                item.assinatura.categoria == categoria
            matchQuery && matchCategoria
        }

        return HomeDashboardUiState.Success(
            nomeUsuario              = "Usuário",
            mesReferencia            = mesLabel,
            totalMensal              = totalMensal,
            totalAtivas              = todosItens.size,
            totalPoucoUsadas         = poucoUsadasList.size,
            totalVenceHoje           = todosItens.count { it.venceHoje },
            valorPoucoUsadas         = valorPoucoUsadas,
            assinaturasFiltradas     = filtrados,
            assinaturasPoucoUsadas   = assinaturasPoucoUsadas,
            queryBusca               = query,
            categoriaFiltro          = categoria,
        )
    }

    /**
     * Fetches usage scores from the API for every [Assinatura] in parallel.
     * Results are stored in [usageCache] keyed by "nome:anomes" to prevent
     * redundant calls when the list or filters change within the same session.
     *
     * Returns a map of assinatura.id → usage (0..1).
     */
    private suspend fun fetchUsagesParallel(
        lista: List<Assinatura>,
        anomes: String,
    ): Map<Long, Float> = coroutineScope {
        lista.map { ass ->
            async {
                val cacheKey = "${ass.nomeServico.lowercase().trim()}:$anomes"
                val cached   = usageCache[cacheKey]
                if (cached != null) {
                    ass.id to cached
                } else {
                    val consumo = streamingRepository.getConsumoMensal(
                        nomeServico = ass.nomeServico,
                        anomes      = anomes,
                    )
                    val score = consumo.usageScore
                    usageCache[cacheKey] = score
                    ass.id to score
                }
            }
        }.awaitAll().toMap()
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /** Returns current month reference in YYYY-MM format, e.g. "2026-05". */
    private fun currentAnomes(cal: Calendar): String {
        val year  = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1  // Calendar.MONTH is 0-based
        return "$year-${month.toString().padStart(2, '0')}"
    }

    private fun currentMesLabel(cal: Calendar): String {
        val monthNames = listOf(
            "janeiro","fevereiro","março","abril","maio","junho",
            "julho","agosto","setembro","outubro","novembro","dezembro",
        )
        return "${monthNames[cal.get(Calendar.MONTH)]} ${cal.get(Calendar.YEAR)}"
    }

    private fun buildVencimentoLabel(ass: Assinatura, todayDay: Int): String =
        if (ass.modalidade == Modalidade.ANUAL) {
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
