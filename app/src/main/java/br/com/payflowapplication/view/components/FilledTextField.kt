package br.com.payflowapplication.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.payflowapplication.ui.theme.Error
import br.com.payflowapplication.ui.theme.OnSurface
import br.com.payflowapplication.ui.theme.OnSurfaceVariant
import br.com.payflowapplication.ui.theme.Outline
import br.com.payflowapplication.ui.theme.Primary
import br.com.payflowapplication.ui.theme.SurfaceContainerHighest

// ─────────────────────────────────────────────────────────────────────────────
// Component: M3 Filled TextField with floating label and error support
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun FilledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    prefix: String? = null,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    val isError = errorMessage != null
    Column(modifier = modifier.fillMaxWidth()) {
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

@Composable
private fun ErrorSupportText(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = Error,
        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
    )
}
