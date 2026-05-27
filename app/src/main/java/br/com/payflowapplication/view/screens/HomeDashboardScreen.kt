package br.com.payflowapplication.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.payflowapplication.ui.theme.*
import br.com.payflowapplication.view.components.*
import br.com.payflowapplication.viewmodels.HomeDashboardUiState
import br.com.payflowapplication.viewmodels.HomeDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    onNovaAssinatura: () -> Unit,
    onAssinaturaClick: (Long) -> Unit,
    onPerfilClick: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToNotificacoes: () -> Unit = {},
    viewModel: HomeDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val avisosNaoLidos by viewModel.avisosNaoLidos.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(NavTab.HOME) }

    // Handle navigation from BottomBar
    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            NavTab.HISTORICO -> {
                onNavigateToHistory()
                selectedTab = NavTab.HOME
            }
            NavTab.AVISOS -> {
                onNavigateToNotificacoes()
                selectedTab = NavTab.HOME
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /* drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                title = {
                    Text(
                        text = "PayFlow",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    // Avatar button
                    IconButton(onClick = onPerfilClick ) {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "US",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        bottomBar = {
            PayFlowNavBar(
                selected = selectedTab,
                onSelect = { selectedTab = it },
                avisosBadge = avisosNaoLidos
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNovaAssinatura,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nova Assinatura") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        when (val state = uiState) {
            is HomeDashboardUiState.Loading -> LoadingContent(Modifier.padding(innerPadding))

            is HomeDashboardUiState.Empty -> EmptyContent(
                modifier = Modifier.padding(innerPadding),
                onNovaAssinatura = onNovaAssinatura
            )

            is HomeDashboardUiState.Error -> ErrorContent(
                message = state.message,
                modifier = Modifier.padding(innerPadding)
            )

            is HomeDashboardUiState.Success -> SuccessContent(
                state = state,
                modifier = Modifier.padding(innerPadding),
                onQueryChange = viewModel::onQueryBuscaChange,
                onCategoriaChange = viewModel::onCategoriaFiltroChange,
                onAssinaturaClick = onAssinaturaClick,
            )
        }
    }
}

// ─── Loading skeleton ─────────────────────────────────────────────────────────

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (it == 0) 130.dp else 90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerHigh)
            )
        }
    }
}

// ─── Empty state ──────────────────────────────────────────────────────────────

@Composable
private fun EmptyContent(
    onNovaAssinatura: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("💳", style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Nenhuma assinatura ainda",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Cadastre sua primeira assinatura e acompanhe todos os seus gastos recorrentes.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onNovaAssinatura) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Adicionar assinatura")
        }
    }
}

// ─── Error state ──────────────────────────────────────────────────────────────

@Composable
private fun ErrorContent(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⚠️", style = MaterialTheme.typography.displayMedium)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Algo deu errado",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ─── Success / main content ───────────────────────────────────────────────────

@Composable
private fun SuccessContent(
    state: HomeDashboardUiState.Success,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    onCategoriaChange: (br.com.payflowapplication.model.CategoriaAssinatura?) -> Unit,
    onAssinaturaClick: (Long) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding = PaddingValues(bottom = 88.dp) // space for FAB above nav
    ) {
        // Greeting
        item {
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)) {
                Text(
                    text = "Olá, ${state.nomeUsuario}! 👋",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = state.mesReferencia,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Summary card
        item {
            SummaryCard(
                totalMensal = state.totalMensal,
                totalAtivas = state.totalAtivas,
                totalPoucoUsadas = state.totalPoucoUsadas,
                totalVenceHoje = state.totalVenceHoje,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        // Search bar
        item {
            HomeSearchBar(
                query = state.queryBusca,
                onQueryChange = onQueryChange,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Filter chips
        item {
            HomeFilterChipsRow(
                selected = state.categoriaFiltro,
                onSelect = onCategoriaChange,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Low-usage warning banner
        if (state.totalPoucoUsadas > 0) {
            item {
                LowUsageBanner(
                    count = state.totalPoucoUsadas,
                    valorMensal = state.valorPoucoUsadas,
                    assinaturas = state.assinaturasPoucoUsadas,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // Section header
        item {
            Text(
                text = "Assinaturas ativas",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        // Empty filtered result
        if (state.assinaturasFiltradas.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔍", style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Nenhuma assinatura encontrada",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Subscription cards
        items(
            items = state.assinaturasFiltradas,
            key = { it.assinatura.id }
        ) { item ->
            AssinaturaCard(
                item = item,
                onClick = { onAssinaturaClick(item.assinatura.id) },
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

