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
// Component: Campo de moeda brasileira (BRL)
//
// • O estado armazena APENAS dígitos: "14500" = R$ 145,00
// • A exibição é formatada via CurrencyVisualTransformation
// • O teclado numérico não exibe vírgula/ponto (KeyboardType.NumberPassword)
//   para forçar entrada limpa; a máscara cuida da formatação visual.
//
// Exemplos de exibição:
//   ""      →  "0,00"       (campo vazio, mostra placeholder formatado)
//   "5"     →  "0,05"
//   "50"    →  "0,50"
//   "100"   →  "1,00"
//   "14500" →  "145,00"
//   "105000"→  "1.050,00"
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CurrencyTextField(
    label: String,
    digits: String,
    onDigitsChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    val isError = errorMessage != null
    val transformation = CurrencyVisualTransformation()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = if (isError) Error else Primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        TextField(
            value = digits,
            onValueChange = { raw ->
                // Aceita apenas dígitos; remove zeros à esquerda; limita 13 chars (R$ 99.999.999,99)
                val cleaned = raw.filter { it.isDigit() }.trimStart('0').take(13)
                onDigitsChange(cleaned)
            },
            visualTransformation = transformation,
            prefix = {
                Text(
                    text = "R$ ",
                    color = OnSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            placeholder = {
                Text(
                    text = "0,00",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant
                )
            },
            isError = isError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
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
            ErrorSupportText(errorMessage ?: "")
        }
        Spacer(Modifier.height(if (isError) 4.dp else 16.dp))
    }
}



