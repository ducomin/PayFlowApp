package br.com.payflowapplication.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import br.com.payflowapplication.model.Assinatura
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
 * Can be adapted for history screen by hiding the usage bar.
 */
@Composable
fun AssinaturaCard(
    assinatura: Assinatura,
    isHistorico: Boolean = false,
    vencimentoLabel: String = "",
    poucoUsada: Boolean = false,
    venceHoje: Boolean = false,
    usage: Float = 0f,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val fmt = NumberFormat.getInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    val valorLabel = if (assinatura.modalidade == Modalidade.ANUAL)
        "R$ ${fmt.format(assinatura.valor / 12.0)}/mês"
    else
        "R$ ${fmt.format(assinatura.valor)}"

    val borderModifier = if (poucoUsada)
        Modifier.border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
    else Modifier

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(borderModifier)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header row: avatar + name + value
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Service logo avatar
                ServiceAvatar(name = assinatura.nomeServico, categoria = assinatura.categoria)

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = assinatura.nomeServico,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (isHistorico) {
                            Text(
                                text = "R$ ${fmt.format(assinatura.valor)}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = valorLabel,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // Status chip + vencimento
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusChip(
                            isHistorico = isHistorico,
                            poucoUsada = poucoUsada,
                            venceHoje = venceHoje
                        )
                        if (isHistorico) {
                            val dataFimStr = try {
                                val dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yy")
                                assinatura.dataFim?.format(dateFormatter) ?: "N/A"
                            } catch (e: Exception) { "N/A" }
                            
                            Text(
                                text = "Finalizada em $dataFimStr",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = vencimentoLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Usage progress (only if not in history mode)
            if (!isHistorico) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Uso este mês",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val usagePct = (usage * 100).toInt()
                    val successColor = if (isSystemInDarkTheme()) Success else LightSuccess
                    val usageColor = when {
                        poucoUsada -> MaterialTheme.colorScheme.secondary
                        usagePct >= 70 -> successColor
                        else -> MaterialTheme.colorScheme.primary
                    }
                    Text(
                        text = "$usagePct%",
                        style = MaterialTheme.typography.labelSmall,
                        color = usageColor
                    )
                }
                Spacer(Modifier.height(4.dp))
                UsageProgressBar(progress = usage, poucoUsada = poucoUsada)
            }
        }
    }
}

@Composable
fun AssinaturaCard(
    item: AssinaturaUiItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AssinaturaCard(
        assinatura = item.assinatura,
        isHistorico = false,
        vencimentoLabel = item.vencimentoLabel,
        poucoUsada = item.poucoUsada,
        venceHoje = item.venceHoje,
        usage = item.usage,
        onClick = onClick,
        modifier = modifier
    )
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
private fun StatusChip(isHistorico: Boolean, poucoUsada: Boolean, venceHoje: Boolean) {
    val isDark = isSystemInDarkTheme()

    // Cores de Success respondem ao tema:
    //   Dark  → fundo SuccessContainer (#005225) + texto Success (#6DD58C) verde claro
    //   Light → fundo LightSuccess (#2E7D32)     + texto branco (#FFFFFF)
    val successBg = if (isDark) SuccessContainer else LightSuccess
    val successFg = if (isDark) Success          else LightOnSuccess

    val (text, bg, fg) = when {
        isHistorico -> Triple("Inativa",      MaterialTheme.colorScheme.surfaceVariant,    MaterialTheme.colorScheme.onSurfaceVariant)
        venceHoje   -> Triple("● Vence hoje", MaterialTheme.colorScheme.errorContainer,    MaterialTheme.colorScheme.error)
        poucoUsada  -> Triple("⚠ Pouco usada", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.secondary)
        else        -> Triple("● Ativa",      successBg,                                  successFg)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun UsageProgressBar(progress: Float, poucoUsada: Boolean) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val successColor = if (isSystemInDarkTheme()) Success else LightSuccess
    val fillColor = when {
        poucoUsada       -> MaterialTheme.colorScheme.secondary
        progress >= 0.7f -> successColor
        else             -> MaterialTheme.colorScheme.primary
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
