package br.com.payflowapplication.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.payflowapplication.ui.theme.*
import br.com.payflowapplication.view.components.CategoriaChipsGroup
import br.com.payflowapplication.view.components.CurrencyTextField
import br.com.payflowapplication.view.components.ErrorSupportText
import br.com.payflowapplication.view.components.FieldLabel
import br.com.payflowapplication.view.components.FilledTextField
import br.com.payflowapplication.view.components.ModalidadeSegmentedButton
import br.com.payflowapplication.view.components.StepIndicator
import br.com.payflowapplication.viewmodels.CadastroAssinaturaViewModel

// ─────────────────────────────────────────────────────────────────────────────
// Tela 06 — Cadastro / Edição de Assinatura
// Components: TopAppBar · StepIndicator · FilledTextField · SegmentedButton ·
//             CategoryChips · Outlined+Filled buttons · SnackBar feedback
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroAssinaturaScreen(
    onNavigateBack: () -> Unit,
    onSalvoComSucesso: () -> Unit,
    viewModel: CadastroAssinaturaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigate on success
    LaunchedEffect(uiState.savedSuccessfully) {
        if (uiState.savedSuccessfully) {
            snackbarHostState.showSnackbar("Assinatura salva com sucesso!")
            viewModel.resetSavedFlag()
            onSalvoComSucesso()
        }
    }

    // Unsaved-changes confirmation dialog state
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Descartar alterações?") },
            text = { Text("Você tem alterações não salvas. Deseja sair sem salvar?") },
            confirmButton = {
                TextButton(onClick = {
                    showCancelDialog = false
                    onNavigateBack()
                }) { Text("Sair", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) { Text("Continuar") }
            },
            containerColor = SurfaceContainerHigh,
        )
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            // ── M3 Top App Bar ─────────────────────────────────────────────
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEditMode) "Editar Assinatura" else "Nova Assinatura",
                        style = MaterialTheme.typography.titleLarge,
                        color = OnSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.hasUnsavedChanges) showCancelDialog = true
                        else onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = OnSurface
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.salvar() },
                        enabled = !uiState.isSaving
                    ) {
                        Text(
                            text = "Salvar",
                            style = MaterialTheme.typography.labelLarge,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceContainerLow
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Spacer(Modifier.height(12.dp))

            // ── Step Indicator (3 steps, step 1 & 2 filled) ────────────────
            StepIndicator(currentStep = if (uiState.isEditMode) 3 else 2, totalSteps = 3)

            Spacer(Modifier.height(16.dp))

            // ── Nome do Serviço ─────────────────────────────────────────────
            FilledTextField(
                label = "Nome do serviço *",
                value = uiState.nomeServico,
                onValueChange = viewModel::onNomeChange,
                placeholder = "Ex: Netflix, Spotify…",
                errorMessage = uiState.nomeError
            )

            // ── Valor ────────────────────────────────────────────────────────
            CurrencyTextField(
                label = "Valor *",
                digits = uiState.valor,
                onDigitsChange = viewModel::onValorChange,
                errorMessage = uiState.valorError
            )

            // ── Modalidade — SegmentedButton ────────────────────────────────
            FieldLabel(text = "Modalidade *")
            Spacer(Modifier.height(4.dp))
            ModalidadeSegmentedButton(
                selected = uiState.modalidade,
                onSelect = viewModel::onModalidadeChange
            )
            Spacer(Modifier.height(16.dp))

            // ── Data de Vencimento ───────────────────────────────────────────
            FilledTextField(
                label = "Dia de vencimento *",
                value = uiState.diaVencimento,
                onValueChange = viewModel::onVencimentoChange,
                placeholder = "Ex: 10 (dia do mês)",
                errorMessage = uiState.vencimentoError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // ── Categoria — FilterChips ─────────────────────────────────────
            FieldLabel(text = "Categoria *")
            Spacer(Modifier.height(6.dp))
            CategoriaChipsGroup(
                selected = uiState.categoria,
                onSelect = viewModel::onCategoriaChange
            )
            if (uiState.categoriaError != null) {
                ErrorSupportText(uiState.categoriaError!!)
            }
            Spacer(Modifier.height(16.dp))

            // ── URL (opcional) ───────────────────────────────────────────────
            FilledTextField(
                label = "URL do serviço",
                value = uiState.urlServico,
                onValueChange = viewModel::onUrlChange,
                placeholder = "https://…",
                errorMessage = uiState.urlError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Spacer(Modifier.height(8.dp))

            // ── Action Buttons ───────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Outlined — Cancelar
                OutlinedButton(
                    onClick = {
                        if (uiState.hasUnsavedChanges) showCancelDialog = true
                        else onNavigateBack()
                    },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Primary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(Outline)
                    )
                ) {
                    Text("Cancelar", style = MaterialTheme.typography.labelLarge)
                }

                // Filled — Salvar
                Button(
                    onClick = { viewModel.salvar() },
                    enabled = !uiState.isSaving,
                    modifier = Modifier.weight(2f),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = OnPrimary,
                        disabledContainerColor = SurfaceVariant,
                        disabledContentColor = OnSurfaceVariant
                    )
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = OnPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Salvar assinatura", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}






