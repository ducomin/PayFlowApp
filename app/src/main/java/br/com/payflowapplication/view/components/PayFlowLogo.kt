package br.com.payflowapplication.view.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun PayFlowGlyph(
    tealColor: Color,
    amberColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val vw = 72f
        val vh = 72f
        val sx = w / vw
        val sy = h / vh

        val pPath = Path().apply {
            moveTo(18f * sx, 10f * sy)
            lineTo(40f * sx, 10f * sy)
            cubicTo(55.464f * sx, 10f * sy,  68f * sx, 22.536f * sy, 68f * sx, 38f * sy)
            cubicTo(68f * sx, 53.464f * sy,  55.464f * sx, 66f * sy, 40f * sx, 66f * sy)
            lineTo(30f * sx, 66f * sy)
            lineTo(30f * sx, 50f * sy)
            lineTo(39f * sx, 50f * sy)
            cubicTo(45.627f * sx, 50f * sy,  51f * sx, 44.627f * sy, 51f * sx, 38f * sy)
            cubicTo(51f * sx, 31.373f * sx,  45.627f * sx, 26f * sy, 39f * sx, 26f * sy)
            lineTo(34f * sx, 26f * sy)
            lineTo(34f * sx, 66f * sy)
            lineTo(18f * sx, 66f * sy)
            close()
        }
        drawPath(path = pPath, color = tealColor)

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
