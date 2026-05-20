package br.com.payflowapplication.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade
import br.com.payflowapplication.ui.theme.*
import br.com.payflowapplication.viewmodels.AssinaturaUiItem
import java.text.NumberFormat
import java.util.Locale

/**
 * MD3 Elevated card for a single subscription.
 * Matches the HTML mockup — logo avatar, name, value, status chip,
 * vencimento label and LinearProgressIndicator for usage.
 */
@Composable
fun AssinaturaCard(
    item: AssinaturaUiItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ass = item.assinatura
    val fmt = NumberFormat.getInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    val valorLabel = if (ass.modalidade == Modalidade.ANUAL)
        "R$ ${fmt.format(ass.valor / 12.0)}/mês"
    else
        "R$ ${fmt.format(ass.valor)}"

    val isLowUsage = item.poucoUsada
    val borderModifier = if (isLowUsage)
        Modifier.border(1.dp, SecondaryContainer, RoundedCornerShape(12.dp))
    else Modifier

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(borderModifier)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header row: avatar + name + value
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Service logo avatar
                ServiceAvatar(name = ass.nomeServico, categoria = ass.categoria)

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ass.nomeServico,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            color = OnSurface
                        )
                        Text(
                            text = valorLabel,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // Status chip + vencimento
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusChip(poucoUsada = isLowUsage, venceHoje = item.venceHoje)
                        Text(
                            text = item.vencimentoLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            // Usage progress
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Uso este mês",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariant
                )
                val usagePct = (item.usage * 100).toInt()
                val usageColor = when {
                    item.poucoUsada -> Secondary
                    usagePct >= 70 -> Success
                    else -> Primary
                }
                Text(
                    text = "$usagePct%",
                    style = MaterialTheme.typography.labelSmall,
                    color = usageColor
                )
            }
            Spacer(Modifier.height(4.dp))
            UsageProgressBar(progress = item.usage, poucoUsada = isLowUsage)
        }
    }
}

@Composable
private fun ServiceAvatar(
    name: String,
    categoria: CategoriaAssinatura,
    modifier: Modifier = Modifier,
) {
    val initials = name.take(2).uppercase()
    val bgColor = categoryColor(categoria)
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

private fun categoryColor(categoria: CategoriaAssinatura): Color = when (categoria) {
    CategoriaAssinatura.STREAMING   -> Color(0xFFE50914)
    CategoriaAssinatura.MUSICA      -> Color(0xFF1DB954)
    CategoriaAssinatura.JOGOS       -> Color(0xFF107C10)
    CategoriaAssinatura.PRODUTIVIDADE -> Color(0xFF0078D4)
    CategoriaAssinatura.EDUCACAO    -> Color(0xFFF4A400)
    CategoriaAssinatura.SAUDE       -> Color(0xFF00BCD4)
    CategoriaAssinatura.FINANCAS    -> Color(0xFF7B1FA2)
    CategoriaAssinatura.OUTROS      -> Color(0xFF607D8B)
}

@Composable
private fun StatusChip(poucoUsada: Boolean, venceHoje: Boolean) {
    val (text, bg, fg) = when {
        venceHoje  -> Triple("● Vence hoje", ErrorContainer, Error)
        poucoUsada -> Triple("⚠ Pouco usada", SecondaryContainer, Secondary)
        else       -> Triple("● Ativa", SuccessContainer, Success)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = fg,
            fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun UsageProgressBar(progress: Float, poucoUsada: Boolean) {
    val trackColor = SurfaceVariant
    val fillColor = when {
        poucoUsada   -> Secondary
        progress >= 0.7f -> Success
        else         -> Primary
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(fillColor)
        )
    }
}

