package br.com.payflowapplication.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
        targetValue = if (notificacao.lida) SurfaceContainer else config.containerColor,
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
                        color = OnSurface.copy(alpha = contentAlpha),
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
                    color = OnSurfaceVariant.copy(alpha = contentAlpha),
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
                        color = OnSurfaceVariant.copy(alpha = 0.7f)
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
    val label: String,
    val contentDesc: String
)

@Composable
private fun notificacaoConfig(tipo: TipoNotificacao) = when (tipo) {
    TipoNotificacao.VENCIMENTO -> NotifConfig(
        icon = Icons.Default.Warning,
        iconTint = Error,
        iconBackground = ErrorContainer.copy(alpha = 0.6f),
        containerColor = ErrorContainer.copy(alpha = 0.25f),
        label = "Vencimento",
        contentDesc = "Alerta de vencimento"
    )
    TipoNotificacao.BAIXO_USO -> NotifConfig(
        icon = Icons.Default.Info,
        iconTint = Secondary,
        iconBackground = SecondaryContainer.copy(alpha = 0.6f),
        containerColor = SecondaryContainer.copy(alpha = 0.25f),
        label = "Baixo uso",
        contentDesc = "Alerta de baixo uso"
    )
    TipoNotificacao.PROMOCAO -> NotifConfig(
        icon = Icons.Default.Star,
        iconTint = Tertiary,
        iconBackground = TertiaryContainer.copy(alpha = 0.6f),
        containerColor = TertiaryContainer.copy(alpha = 0.25f),
        label = "Promoção",
        contentDesc = "Promoção disponível"
    )
    TipoNotificacao.RENOVACAO -> NotifConfig(
        icon = Icons.Default.CheckCircle,
        iconTint = Success,
        iconBackground = SuccessContainer.copy(alpha = 0.6f),
        containerColor = SuccessContainer.copy(alpha = 0.25f),
        label = "Renovação",
        contentDesc = "Renovação confirmada"
    )
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

