package br.com.payflowapplication.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade
import br.com.payflowapplication.ui.theme.*
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
            FilledTextField(
                label = "Valor *",
                value = uiState.valor,
                onValueChange = viewModel::onValorChange,
                placeholder = "0,00",
                prefix = "R$ ",
                errorMessage = uiState.valorError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
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

// ─────────────────────────────────────────────────────────────────────────────
// Component: Step Indicator (3 bars, filled = primary, empty = surfaceVariant)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun StepIndicator(currentStep: Int, totalSteps: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(totalSteps) { index ->
                val filled = index < currentStep
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(if (filled) Primary else SurfaceVariant)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Passo $currentStep de $totalSteps",
            style = MaterialTheme.typography.labelSmall,
            color = OnSurfaceVariant
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Component: M3 Filled TextField with floating label and error support
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun FilledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    prefix: String? = null,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val isError = errorMessage != null
    Column(modifier = Modifier.fillMaxWidth()) {
        // Label above the field (matches mockup: label in primary color on top)
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = if (isError) Error else Primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant
                )
            },
            prefix = if (prefix != null) {
                { Text(prefix, color = OnSurfaceVariant, style = MaterialTheme.typography.bodyLarge) }
            } else null,
            isError = isError,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SurfaceContainerHighest,
                unfocusedContainerColor = SurfaceContainerHighest,
                errorContainerColor = SurfaceContainerHighest,
                focusedTextColor = OnSurface,
                unfocusedTextColor = OnSurface,
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Outline,
                errorIndicatorColor = Error,
                cursorColor = Primary,
                errorCursorColor = Error,
                focusedLabelColor = Primary,
                unfocusedLabelColor = OnSurfaceVariant,
                errorLabelColor = Error
            ),
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError) {
            ErrorSupportText(errorMessage!!)
        }
        Spacer(Modifier.height(if (isError) 4.dp else 16.dp))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Component: M3 SegmentedButton — Mensal / Anual
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalidadeSegmentedButton(
    selected: Modalidade,
    onSelect: (Modalidade) -> Unit
) {
    val options = Modalidade.entries.toList()
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, modalidade ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onSelect(modalidade) },
                selected = selected == modalidade,
                label = {
                    Text(
                        text = when (modalidade) {
                            Modalidade.MENSAL -> "Mensal"
                            Modalidade.ANUAL -> "Anual"
                        }
                    )
                }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Component: Category Filter Chips (wrapping row)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CategoriaChipsGroup(
    selected: CategoriaAssinatura?,
    onSelect: (CategoriaAssinatura) -> Unit
) {
    val emojiMap = mapOf(
        CategoriaAssinatura.STREAMING to "🎬",
        CategoriaAssinatura.MUSICA to "🎵",
        CategoriaAssinatura.JOGOS to "🎮",
        CategoriaAssinatura.PRODUTIVIDADE to "💼",
        CategoriaAssinatura.EDUCACAO to "📚",
        CategoriaAssinatura.SAUDE to "🏥",
        CategoriaAssinatura.FINANCAS to "💰",
        CategoriaAssinatura.OUTROS to "📦"
    )

    val all = CategoriaAssinatura.entries.toList()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        all.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { categoria ->
                    FilterChip(
                        selected = selected == categoria,
                        onClick = { onSelect(categoria) },
                        label = {
                            Text(
                                text = "${emojiMap[categoria]} ${categoria.label}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Atomic helpers
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium,
        color = Primary,
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
private fun ErrorSupportText(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = Error,
        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
    )
}





