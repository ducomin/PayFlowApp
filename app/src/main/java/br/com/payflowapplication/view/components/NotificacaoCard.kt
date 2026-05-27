package br.com.payflowapplication.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.payflowapplication.model.Notificacao
import br.com.payflowapplication.model.TipoNotificacao
import br.com.payflowapplication.ui.theme.*
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Card de notificação — MD3 filled card com destaque semântico por severidade.
 * Atende HU-NOTIF-02 e UX: ícone + cor + copy (não depende só de cromática).
 */
@Composable
fun NotificacaoCard(
    notificacao: Notificacao,
    onClick: (Notificacao) -> Unit,
    modifier: Modifier = Modifier
) {
    val config = notificacaoConfig(notificacao.tipo)

    val containerColor by animateColorAsState(
        targetValue = if (notificacao.lida) MaterialTheme.colorScheme.surfaceContainer else config.containerColor,
        label = "notif_bg"
    )
    val contentAlpha = if (notificacao.lida) 0.6f else 1f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(notificacao) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notificacao.lida) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Ícone semântico ────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(config.iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = config.icon,
                    contentDescription = config.contentDesc,
                    tint = config.iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            // ── Conteúdo ───────────────────────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = notificacao.titulo,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (notificacao.lida) FontWeight.Normal else FontWeight.SemiBold,
                        // onContainerColor harmoniza com o tint do fundo do card
                        color = if (notificacao.lida)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha)
                        else
                            config.onContainerColor.copy(alpha = contentAlpha),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    // Badge de tipo
                    TypeBadge(tipo = notificacao.tipo, lida = notificacao.lida)
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = notificacao.descricao,
                    style = MaterialTheme.typography.bodySmall,
                    // descrição levemente mais suave: onContainerColor com alpha reduzido
                    color = if (notificacao.lida)
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha)
                    else
                        config.onContainerColor.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = formatarData(notificacao.criadaEm),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (notificacao.lida)
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        else
                            config.onContainerColor.copy(alpha = 0.6f)
                    )
                    if (!notificacao.lida) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(config.iconTint)
                        )
                    }
                }
            }
        }
    }
}

// ─── Badge de tipo ─────────────────────────────────────────────────────────
@Composable
private fun TypeBadge(tipo: TipoNotificacao, lida: Boolean) {
    if (lida) return
    val config = notificacaoConfig(tipo)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(config.iconBackground)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = config.label,
            style = MaterialTheme.typography.labelSmall,
            color = config.iconTint,
            fontWeight = FontWeight.Medium
        )
    }
}

// ─── Configuração semântica por tipo ───────────────────────────────────────
private data class NotifConfig(
    val icon: ImageVector,
    val iconTint: Color,
    val iconBackground: Color,
    val containerColor: Color,
    // Cor do texto principal dentro do card — usa o token "onContainer" do tipo,
    // garantindo que título e descrição harmonizem com o tint do fundo do card.
    val onContainerColor: Color,
    val label: String,
    val contentDesc: String
)

@Composable
private fun notificacaoConfig(tipo: TipoNotificacao): NotifConfig {
    val isDark = isSystemInDarkTheme()
    // Dark  → containerColor com alpha 0.25 (o fundo escuro base já é visível)
    // Light → containerColor SEM alpha (usa a cor plena do container token) para
    //         que o fundo do card fique com o mesmo tint forte do ícone/borda.
    return when (tipo) {
        TipoNotificacao.VENCIMENTO -> NotifConfig(
            icon             = Icons.Default.Warning,
            iconTint         = if (isDark) Error                    else LightError,
            iconBackground   = if (isDark) ErrorContainer           else LightErrorContainer,
            containerColor   = if (isDark) ErrorContainer.copy(alpha = 0.25f) else LightErrorContainer,
            onContainerColor = if (isDark) OnErrorContainer         else LightOnErrorContainer,
            label            = "Vencimento",
            contentDesc      = "Alerta de vencimento"
        )
        TipoNotificacao.BAIXO_USO -> NotifConfig(
            icon             = Icons.Default.Info,
            iconTint         = if (isDark) Secondary                else LightSecondary,
            iconBackground   = if (isDark) SecondaryContainer       else LightSecondaryContainer,
            containerColor   = if (isDark) SecondaryContainer.copy(alpha = 0.25f) else LightSecondaryContainer,
            onContainerColor = if (isDark) OnSecondaryContainer     else LightOnSecondaryContainer,
            label            = "Baixo uso",
            contentDesc      = "Alerta de baixo uso"
        )
        TipoNotificacao.PROMOCAO -> NotifConfig(
            icon             = Icons.Default.Star,
            iconTint         = if (isDark) Tertiary                 else LightTertiary,
            iconBackground   = if (isDark) TertiaryContainer        else LightTertiaryContainer,
            containerColor   = if (isDark) TertiaryContainer.copy(alpha = 0.25f) else LightTertiaryContainer,
            onContainerColor = if (isDark) OnTertiaryContainer      else LightOnTertiaryContainer,
            label            = "Promoção",
            contentDesc      = "Promoção disponível"
        )
        TipoNotificacao.RENOVACAO -> NotifConfig(
            icon             = Icons.Default.CheckCircle,
            iconTint         = if (isDark) Success                  else LightSuccess,
            iconBackground   = if (isDark) SuccessContainer         else LightSuccessContainer,
            containerColor   = if (isDark) SuccessContainer.copy(alpha = 0.25f) else LightSuccessContainer,
            onContainerColor = if (isDark) OnSuccess                else LightOnSuccessContainer,
            label            = "Renovação",
            contentDesc      = "Renovação confirmada"
        )
    }
}

// ─── Formatação de data ────────────────────────────────────────────────────
private val horaFmt = DateTimeFormatter.ofPattern("HH:mm")
private val dataFmt = DateTimeFormatter.ofPattern("dd/MM")

private fun formatarData(dt: LocalDateTime): String {
    val hoje = LocalDate.now()
    return when (dt.toLocalDate()) {
        hoje -> "Hoje, ${dt.format(horaFmt)}"
        hoje.minusDays(1) -> "Ontem, ${dt.format(horaFmt)}"
        else -> dt.format(dataFmt)
    }
}

