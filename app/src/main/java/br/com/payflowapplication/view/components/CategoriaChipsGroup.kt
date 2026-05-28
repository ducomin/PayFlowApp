package br.com.payflowapplication.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.payflowapplication.model.CategoriaAssinatura

// ─────────────────────────────────────────────────────────────────────────────
// Component: Category Filter Chips (wrapping row with 2 columns)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CategoriaChipsGroup(
    selected: CategoriaAssinatura?,
    onSelect: (CategoriaAssinatura) -> Unit,
    modifier: Modifier = Modifier
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
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        all.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { categoria ->
                    FilterChip(
                        selected = selected == categoria,
                        onClick = { onSelect(categoria) },
                        label = {
                            Text(
                                text = "${emojiMap[categoria]} ${categoria.label}",
                                style = MaterialTheme.typography.labelLarge,
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
