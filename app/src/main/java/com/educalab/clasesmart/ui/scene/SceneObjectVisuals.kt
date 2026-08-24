package com.educalab.clasesmart.ui.scene

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C

/**
 * Ilustraciones vectoriales propias de cada objeto del aula, dibujadas con
 * Compose Canvas (Regla: "una mesa debe parecer una mesa... una pizarra
 * debe parecer una pizarra"). Cada composable recibe un `progress` de 0f a
 * 1f para animaciones de apertura/interaccion.
 */

@Composable
fun PizarraVisual(modifier: Modifier = Modifier, tizaGlow: Float = 0f) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        // Marco de madera
        drawRoundRect(C.MarcoMadera, size = Size(w, h), cornerRadius = CornerRadius(w * 0.04f))
        // Superficie verde
        val inset = w * 0.06f
        drawRoundRect(
            color = C.PizarraVerde,
            topLeft = Offset(inset, inset),
            size = Size(w - inset * 2, h - inset * 2),
            cornerRadius = CornerRadius(w * 0.02f)
        )
        // Trazos de tiza (lineas curvas simples que sugieren escritura)
        val chalk = C.TizaBlanca.copy(alpha = 0.85f + tizaGlow * 0.15f)
        val path = Path().apply {
            moveTo(w * 0.18f, h * 0.35f)
            quadraticBezierTo(w * 0.35f, h * 0.22f, w * 0.5f, h * 0.35f)
            quadraticBezierTo(w * 0.65f, h * 0.48f, w * 0.82f, h * 0.35f)
        }
        drawPath(path, chalk, style = Stroke(width = h * 0.035f))
        drawLine(chalk, Offset(w * 0.2f, h * 0.62f), Offset(w * 0.55f, h * 0.62f), strokeWidth = h * 0.03f)
        drawLine(chalk.copy(alpha = 0.6f), Offset(w * 0.2f, h * 0.72f), Offset(w * 0.7f, h * 0.72f), strokeWidth = h * 0.03f)
        // Bandeja de tizas
        drawRoundRect(C.MarcoMaderaOscuro, topLeft = Offset(inset, h - inset * 1.6f), size = Size(w - inset * 2, inset * 0.9f), cornerRadius = CornerRadius(4f))
    }
}

@Composable
fun RelojVisual(modifier: Modifier = Modifier, minuteAngleDeg: Float = 45f, hourAngleDeg: Float = 130f) {
    Canvas(modifier = modifier) {
        val d = minOf(size.width, size.height)
        val center = Offset(size.width / 2, size.height / 2)
        val r = d / 2
        drawCircle(C.MarcoMadera, radius = r, center = center)
        drawCircle(C.TizaBlanca, radius = r * 0.86f, center = center)
        drawCircle(C.TextoOscuro, radius = r * 0.86f, center = center, style = Stroke(width = r * 0.04f))
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val outer = Offset(center.x + (r * 0.75f * kotlin.math.sin(angle)).toFloat(), center.y - (r * 0.75f * kotlin.math.cos(angle)).toFloat())
            val inner = Offset(center.x + (r * 0.65f * kotlin.math.sin(angle)).toFloat(), center.y - (r * 0.65f * kotlin.math.cos(angle)).toFloat())
            drawLine(C.TextoSuave, inner, outer, strokeWidth = r * 0.035f)
        }
        fun handEnd(angleDeg: Float, length: Float): Offset {
            val rad = Math.toRadians(angleDeg.toDouble())
            return Offset(center.x + (length * kotlin.math.sin(rad)).toFloat(), center.y - (length * kotlin.math.cos(rad)).toFloat())
        }
        drawLine(C.TextoOscuro, center, handEnd(hourAngleDeg, r * 0.42f), strokeWidth = r * 0.08f)
        drawLine(C.AcentoNaranja, center, handEnd(minuteAngleDeg, r * 0.62f), strokeWidth = r * 0.05f)
        drawCircle(C.TextoOscuro, radius = r * 0.06f, center = center)
    }
}

@Composable
fun EstanteVisual(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        drawRoundRect(C.MarcoMadera, size = Size(w, h), cornerRadius = CornerRadius(w * 0.03f))
        val shelfCount = 3
        val objColors = listOf(C.AcentoAzulCielo, C.AcentoVerdeManzana, C.TizaRosa, C.AcentoMorado, C.TizaAmarilla)
        for (i in 0 until shelfCount) {
            val shelfY = h * (0.28f + i * 0.28f)
            drawLine(C.MarcoMaderaOscuro, Offset(w * 0.05f, shelfY), Offset(w * 0.95f, shelfY), strokeWidth = h * 0.025f)
            // objetos sobre la balda: cajas/libros de colores
            var x = w * 0.1f
            for (j in 0 until 4) {
                val boxW = w * 0.16f
                val boxH = h * (0.14f + (j % 3) * 0.02f)
                drawRoundRect(
                    objColors[(i * 4 + j) % objColors.size],
                    topLeft = Offset(x, shelfY - boxH),
                    size = Size(boxW * 0.85f, boxH),
                    cornerRadius = CornerRadius(4f)
                )
                x += boxW
            }
        }
    }
}

