package br.com.payflowapplication.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.payflowapplication.model.Modalidade

// ─────────────────────────────────────────────────────────────────────────────
// Component: M3 SegmentedButton — Modalidade selection (Mensal / Anual)
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalidadeSegmentedButton(
    selected: Modalidade,
    onSelect: (Modalidade) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = Modalidade.entries.toList()
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
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
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}
