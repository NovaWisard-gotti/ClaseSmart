package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C

private data class IdeaNote(val id: Int, val text: String, val color: androidx.compose.ui.graphics.Color, var offset: Offset)

/**
 * Modulo 6 - "Pizarra de ideas". Notas libres que se pueden arrastrar y
 * agrupar sobre una pizarra real, no una lista de tarjetas.
 * NOTA DE ALCANCE: en esta entrega v1.0.0 las notas son de sesion (no se
 * persisten en Room todavia); ver docs/BUILD_REPORT.md.
 */
@Composable
fun PizarraIdeasScreen(onExit: () -> Unit) {
    val notes = remember {
        mutableStateListOf(
            IdeaNote(1, "Feria de ciencias", C.PapelNota, Offset(40f, 60f)),
            IdeaNote(2, "Mural del aula", C.PapelNotaAzul, Offset(220f, 100f)),
            IdeaNote(3, "Repartir tareas", C.PapelNotaRosa, Offset(80f, 260f)),
            IdeaNote(4, "Elegir fecha", C.PapelNota, Offset(240f, 300f))
        )
    }

    Column(Modifier.fillMaxSize().background(C.ParedCrema).padding(16.dp)) {
        Row {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("Pizarra de ideas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
        Text("Arrastra las notas para agrupar ideas parecidas.", color = C.TextoSuave)
        Spacer(Modifier.height(12.dp))

        Surface(shape = RoundedCornerShape(20.dp), color = C.PizarraVerde, modifier = Modifier.fillMaxWidth().weight(1f)) {
            Box(Modifier.fillMaxSize()) {
                notes.forEach { note ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = note.color,
                        modifier = Modifier
                            .graphicsLayer { translationX = note.offset.x; translationY = note.offset.y }
                            .size(120.dp, 90.dp)
                            .pointerInput(note.id) {
                                detectDragGestures { change, delta ->
                                    change.consume()
                                    val idx = notes.indexOfFirst { it.id == note.id }
                                    if (idx >= 0) notes[idx] = notes[idx].copy(offset = notes[idx].offset + delta)
                                }
                            }
                    ) {
                        Box(Modifier.padding(10.dp)) {
                            Text(note.text, color = C.TextoOscuro, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
