package br.com.payflowapplication.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import br.com.payflowapplication.model.Streaming
import br.com.payflowapplication.ui.theme.Error
import br.com.payflowapplication.ui.theme.OnSurface
import br.com.payflowapplication.ui.theme.OnSurfaceVariant
import br.com.payflowapplication.ui.theme.Outline
import br.com.payflowapplication.ui.theme.Primary
import br.com.payflowapplication.ui.theme.SurfaceContainerHigh
import br.com.payflowapplication.ui.theme.SurfaceContainerHighest

// ─────────────────────────────────────────────────────────────────────────────
// Component: Streaming Autocomplete TextField + Dropdown
// Shows phonetic suggestions while typing; notifies parent on selection.
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun StreamingAutocompleteField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<Streaming>,
    isLoading: Boolean,
    showSuggestions: Boolean,
    onSuggestionSelected: (Streaming) -> Unit,
    onDismiss: () -> Unit,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    val isError = errorMessage != null

    Column(modifier = modifier.fillMaxWidth()) {
        // ── Label ────────────────────────────────────────────────────────────
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = if (isError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            },
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )

        // ── Input Field ──────────────────────────────────────────────────────
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Ex: Netflix, Spotify…",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                when {
                    isLoading -> CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    value.isNotEmpty() -> IconButton(onClick = {
                        onValueChange("")
                        onDismiss()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            isError = isError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                errorContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(
                topStart = 4.dp, topEnd = 4.dp,
                bottomStart = if (showSuggestions) 0.dp else 4.dp,
                bottomEnd = if (showSuggestions) 0.dp else 4.dp
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // ── Error Text ───────────────────────────────────────────────────────
        if (isError) {
            ErrorSupportText(errorMessage!!)
        }

        // ── Dropdown Suggestions ─────────────────────────────────────────────
        if (showSuggestions && suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                suggestions.forEachIndexed { index, streaming ->
                    StreamingSuggestionItem(
                        streaming = streaming,
                        onClick = { onSuggestionSelected(streaming) }
                    )
                    if (index < suggestions.lastIndex) {
                        HorizontalDivider(
                            color = Outline.copy(alpha = 0.3f),
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(if (isError) 4.dp else 16.dp))
    }
}

@Composable
private fun StreamingSuggestionItem(
    streaming: Streaming,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = streaming.nome,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = streaming.categoriaPrincipal,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "↵",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

