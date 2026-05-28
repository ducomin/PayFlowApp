package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.preferences.AuthPreferencesDataStore
import br.com.payflowapplication.data.repository.AssinaturaRepository
import br.com.payflowapplication.data.repository.NotificacaoRepository
import br.com.payflowapplication.data.repository.StreamingRepository
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.ConsumoMensal
import br.com.payflowapplication.model.Modalidade
import br.com.payflowapplication.view.components.AssinaturaUso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
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
    private val notificacaoRepository: NotificacaoRepository,
    private val authPreferences: AuthPreferencesDataStore,
) : ViewModel() {

    private val _queryBusca     = MutableStateFlow("")
    private val _categoriaFiltro = MutableStateFlow<CategoriaAssinatura?>(null)

    private val _uiState = MutableStateFlow<HomeDashboardUiState>(HomeDashboardUiState.Loading)
    val uiState: StateFlow<HomeDashboardUiState> = _uiState.asStateFlow()

    /** Contagem reativa de notificações não lidas — exposta para o badge da NavBar */
    @OptIn(ExperimentalCoroutinesApi::class)
    val avisosNaoLidos: StateFlow<Int> = authPreferences.username
        .flatMapLatest { user ->
            notificacaoRepository.contarNaoLidas(user ?: "default")
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /**
     * In-session cache: key = "nomeServico:anomes" (lowercase), value = ConsumoMensal.
     * Avoids hitting the API again for the same service within the same session.
     */
    private val usageCache = mutableMapOf<String, ConsumoMensal>()

    init {
        viewModelScope.launch {
            combine(
                assinaturaRepository.getAtivas(),
                _queryBusca,
                _categoriaFiltro,
                authPreferences.username
            ) { lista, query, categoria, user ->
                DataParams(lista, query, categoria, user ?: "Usuário")
            }
                .catch { e -> _uiState.value = HomeDashboardUiState.Error(e.message ?: "Erro") }
                .collect { params ->
                    // Emit loading skeleton on first load only
                    if (_uiState.value is HomeDashboardUiState.Loading) {
                        _uiState.value = HomeDashboardUiState.Loading
                    }
                    val state = buildUiState(params)
                    _uiState.value = state
                }
        }
    }

    private data class DataParams(
        val lista: List<Assinatura>,
        val query: String,
        val categoria: CategoriaAssinatura?,
        val nomeUsuario: String
    )

    fun onQueryBuscaChange(query: String)             = _queryBusca.update { query }
    fun onCategoriaFiltroChange(c: CategoriaAssinatura?) = _categoriaFiltro.update { c }
    fun onDeleteAssinatura(ass: Assinatura) {
        viewModelScope.launch { assinaturaRepository.delete(ass) }
    }

    // ─── Build state ──────────────────────────────────────────────────────────

    private suspend fun buildUiState(params: DataParams): HomeDashboardUiState {
        val (lista, query, categoria, nomeUsuario) = params
        
        if (lista.isEmpty()) return HomeDashboardUiState.Empty

        val cal      = Calendar.getInstance()
        val todayDay = cal.get(Calendar.DAY_OF_MONTH)
        val anomes   = currentAnomes(cal)
        val mesLabel = currentMesLabel(cal)

        // ── Fetch usage for all subscriptions in parallel ─────────────────────
        val usages: Map<Long, ConsumoMensal> = fetchUsagesParallel(lista, anomes)

        // ── Build enriched items ──────────────────────────────────────────────
        val todosItens = lista.map { ass ->
            val consumo   = usages[ass.id]
            val usage     = consumo?.usageScore ?: 1f   // unknown → not flagged
            val poucoUsada = consumo != null && usage < BAIXO_USO_THRESHOLD
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
        val assinaturasPoucoUsadas = poucoUsadasList.map { item ->
            AssinaturaUso(
                nome     = item.assinatura.nomeServico,
                diasUso  = usages[item.assinatura.id]?.diasUtilizados ?: (item.usage * 30).toInt(),
                valorMes = if (item.assinatura.modalidade == Modalidade.ANUAL)
                               item.assinatura.valor / 12.0
                           else
                               item.assinatura.valor,
            )
        }

        // ── Filter ───────────────────────────────────────────────────────────
        val filtrados = todosItens.filter { item ->
            val matchQuery    = query.isBlank() ||
                item.assinatura.nomeServico.contains(query, ignoreCase = true)
            val matchCategoria = categoria == null ||
                item.assinatura.categoria == categoria
            matchQuery && matchCategoria
        }

        return HomeDashboardUiState.Success(
            nomeUsuario              = nomeUsuario,
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
     * Fetches usage data from the API for every [Assinatura] in parallel.
     * Results are stored in [usageCache] keyed by "nome:anomes" to prevent
     * redundant calls when the list or filters change within the same session.
     *
     * Returns a map of assinatura.id → ConsumoMensal.
     */
    private suspend fun fetchUsagesParallel(
        lista: List<Assinatura>,
        anomes: String,
    ): Map<Long, ConsumoMensal> = coroutineScope {
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
                    usageCache[cacheKey] = consumo
                    ass.id to consumo
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
