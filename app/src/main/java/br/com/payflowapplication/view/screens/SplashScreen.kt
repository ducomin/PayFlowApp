package br.com.payflowapplication.view.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ─── PayFlow Splash Screen ────────────────────────────────────────────────────
//
// Opção 3 – Slide & Reveal (identical to opcao-3-slide-reveal.html)
// Phase 1 (0–520ms):  icon bounces in from below with overshoot
// Phase 2 (420–850ms): wordmark "Pay|Flow" slides up and fades in
// After 2 s total → onFinished() navigates to Home
//
// Brand colours (from guia-rapido-branding.md):
//   Background     #191C1E
//   PrimaryContainer #00525C  (icon bg gradient start)
//   teal deep      #006874   (icon bg gradient end)
//   teal-soft      #9EEEFF   (glyph colour)
//   amber          #FFD54F   (flow dot colour)
// ─────────────────────────────────────────────────────────────────────────────

private val SplashBackground     = Color(0xFF191C1E)
private val IconGradientStart    = Color(0xFF00525C)   // stop 0% do SVG background
private val IconGradientMid      = Color(0xFF006874)   // stop 62%
private val IconGradientEnd      = Color(0xFF3D2F00)   // stop 100%
private val GlyphTeal            = Color(0xFF9EEEFF)   // OnPrimaryContainer
private val GlyphAmber           = Color(0xFFFFEDBC)   // OnSecondaryContainer (âmbar suave)
private val TextWhite            = Color(0xFFE1E2E5)
private val TextTeal             = Color(0xFF4DD0E1)

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    // ── Animation states ─────────────────────────────────────────────────────

    // Icon: bounce (scale + translateY)
    val iconVisible = remember { MutableTransitionState(false).apply { targetState = true } }
    val iconTransition = rememberTransition(iconVisible, label = "iconBounce")

    val iconScale by iconTransition.animateFloat(
        label = "scale",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        }
    ) { visible -> if (visible) 1f else 0.88f }

    val iconOffsetY by iconTransition.animateFloat(
        label = "translateY",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        }
    ) { visible -> if (visible) 0f else 14f }

    val iconAlpha by iconTransition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(280) }
    ) { visible -> if (visible) 1f else 0f }

    // Wordmark: slide-up + fade (delayed 420 ms)
    var wordmarkVisible by remember { mutableStateOf(false) }
    val wordmarkAlpha by animateFloatAsState(
        targetValue = if (wordmarkVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 430, delayMillis = 0),
        label = "wordmarkAlpha"
    )
    val wordmarkOffset by animateFloatAsState(
        targetValue = if (wordmarkVisible) 0f else 22f,
        animationSpec = tween(durationMillis = 430, easing = FastOutSlowInEasing),
        label = "wordmarkOffset"
    )

    // Sequence controller
    LaunchedEffect(Unit) {
        delay(420)          // icon bounce settles
        wordmarkVisible = true
        delay(1600)         // hold branded screen
        onFinished()
    }

    // ── Layout ────────────────────────────────────────────────────────────────

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Icon ─────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .offset(y = iconOffsetY.dp)
                    .scale(iconScale)
                    .alpha(iconAlpha)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(
                            colorStops = arrayOf(
                                0.00f to IconGradientStart,
                                0.62f to IconGradientMid,
                                1.00f to IconGradientEnd
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                PayFlowGlyph(
                    tealColor  = GlyphTeal,
                    amberColor = GlyphAmber,
                    modifier   = Modifier.size(58.dp)
                )
            }

            // ── Wordmark ─────────────────────────────────────────────────────
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextWhite)) { append("Pay") }
                    withStyle(SpanStyle(color = TextTeal))  { append("Flow") }
                },
                fontSize   = 30.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.6).sp,
                modifier = Modifier
                    .offset(y = wordmarkOffset.dp)
                    .alpha(wordmarkAlpha)
            )
        }
    }
}

// ─── PayFlow "P + flow" glyph — fiel ao brand kit SVG ────────────────────────
//
// SVG original (viewBox 0 0 108 108, com translate(18 18)):
//   P path:    M18 10H40C55.464 10 68 22.536 68 38... (coords locais, +18+18 = canvas coords)
//   Flow path: M36 56.5C46.3 56.3... (coords locais)
//
// Para o Canvas Compose usamos uma viewport normalizada 0..1
// mapeada pro tamanho real do composable.
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PayFlowGlyph(
    tealColor:  Color,
    amberColor: Color,
    modifier:   Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // O SVG original tem viewBox 72×72 (conteúdo dentro do translate 18,18 do 108×108)
        // Normalizamos para 72×72 e escalamos para o tamanho do Canvas.
        val vw = 72f
        val vh = 72f
        val sx = w / vw
        val sy = h / vh

        // ── Letra P ──────────────────────────────────────────────────────────
        // Path original (coords locais pós-translate):
        // M18 10 H40 C55.464 10 68 22.536 68 38 C68 53.464 55.464 66 40 66
        // H30 V50 H39 C45.627 50 51 44.627 51 38 C51 31.373 45.627 26 39 26
        // H34 V66 H18 V10 Z
        val pPath = Path().apply {
            moveTo(18f * sx, 10f * sy)
            lineTo(40f * sx, 10f * sy)
            cubicTo(55.464f * sx, 10f * sy,  68f * sx, 22.536f * sy, 68f * sx, 38f * sy)
            cubicTo(68f * sx, 53.464f * sy,  55.464f * sx, 66f * sy, 40f * sx, 66f * sy)
            lineTo(30f * sx, 66f * sy)
            lineTo(30f * sx, 50f * sy)
            lineTo(39f * sx, 50f * sy)
            cubicTo(45.627f * sx, 50f * sy,  51f * sx, 44.627f * sy, 51f * sx, 38f * sy)
            cubicTo(51f * sx, 31.373f * sy,  45.627f * sx, 26f * sy, 39f * sx, 26f * sy)
            lineTo(34f * sx, 26f * sy)
            lineTo(34f * sx, 66f * sy)
            lineTo(18f * sx, 66f * sy)
            close()
        }
        drawPath(path = pPath, color = tealColor)

        // ── Cauda flow (âmbar suave) ─────────────────────────────────────────
        // Path original:
        // M36 56.5 C46.3 56.3 53.7 51.5 59.2 45.5
        //           C62.6 41.9 67.9 41.6 71.5 45
        //           C75.1 48.4 75.4 53.7 72 57.3
        //           C62.4 67.4 50.7 73.2 36.5 73.5 L36 56.5 Z
        val flowPath = Path().apply {
            moveTo(36f * sx, 56.5f * sy)
            cubicTo(46.3f * sx, 56.3f * sy,  53.7f * sx, 51.5f * sy, 59.2f * sx, 45.5f * sy)
            cubicTo(62.6f * sx, 41.9f * sy,  67.9f * sx, 41.6f * sy, 71.5f * sx, 45f * sy)
            cubicTo(75.1f * sx, 48.4f * sy,  75.4f * sx, 53.7f * sy, 72f * sx,   57.3f * sy)
            cubicTo(62.4f * sx, 67.4f * sy,  50.7f * sx, 73.2f * sy, 36.5f * sx, 73.5f * sy)
            lineTo(36f * sx, 56.5f * sy)
            close()
        }
        drawPath(path = flowPath, color = amberColor)
    }
}



