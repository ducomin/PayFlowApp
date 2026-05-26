package br.com.payflowapplication.view.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.payflowapplication.model.CategoriaAssinatura

/**
 * Horizontally scrollable filter chips row.
 * "Todos" chip when [selected] is null.
 */
@Composable
fun HomeFilterChipsRow(
    selected: CategoriaAssinatura?,
    onSelect: (CategoriaAssinatura?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "Todos" chip
        FilterChip(
            selected = selected == null,
            onClick = { onSelect(null) },
            label = { Text("Todos") },
            leadingIcon = if (selected == null) {
                { Text("✓", style = MaterialTheme.typography.labelMedium) }
            } else null
        )

        CategoriaAssinatura.entries.forEach { cat ->
            FilterChip(
                selected = selected == cat,
                onClick = { onSelect(if (selected == cat) null else cat) },
                label = { Text("${categoryEmoji(cat)} ${cat.label}") },
                leadingIcon = if (selected == cat) {
                    { Text("✓", style = MaterialTheme.typography.labelMedium) }
                } else null
            )
        }
    }
}

private fun categoryEmoji(cat: CategoriaAssinatura): String = when (cat) {
    CategoriaAssinatura.STREAMING    -> "🎬"
    CategoriaAssinatura.MUSICA       -> "🎵"
    CategoriaAssinatura.JOGOS        -> "🎮"
    CategoriaAssinatura.PRODUTIVIDADE-> "💼"
    CategoriaAssinatura.EDUCACAO     -> "📚"
    CategoriaAssinatura.SAUDE        -> "🏥"
    CategoriaAssinatura.FINANCAS     -> "💰"
    CategoriaAssinatura.OUTROS       -> "📦"
    CategoriaAssinatura.NONE         -> "" // Handle the NONE case
}
