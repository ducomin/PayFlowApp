package br.com.payflowapplication.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.payflowapplication.ui.theme.Error
import br.com.payflowapplication.ui.theme.Primary

// ─────────────────────────────────────────────────────────────────────────────
// Atomic helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun FieldLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium,
        color = Primary,
        modifier = modifier.padding(start = 16.dp)
    )
}

@Composable
fun ErrorSupportText(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = Error,
        modifier = modifier.padding(start = 16.dp, top = 4.dp)
    )
}
