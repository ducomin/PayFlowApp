package br.com.payflowapplication.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import br.com.payflowapplication.ui.theme.*

/**
 * Data model for each subscription entry shown in the chart.
 *
 * @param nome      Display name of the subscription (e.g. "Netflix")
 * @param diasUso   Days of use in the current month (0..31)
 * @param valorMes  Monthly cost in BRL
 */
data class AssinaturaUso(
    val nome: String,
    val diasUso: Int,
    val valorMes: Double,
)

/**
 * Animated full-screen overlay modal with horizontal bar chart showing up to
 * 5 low-usage subscriptions ordered from least used (top) to most used (bottom).
 */
@Composable
fun LowUsageChartModal(
    assinaturas: List<AssinaturaUso>,
    onDismiss: () -> Unit,
) {
    val items = assinaturas.sortedBy { it.diasUso }.take(5)
    val maxDias = items.maxOfOrNull { it.diasUso }?.coerceAtLeast(1) ?: 1

    // Drive AnimatedVisibility: start invisible, immediately flip to visible
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = false, // we handle it ourselves
        ),
    ) {
        // Full-screen scrim that consumes all pointer events on itself
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(onDismiss) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            // consume every event that hits the scrim
                            event.changes.forEach { it.consume() }
                            // dismiss when the finger lifts on the scrim
                            if (event.changes.all { !it.pressed }) {
                                onDismiss()
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            // Semi-transparent backdrop (always shown behind the card)
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(250)),
                exit  = fadeOut(tween(200)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                )
            }

            // Card with slide-up + fade animation
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(340, easing = EaseOutBack),
                ) + fadeIn(tween(260)),
                exit = slideOutVertically(
                    targetOffsetY = { it / 4 },
                    animationSpec = tween(220, easing = EaseIn),
                ) + fadeOut(tween(180)),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        // consume pointer events so taps on the card don't reach the scrim
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    event.changes.forEach { it.consume() }
                                }
                            }
                        }
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceContainerHigh)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    // ── Header ──────────────────────────────────────────────
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Assinaturas pouco utilizadas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface,
                            )
                            Text(
                                text = "Dias de uso no mês atual",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceVariant,
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar",
                                tint = OnSurfaceVariant,
                            )
                        }
                    }

                    // ── Bar chart ────────────────────────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items.forEachIndexed { index, item ->
                            BarRow(
                                item = item,
                                maxDias = maxDias,
                                animationDelay = index * 80,
                            )
                        }
                        // X-axis labels
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = NAME_COL_WIDTH),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            listOf("0", "${maxDias / 2}", "$maxDias dias").forEach { label ->
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OnSurfaceVariant,
                                )
                            }
                        }
                    }

                    // ── Footer hint ──────────────────────────────────────────
                    HorizontalDivider(color = OutlineVariant)
                    Text(
                        text = "💡 Considere cancelar assinaturas com 0–3 dias de uso.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Secondary.copy(alpha = 0.9f),
                    )
                }
            }
        }
    }
}

private val NAME_COL_WIDTH = 88.dp

@Composable
private fun BarRow(
    item: AssinaturaUso,
    maxDias: Int,
    animationDelay: Int,
) {
    val fraction = item.diasUso.toFloat() / maxDias.toFloat()
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(animationDelay.toLong())
        started = true
    }
    val animFraction by animateFloatAsState(
        targetValue = if (started) fraction else 0f,
        animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
        label = "bar-${item.nome}",
    )

    val barColor = when {
        item.diasUso == 0  -> Error
        item.diasUso <= 5  -> Secondary
        else               -> Primary
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = item.nome,
            style = MaterialTheme.typography.bodySmall,
            color = OnSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(NAME_COL_WIDTH),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(22.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SurfaceContainerHighest),
        ) {
            if (animFraction > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animFraction)
                        .clip(RoundedCornerShape(4.dp))
                        .background(barColor),
                )
            }
            Text(
                text = "${item.diasUso}d",
                style = MaterialTheme.typography.labelSmall,
                color = if (animFraction > 0.3f) OnSurface else OnSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 6.dp),
            )
        }
    }
}
