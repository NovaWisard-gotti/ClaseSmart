package com.educalab.clasesmart.ui.scene

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.Expression
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Personaje ilustrado con cuerpo, postura y expresion real (Regla:
 * "un personaje debe tener cuerpo, postura y expresion", NO un avatar
 * circular generico). Dibujado 100% con Compose Canvas: sin assets
 * remotos, sin PNG externos.
 */
@Composable
fun CharacterSprite(
    skinColor: Color,
    hairColor: Color,
    shirtColor: Color,
    expression: Expression,
    modifier: Modifier = Modifier,
    sizeDp: Int = 96
) {
    Canvas(modifier = modifier.size(sizeDp.dp)) {
        val w = size.width
        val h = size.height

        // Piernas
        drawLine(Color(0xFF4A4A4A), Offset(w * 0.42f, h * 0.78f), Offset(w * 0.40f, h * 0.98f), strokeWidth = w * 0.09f)
        drawLine(Color(0xFF4A4A4A), Offset(w * 0.58f, h * 0.78f), Offset(w * 0.60f, h * 0.98f), strokeWidth = w * 0.09f)

        // Cuerpo (camiseta)
        drawRoundRect(
            color = shirtColor,
            topLeft = Offset(w * 0.28f, h * 0.48f),
            size = androidx.compose.ui.geometry.Size(w * 0.44f, h * 0.34f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.14f, w * 0.14f)
        )
        // Brazos (postura: uno ligeramente levantado para dar sensacion de vida)
        drawLine(shirtColor, Offset(w * 0.30f, h * 0.55f), Offset(w * 0.16f, h * 0.7f), strokeWidth = w * 0.08f)
        drawLine(shirtColor, Offset(w * 0.70f, h * 0.55f), Offset(w * 0.86f, h * 0.45f), strokeWidth = w * 0.08f)

        // Cabeza
        val headCenter = Offset(w * 0.5f, h * 0.32f)
        val headRadius = w * 0.22f
        drawCircle(skinColor, radius = headRadius, center = headCenter)

        // Pelo (forma simple segun mitad superior)
        drawArc(
            color = hairColor,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(headCenter.x - headRadius * 1.05f, headCenter.y - headRadius * 1.15f),
            size = androidx.compose.ui.geometry.Size(headRadius * 2.1f, headRadius * 1.5f)
        )

        drawExpression(expression, headCenter, headRadius)
    }
}

private fun DrawScope.drawExpression(expression: Expression, center: Offset, r: Float) {
    val eyeColor = Color(0xFF2B241C)
    val eyeY = center.y - r * 0.05f
    val eyeOffsetX = r * 0.38f
    val eyeRadius = r * 0.09f

    // Ojos (varian de forma segun expresion)
    when (expression) {
        Expression.SORPRENDIDO -> {
            drawCircle(eyeColor, radius = eyeRadius * 1.4f, center = Offset(center.x - eyeOffsetX, eyeY))
            drawCircle(eyeColor, radius = eyeRadius * 1.4f, center = Offset(center.x + eyeOffsetX, eyeY))
        }
        Expression.PREOCUPADO, Expression.CONFUNDIDO -> {
            drawLine(eyeColor, Offset(center.x - eyeOffsetX - eyeRadius, eyeY - eyeRadius), Offset(center.x - eyeOffsetX + eyeRadius, eyeY + eyeRadius * 0.4f), strokeWidth = r * 0.06f)
            drawLine(eyeColor, Offset(center.x + eyeOffsetX + eyeRadius, eyeY - eyeRadius), Offset(center.x + eyeOffsetX - eyeRadius, eyeY + eyeRadius * 0.4f), strokeWidth = r * 0.06f)
        }
        else -> {
            drawCircle(eyeColor, radius = eyeRadius, center = Offset(center.x - eyeOffsetX, eyeY))
            drawCircle(eyeColor, radius = eyeRadius, center = Offset(center.x + eyeOffsetX, eyeY))
        }
    }

    // Boca (varia de forma segun expresion)
    val mouthY = center.y + r * 0.35f
    val mouthPath = androidx.compose.ui.graphics.Path()
    when (expression) {
        Expression.FELIZ, Expression.ORGULLOSO, Expression.COLABORANDO -> {
            mouthPath.moveTo(center.x - r * 0.3f, mouthY)
            mouthPath.quadraticBezierTo(center.x, mouthY + r * 0.35f, center.x + r * 0.3f, mouthY)
        }
        Expression.PREOCUPADO -> {
            mouthPath.moveTo(center.x - r * 0.25f, mouthY + r * 0.15f)
            mouthPath.quadraticBezierTo(center.x, mouthY - r * 0.1f, center.x + r * 0.25f, mouthY + r * 0.15f)
        }
        Expression.CONFUNDIDO, Expression.PENSATIVO -> {
            mouthPath.moveTo(center.x - r * 0.2f, mouthY)
            mouthPath.lineTo(center.x + r * 0.25f, mouthY - r * 0.05f)
        }
        Expression.SORPRENDIDO -> {
            drawCircle(Color(0xFF2B241C), radius = r * 0.12f, center = Offset(center.x, mouthY))
            return
        }
        Expression.CONCENTRADO -> {
            mouthPath.moveTo(center.x - r * 0.22f, mouthY)
            mouthPath.lineTo(center.x + r * 0.22f, mouthY)
        }
    }
    drawPath(mouthPath, color = Color(0xFF2B241C), style = Stroke(width = r * 0.08f))
}
