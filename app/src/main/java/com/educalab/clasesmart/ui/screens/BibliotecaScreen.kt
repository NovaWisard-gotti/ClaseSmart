package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.ui.scene.BibliotecaVisual
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C

private data class Resource(val id: String, val name: String, val correctCategory: String)
private val CATEGORIES = listOf("Leer", "Investigar", "Crear", "Construir", "Comunicar", "Organizar")
private val RESOURCES = listOf(
    Resource("r1", "Libro de cuentos", "Leer"),
    Resource("r2", "Lupa de observacion", "Investigar"),
    Resource("r3", "Caja de pinturas", "Crear"),
    Resource("r4", "Bloques de madera", "Construir"),
    Resource("r5", "Mapa del aula", "Comunicar"),
    Resource("r6", "Carpeta clasificadora", "Organizar")
)

/**
 * Modulo 7 - "Biblioteca del aula". Clasificar recursos tocando la
 * categoria correcta, con la estanteria ilustrada como fondo.
 * NOTA DE ALCANCE: modulo mas ligero, sin persistencia de progreso propia
 * en esta entrega v1.0.0; ver docs/BUILD_REPORT.md.
 */
@Composable
fun BibliotecaScreen(onExit: () -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var correctCount by remember { mutableStateOf(0) }
    val current = RESOURCES.getOrNull(currentIndex)

    Column(Modifier.fillMaxSize().background(C.ParedCrema).padding(16.dp)) {
        Row {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("Biblioteca del aula", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
        Spacer(Modifier.height(12.dp))
        BibliotecaVisual(modifier = Modifier.fillMaxWidth().height(150.dp))
        Spacer(Modifier.height(16.dp))

        if (current == null) {
            Text("Clasificaste todos los recursos: $correctCount de ${RESOURCES.size} a la primera.", color = C.TextoOscuro, fontWeight = FontWeight.Bold)
        } else {
            Text("¿Donde va \"${current.name}\"?", style = MaterialTheme.typography.titleMedium, color = C.TextoOscuro, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CATEGORIES) { category ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = C.PapelBeige,
                        onClick = {
                            val isCorrect = category == current.correctCategory
                            if (isCorrect) correctCount++
                            feedback = if (isCorrect) "¡Bien clasificado! \"${current.name}\" ayuda a ${category.lowercase()}."
                                       else "Prueba otra categoria: \"${current.name}\" encaja mejor en ${current.correctCategory}."
                            if (isCorrect) currentIndex++
                        }
                    ) {
                        Text(category, modifier = Modifier.padding(12.dp), color = C.TextoOscuro)
                    }
                }
            }
        }

        feedback?.let {
            Spacer(Modifier.height(14.dp))
            Surface(shape = RoundedCornerShape(12.dp), color = C.TizaAmarilla, modifier = Modifier.fillMaxWidth()) {
                Text(it, modifier = Modifier.padding(12.dp), color = C.TextoOscuro)
            }
        }
    }
}
