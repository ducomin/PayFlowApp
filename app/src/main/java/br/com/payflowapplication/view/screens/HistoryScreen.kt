package br.com.payflowapplication.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.view.components.AssinaturaCard
import br.com.payflowapplication.viewmodels.HistorySortOption
import br.com.payflowapplication.viewmodels.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Histórico",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint =MaterialTheme .colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    SortMenu(
                        selectedOption = uiState.sortOption,
                        onOptionSelected = viewModel::onSortOptionChange
                    )
                },
                colors =
                    TopAppBarDefaults
                        .centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)

        ) {
            CategoryDropdownFilter(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::onCategoryFilterChange
            )
            when {

                uiState.isLoading -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }
                }

                uiState.error != null -> {

                    Box(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = uiState.error!!,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }
                }

                uiState.historico.isEmpty() -> {

                    Box(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text =
                                "Nenhum resultado encontrado.",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }
                }

                else -> {
                    HistoryList(
                        historico =
                            uiState.historico
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryDropdownFilter(
    selectedCategory: CategoriaAssinatura?,
    onCategorySelected: (CategoriaAssinatura?) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val categories =
        CategoriaAssinatura.values()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        OutlinedButton(

            onClick = {
                expanded = true
            },

            modifier =
                Modifier.fillMaxWidth()

        ) {

            Text(
                text =
                    selectedCategory?.label
                        ?: "Todas as categorias",

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )

            Spacer(
                modifier =
                    Modifier.weight(1f)
            )

            Icon(
                imageVector =
                    Icons.Default
                        .ArrowDropDown,

                contentDescription =
                    "Abrir filtro de categoria",

                tint =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {

            DropdownMenuItem(

                text = {

                    Text(
                        "Todas as categorias"
                    )
                },

                onClick = {

                    onCategorySelected(null)
                    expanded = false
                }
            )

            categories.forEach { category ->

                DropdownMenuItem(

                    text = {
                        Text(category.label)
                    },

                    onClick = {

                        onCategorySelected(
                            category
                        )

                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SortMenu(
    selectedOption: HistorySortOption,
    onOptionSelected: (HistorySortOption) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Box {

        TextButton(
            onClick = {
                expanded = true
            }
        ) {

            Text(
                text =
                    selectedOption.label,

                color =
                    MaterialTheme
                        .colorScheme
                        .primary
            )
        }

        DropdownMenu(
            expanded = expanded,

            onDismissRequest = {
                expanded = false
            }
        ) {

            HistorySortOption
                .values()
                .forEach { option ->

                    DropdownMenuItem(

                        text = {
                            Text(option.label)
                        },

                        onClick = {

                            onOptionSelected(
                                option
                            )

                            expanded = false
                        }
                    )
                }
        }
    }
}

@Composable
fun HistoryList(
    historico: List<Assinatura>
) {

    LazyColumn(

        verticalArrangement =
            Arrangement.spacedBy(10.dp),

        contentPadding =
            PaddingValues(vertical = 16.dp)

    ) {

        items(historico) { assinatura ->

            AssinaturaCard(
                assinatura = assinatura,
                isHistorico = true
            )
        }
    }
}

@Composable
fun StatusChip(
    ativa: Boolean
) {

    val (
        text,
        color,
        containerColor
    ) = if (!ativa) {

        Triple(

            "Inativa",

            MaterialTheme
                .colorScheme
                .onSurfaceVariant,

            MaterialTheme
                .colorScheme
                .surfaceVariant
        )

    } else {

        Triple(

            "Ativa",

            MaterialTheme
                .colorScheme
                .primary,

            MaterialTheme
                .colorScheme
                .primaryContainer
        )
    }

    Surface(
        shape =
            MaterialTheme
                .shapes
                .extraSmall,

        color =
            containerColor,

        contentColor =
            color,
    ) {

        Text(

            text = text,

            style =
                MaterialTheme
                    .typography
                    .labelSmall,

            fontWeight =
                FontWeight.Medium,

            modifier =
                Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 3.dp
                )
        )
    }
}