@Composable
fun MesaVisual(modifier: Modifier = Modifier, colorMesa: Color = C.SueloMadera) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        // Patas
        drawRect(C.MarcoMaderaOscuro, topLeft = Offset(w * 0.12f, h * 0.55f), size = Size(w * 0.08f, h * 0.4f))
        drawRect(C.MarcoMaderaOscuro, topLeft = Offset(w * 0.8f, h * 0.55f), size = Size(w * 0.08f, h * 0.4f))
        // Tablero
        drawRoundRect(colorMesa, topLeft = Offset(0f, h * 0.42f), size = Size(w, h * 0.2f), cornerRadius = CornerRadius(8f))
        drawRoundRect(colorMesa.copy(alpha = 0.7f), topLeft = Offset(0f, h * 0.56f), size = Size(w, h * 0.06f))
    }
}

@Composable
fun BibliotecaVisual(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        drawRoundRect(C.MarcoMadera, size = Size(w, h), cornerRadius = CornerRadius(w * 0.03f))
        val rowColors = listOf(C.AcentoAzulCielo, C.TizaRosa, C.AcentoVerdeManzana, C.TizaAmarilla, C.AcentoMorado, C.AcentoNaranja)
        for (row in 0 until 3) {
            val shelfY = h * (0.22f + row * 0.26f)
            drawLine(C.MarcoMaderaOscuro, Offset(w * 0.04f, shelfY + h * 0.2f), Offset(w * 0.96f, shelfY + h * 0.2f), strokeWidth = h * 0.02f)
            var x = w * 0.08f
            var i = 0
            while (x < w * 0.9f) {
                val bookW = w * (0.04f + (i % 3) * 0.01f)
                val bookH = h * (0.16f + (i % 4) * 0.015f)
                drawRect(rowColors[(row * 5 + i) % rowColors.size], topLeft = Offset(x, shelfY + h * 0.2f - bookH), size = Size(bookW, bookH))
                x += bookW + w * 0.008f
                i++
            }
        }
    }
}

@Composable
fun MochilaVisual(modifier: Modifier = Modifier, color: Color = C.AcentoNaranja) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        drawRoundRect(color, topLeft = Offset(w * 0.18f, h * 0.25f), size = Size(w * 0.64f, h * 0.65f), cornerRadius = CornerRadius(w * 0.16f))
        drawRoundRect(color.copy(alpha = 0.8f), topLeft = Offset(w * 0.3f, h * 0.1f), size = Size(w * 0.4f, h * 0.25f), cornerRadius = CornerRadius(w * 0.1f))
        drawLine(C.MarcoMaderaOscuro, Offset(w * 0.3f, 0f), Offset(w * 0.3f, h * 0.3f), strokeWidth = w * 0.05f)
        drawLine(C.MarcoMaderaOscuro, Offset(w * 0.7f, 0f), Offset(w * 0.7f, h * 0.3f), strokeWidth = w * 0.05f)
        drawRoundRect(C.ParedCrema, topLeft = Offset(w * 0.32f, h * 0.5f), size = Size(w * 0.36f, h * 0.28f), cornerRadius = CornerRadius(w * 0.05f))
    }
}

@Composable
fun PuertaVisual(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        drawRoundRect(C.MarcoMadera, size = Size(w, h), cornerRadius = CornerRadius(w * 0.06f))
        drawRoundRect(C.MarcoMaderaOscuro, topLeft = Offset(w * 0.1f, h * 0.08f), size = Size(w * 0.8f, h * 0.84f), cornerRadius = CornerRadius(w * 0.05f))
        drawCircle(C.TizaAmarilla, radius = w * 0.05f, center = Offset(w * 0.75f, h * 0.5f))
    }
}

@Composable
fun PlantaVisual(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        drawRoundRect(C.SueloMadera, topLeft = Offset(w * 0.3f, h * 0.7f), size = Size(w * 0.4f, h * 0.25f), cornerRadius = CornerRadius(6f))
        val leafColor = C.AcentoVerdeManzana
        for (i in 0 until 5) {
            val angle = -90.0 + (i - 2) * 22
            val rad = Math.toRadians(angle)
            val tip = Offset(w * 0.5f + (w * 0.28f * kotlin.math.cos(rad)).toFloat(), h * 0.7f + (h * 0.5f * kotlin.math.sin(rad)).toFloat())
            drawLine(leafColor, Offset(w * 0.5f, h * 0.7f), tip, strokeWidth = w * 0.06f)
        }
    }
}

@Composable
fun CartelVisual(modifier: Modifier = Modifier, color: Color = C.TizaAzul) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        drawRoundRect(C.PapelBeige, size = Size(w, h), cornerRadius = CornerRadius(w * 0.08f))
        drawRoundRect(color, topLeft = Offset(w * 0.1f, h * 0.15f), size = Size(w * 0.8f, h * 0.2f), cornerRadius = CornerRadius(4f))
        drawLine(C.TextoSuave.copy(alpha = 0.5f), Offset(w * 0.15f, h * 0.55f), Offset(w * 0.85f, h * 0.55f), strokeWidth = h * 0.02f)
        drawLine(C.TextoSuave.copy(alpha = 0.5f), Offset(w * 0.15f, h * 0.7f), Offset(w * 0.7f, h * 0.7f), strokeWidth = h * 0.02f)
    }
}

@Composable
fun PapeleraVisual(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        val path = Path().apply {
            moveTo(w * 0.28f, h * 0.2f)
            lineTo(w * 0.72f, h * 0.2f)
            lineTo(w * 0.62f, h * 0.95f)
            lineTo(w * 0.38f, h * 0.95f)
            close()
        }
        drawPath(path, C.AcentoAzulCielo)
        drawRect(C.MarcoMaderaOscuro, topLeft = Offset(w * 0.22f, h * 0.12f), size = Size(w * 0.56f, h * 0.08f))
    }
}
