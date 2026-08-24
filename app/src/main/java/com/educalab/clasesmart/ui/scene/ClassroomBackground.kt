package com.educalab.clasesmart.ui.scene

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C

/** Escenografia de fondo del aula: pared, zocalo y suelo de madera con perspectiva simple. */
@Composable
fun ClassroomBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height
        val floorTopY = h * 0.82f

        // Pared
        drawRect(C.ParedCrema, size = Size(w, floorTopY))
        // Zocalo
        drawRect(C.ParedCremaSombra, topLeft = Offset(0f, floorTopY - h * 0.03f), size = Size(w, h * 0.03f))
        // Suelo con tablones
        drawRect(C.SueloMadera, topLeft = Offset(0f, floorTopY), size = Size(w, h - floorTopY))
        val plankCount = 10
        for (i in 1 until plankCount) {
            val x = w * i / plankCount
            drawLine(C.SueloMaderaOscuro.copy(alpha = 0.35f), Offset(x, floorTopY), Offset(x * 0.94f + w * 0.03f, h), strokeWidth = 2f)
        }
    }
}
