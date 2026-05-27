package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.repository.NotificacaoRepository
import br.com.payflowapplication.model.Notificacao
import br.com.payflowapplication.model.TipoNotificacao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/** Estado imutável da tela de Notificações */
data class NotificacoesUiState(
    val notificacoes: List<Notificacao> = emptyList(),
    val naoLidas: Int = 0,
    val carregando: Boolean = true,
    val erro: String? = null,
    /** IDs cuja animação de saída já foi disparada mas ainda não persistida */
    val removendo: Set<Long> = emptySet()
)

/** Agrupamento por período para HU-NOTIF-01 */
data class GrupoNotificacao(
    val titulo: String,
    val itens: List<Notificacao>
)

@HiltViewModel
class NotificacoesViewModel @Inject constructor(
    private val repository: NotificacaoRepository
) : ViewModel() {

    // Usuário logado — em produção viria de AuthRepository/PrefsRepository
    private val username = "user1"

    private val _uiState = MutableStateFlow(NotificacoesUiState())
    val uiState: StateFlow<NotificacoesUiState> = _uiState.asStateFlow()

    /** Notificações agrupadas por período (Hoje / Esta semana / Anteriores) */
    val grupos: StateFlow<List<GrupoNotificacao>> = _uiState
        .map { state -> agruparPorPeriodo(state.notificacoes) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        carregarNotificacoes()
    }

    private fun carregarNotificacoes() {
        viewModelScope.launch {
            // Seed inicial se banco estiver vazio
            repository.observar(username)
                .onStart { _uiState.update { it.copy(carregando = true) } }
                .catch { e -> _uiState.update { it.copy(erro = e.message, carregando = false) } }
                .collect { lista ->
                    if (lista.isEmpty()) {
                        repository.inserirTodas(seedNotificacoes(username))
                    } else {
                        val naoLidas = lista.count { !it.lida }
                        _uiState.update {
                            it.copy(notificacoes = lista, naoLidas = naoLidas, carregando = false)
                        }
                    }
                }
        }

        // Badge counter reativo
        viewModelScope.launch {
            repository.contarNaoLidas(username).collect { count ->
                _uiState.update { it.copy(naoLidas = count) }
            }
        }
    }

    /** HU-NOTIF-04: Marca todas como lidas */
    fun lerTodas() {
        viewModelScope.launch {
            repository.marcarTodasComoLidas(username)
        }
    }

    /**
     * HU-NOTIF-05 / click: dispara animação de saída (adiciona ao set `removendo`),
     * aguarda a duração da animação e só então persiste no banco.
     */
    fun marcarComoLida(id: Long) {
        viewModelScope.launch {
            // 1. Marca visualmente como "saindo" → AnimatedVisibility some o item
            _uiState.update { it.copy(removendo = it.removendo + id) }
            // 2. Aguarda a animação (shrinkVertically default ~300ms)
            delay(350)
            // 3. Persiste no banco → o Flow emite nova lista sem o item
            repository.marcarComoLida(id)
            // 4. Limpa o set de removendo (item já saiu da lista via Room Flow)
            _uiState.update { it.copy(removendo = it.removendo - id) }
        }
    }

    // ─── Agrupamento por período ──────────────────────────────────────────────
    private fun agruparPorPeriodo(lista: List<Notificacao>): List<GrupoNotificacao> {
        if (lista.isEmpty()) return emptyList()
        val agora = LocalDateTime.now()
        val inicioDia = agora.toLocalDate().atStartOfDay()
        val inicioSemana = inicioDia.minusDays(agora.dayOfWeek.value.toLong() - 1)

        val hoje = lista.filter { it.criadaEm >= inicioDia }
        val semana = lista.filter { it.criadaEm >= inicioSemana && it.criadaEm < inicioDia }
        val anteriores = lista.filter { it.criadaEm < inicioSemana }

        return buildList {
            if (hoje.isNotEmpty()) add(GrupoNotificacao("Hoje", hoje))
            if (semana.isNotEmpty()) add(GrupoNotificacao("Esta semana", semana))
            if (anteriores.isNotEmpty()) add(GrupoNotificacao("Anteriores", anteriores))
        }
    }
}

// ─── Seed de notificações por usuário ────────────────────────────────────────
fun seedNotificacoes(username: String): List<Notificacao> {
    val agora = LocalDateTime.now()
    return listOf(
        Notificacao(
            titulo = "Vencimento em 2 dias",
            descricao = "Netflix vence no dia ${agora.plusDays(2).dayOfMonth}. Verifique o pagamento.",
            tipo = TipoNotificacao.VENCIMENTO,
            lida = false,
            criadaEm = agora.minusHours(1),
            assinaturaId = 1L,
            username = username
        ),
        Notificacao(
            titulo = "Vencimento amanhã",
            descricao = "Spotify vence amanhã. Certifique-se de que o saldo está disponível.",
            tipo = TipoNotificacao.VENCIMENTO,
            lida = false,
            criadaEm = agora.minusHours(3),
            assinaturaId = 2L,
            username = username
        ),
        Notificacao(
            titulo = "Baixo uso detectado",
            descricao = "Disney+ teve apenas 3 dias de uso esse mês. Considere pausar ou cancelar.",
            tipo = TipoNotificacao.BAIXO_USO,
            lida = false,
            criadaEm = agora.minusHours(5),
            assinaturaId = 3L,
            username = username
        ),
        Notificacao(
            titulo = "Promoção disponível",
            descricao = "HBO Max com 30% de desconto por upgrade para plano familiar. Válido até fim do mês.",
            tipo = TipoNotificacao.PROMOCAO,
            lida = false,
            criadaEm = agora.minusDays(1).minusHours(2),
            assinaturaId = 4L,
            username = username
        ),
        Notificacao(
            titulo = "Renovação confirmada",
            descricao = "Amazon Prime Video renovado com sucesso por R$14,90.",
            tipo = TipoNotificacao.RENOVACAO,
            lida = true,
            criadaEm = agora.minusDays(2),
            assinaturaId = 5L,
            username = username
        ),
        Notificacao(
            titulo = "Baixo uso detectado",
            descricao = "Apple TV+ não foi acessado nos últimos 15 dias. Considere cancelar.",
            tipo = TipoNotificacao.BAIXO_USO,
            lida = true,
            criadaEm = agora.minusDays(3),
            assinaturaId = 6L,
            username = username
        ),
        Notificacao(
            titulo = "Renovação confirmada",
            descricao = "Globoplay renovado com sucesso por R$24,90.",
            tipo = TipoNotificacao.RENOVACAO,
            lida = true,
            criadaEm = agora.minusDays(5),
            assinaturaId = 7L,
            username = username
        ),
        Notificacao(
            titulo = "Promoção disponível",
            descricao = "Paramount+ com plano anual por R$99,90. Economia de R$119,88 no ano.",
            tipo = TipoNotificacao.PROMOCAO,
            lida = true,
            criadaEm = agora.minusDays(6),
            assinaturaId = 8L,
            username = username
        )
    )
}
