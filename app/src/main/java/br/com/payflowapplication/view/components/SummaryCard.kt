package br.com.payflowapplication.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

/**
 * MD3 Summary / Hero card — matches the HTML mockup's gradient card with 3 metric boxes.
 */
@Composable
fun SummaryCard(
    totalMensal: Double,
    totalAtivas: Int,
    totalPoucoUsadas: Int,
    totalVenceHoje: Int,
    modifier: Modifier = Modifier,
) {

    val fmt = NumberFormat.getInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    // Split value into integer and decimal parts for styled display
    val parts = fmt.format(totalMensal).split(",")
    val intPart = parts[0]
    val decPart = if (parts.size > 1) ",${parts[1]}" else ",00"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        Color(0xFF003040)
                    )
                )
            )
            .padding(20.dp)
    ) {

        Column {

            Text(
                text = "Total mensal",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = buildAnnotatedString {
                    append("R$ $intPart")

                    withStyle(
                        SpanStyle(fontSize = 24.sp)
                    ) {
                        append(decPart)
                    }
                },

                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.primary,

                letterSpacing = (-1).sp
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                MetricBox(
                    label = "Ativas",
                    value = totalAtivas.toString(),

                    valueColor =
                        MaterialTheme.colorScheme.onPrimaryContainer,

                    modifier = Modifier.weight(1f)
                )

                MetricBox(
                    label = "Pouco usadas",
                    value = totalPoucoUsadas.toString(),

                    valueColor =
                        MaterialTheme.colorScheme.secondary,

                    labelColor =
                        MaterialTheme.colorScheme.secondary
                            .copy(alpha = 0.9f),

                    modifier = Modifier.weight(1f)
                )

                MetricBox(
                    label = "Vence hoje",
                    value = totalVenceHoje.toString(),

                    valueColor =
                        if (totalVenceHoje > 0)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer,

                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetricBox(
    label: String,
    value: String,
    valueColor: Color,
    labelColor: Color =
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                MaterialTheme.colorScheme.scrim.copy(alpha = 0.2f)
            )
            .padding(10.dp),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = labelColor
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}