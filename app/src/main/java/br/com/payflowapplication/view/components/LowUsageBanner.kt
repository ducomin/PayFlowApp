package br.com.payflowapplication.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.payflowapplication.ui.theme.OnSecondaryContainer
import br.com.payflowapplication.ui.theme.SecondaryContainer
import java.text.NumberFormat
import java.util.Locale

/**
 * Amber warning banner shown when there are low-usage subscriptions.
 * Matches the HTML mockup secondary-container alert row.
 */
@Composable
fun LowUsageBanner(
    count: Int,
    valorMensal: Double,
    modifier: Modifier = Modifier,
) {
    if (count == 0) return

    val fmt = NumberFormat.getInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 2; maximumFractionDigits = 2
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SecondaryContainer)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "⚠️", style = MaterialTheme.typography.titleMedium)
        Column {
            Text(
                text = "$count assinatura${if (count > 1) "s" else ""} pouco utilizada${if (count > 1) "s" else ""}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = OnSecondaryContainer
            )
            Text(
                text = "R$ ${fmt.format(valorMensal)}/mês sem uso real · ver detalhes",
                style = MaterialTheme.typography.bodySmall,
                color = OnSecondaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

