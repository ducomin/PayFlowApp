package br.com.payflowapplication.view.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * VisualTransformation que exibe dígitos brutos como moeda brasileira (BRL).
 *
 * Entrada armazenada: somente dígitos  → "14500"
 * Exibição formatada: moeda BR        → "145,00"
 *
 * Exemplos:
 *   ""         →  "0,00"      (estado vazio / placeholder)
 *   "5"        →  "0,05"
 *   "50"       →  "0,50"
 *   "100"      →  "1,00"
 *   "14500"    →  "145,00"
 *   "105000"   →  "1.050,00"
 *   "1234567"  →  "12.345,67"
 */
class CurrencyVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text          // apenas dígitos, armazenado no estado
        val formatted = formatBRL(digits)

        // O cursor sempre fica no final do texto transformado
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length
            override fun transformedToOriginal(offset: Int): Int = digits.length
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }

    companion object {
        /**
         * Converte string de dígitos em valor BRL formatado.
         * @param digits  somente [0-9], ex: "14500"
         * @return        string formatada, ex: "145,00"
         */
        fun formatBRL(digits: String): String {
            if (digits.isEmpty()) return "0,00"

            // Garante pelo menos 3 chars para ter inteiro + 2 decimais
            val padded = digits.padStart(3, '0')

            val intPart = padded.dropLast(2).trimStart('0').ifEmpty { "0" }
            val decPart = padded.takeLast(2)

            // Insere separadores de milhar no intPart
            val intFormatted = buildString {
                intPart.reversed().forEachIndexed { index, ch ->
                    if (index > 0 && index % 3 == 0) append('.')
                    append(ch)
                }
            }.reversed()

            return "$intFormatted,$decPart"
        }

        /**
         * Converte string de dígitos para Double (valor monetário).
         * @param digits  ex: "14500"  → 145.00
         */
        fun digitsToDouble(digits: String): Double {
            if (digits.isEmpty()) return 0.0
            return digits.toLongOrNull()?.let { it / 100.0 } ?: 0.0
        }
    }
}

