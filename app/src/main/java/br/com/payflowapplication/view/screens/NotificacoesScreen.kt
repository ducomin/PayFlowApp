package br.com.payflowapplication.view.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.payflowapplication.model.Notificacao
import br.com.payflowapplication.view.components.NavTab
import br.com.payflowapplication.view.components.NotificacaoCard
import br.com.payflowapplication.view.components.PayFlowNavBar
import br.com.payflowapplication.viewmodels.GrupoNotificacao
import br.com.payflowapplication.viewmodels.NotificacoesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacoesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    viewModel: NotificacoesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val grupos by viewModel.grupos.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            NotificacoesTopBar(
                naoLidas = uiState.naoLidas,
                onBack = onNavigateBack,
                onLerTodas = { viewModel.lerTodas() }
            )
        },
        bottomBar = {
            PayFlowNavBar(
                selected = NavTab.AVISOS,
                onSelect = { tab ->
                    when (tab) {
                        NavTab.HOME -> onNavigateToHome()
                        NavTab.HISTORICO -> onNavigateToHistory()
                        NavTab.PERFIL -> onNavigateToPerfil()
                        NavTab.AVISOS -> Unit
                    }
                },
                avisosBadge = uiState.naoLidas
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.carregando -> LoadingContent()
                uiState.erro != null -> ErroContent(uiState.erro!!)
                grupos.isEmpty() -> EstadoVazioContent()
                else -> NotificacoesContent(
                    grupos = grupos,
                    removendo = uiState.removendo,
                    onNotificacaoClick = { notif -> viewModel.marcarComoLida(notif.id) }
                )
            }
        }
    }
}

// ─── TopBar ────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificacoesTopBar(
    naoLidas: Int,
    onBack: () -> Unit,
    onLerTodas: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Avisos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                AnimatedVisibility(visible = naoLidas > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.error)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (naoLidas > 99) "99+" else naoLidas.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onError,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        actions = {
            AnimatedVisibility(visible = naoLidas > 0) {
                TextButton(onClick = onLerTodas) {
                    Text(
                        text = "Ler todas",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )
}

// ─── Lista agrupada ────────────────────────────────────────────────────────
@Composable
private fun NotificacoesContent(
    grupos: List<GrupoNotificacao>,
    removendo: Set<Long>,
    onNotificacaoClick: (Notificacao) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grupos.forEach { grupo ->
            val naoLidos = grupo.itens.filter { !it.lida }
            if (naoLidos.isEmpty()) return@forEach

            item(key = "header_${grupo.titulo}") {
                SectionHeader(titulo = grupo.titulo, count = naoLidos.size)
            }

            items(
                items = naoLidos,
                key = { it.id }
            ) { notif ->
                AnimatedVisibility(
                    visible = notif.id !in removendo,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut(animationSpec = tween(200)) +
                            shrinkVertically(animationSpec = tween(300))
                ) {
                    NotificacaoCard(
                        notificacao = notif,
                        onClick = onNotificacaoClick
                    )
                }
            }

            item(key = "spacer_${grupo.titulo}") { Spacer(Modifier.height(8.dp)) }
        }
    }
}

// ─── Cabeçalho de seção ─────────────────────────────────────────────────
@Composable
private fun SectionHeader(titulo: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

// ─── Estado vazio ────────────────────────────────────────────────────────
@Composable
private fun EstadoVazioContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Nenhum aviso no momento",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Você será notificado sobre vencimentos, promoções e uso das suas assinaturas.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Loading ─────────────────────────────────────────────────────────────
@Composable
private fun LoadingContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

// ─── Erro ─────────────────────────────────────────────────────────────────
@Composable
private fun ErroContent(mensagem: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Erro ao carregar avisos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = mensagem,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
