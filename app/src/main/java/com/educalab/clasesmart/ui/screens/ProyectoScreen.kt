package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.data.local.entity.ProjectEntity
import com.educalab.clasesmart.domain.model.ProjectTask
import com.educalab.clasesmart.domain.model.ProjectVisualState
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.ProyectoViewModel

/** Modulo 10 - "Gran Proyecto del Aula". Panel fisico que cambia de aspecto segun avanza el proyecto. */
@Composable
fun ProyectoScreen(viewModel: ProyectoViewModel, onExit: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().background(C.ParedCrema).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            state.project?.let { Text(it.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro) }
        }
        Spacer(Modifier.height(8.dp))
        state.project?.let { Text(it.description, color = C.TextoSuave, style = MaterialTheme.typography.bodyMedium) }
        Spacer(Modifier.height(16.dp))

        // El "panel del proyecto" cambia visualmente segun visualState.
        Surface(shape = RoundedCornerShape(18.dp), color = panelColor(state.visualState), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(panelEmoji(state.visualState), style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.width(12.dp))
                Text(panelLabel(state.visualState), color = C.TextoOscuro, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Pasos del proyecto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = C.TextoOscuro)
        Spacer(Modifier.height(8.dp))

        state.project?.tasks?.sortedBy { it.orderIndex }?.forEach { task ->
            TaskRow(task, done = task.taskId in state.completedTaskIds) { viewModel.completeTask(task.taskId) }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TaskRow(task: ProjectTask, done: Boolean, onClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(12.dp), color = if (done) C.AcentoVerdeManzana else C.PapelBeige, onClick = onClick) {
        Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (done) Icon(Icons.Filled.Check, contentDescription = "Completado", tint = C.TextoOscuro)
            Spacer(Modifier.width(8.dp))
            Text(task.label, color = C.TextoOscuro, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

private fun panelColor(state: ProjectVisualState) = when (state) {
    ProjectVisualState.INICIAL -> C.ParedCremaSombra
    ProjectVisualState.EN_PROGRESO -> C.TizaAmarilla
    ProjectVisualState.CASI_LISTO -> C.AcentoAzulCielo
    ProjectVisualState.COMPLETADO -> C.AcentoVerdeManzana
}

private fun panelEmoji(state: ProjectVisualState) = when (state) {
    ProjectVisualState.INICIAL -> "🗂️"
    ProjectVisualState.EN_PROGRESO -> "🛠️"
    ProjectVisualState.CASI_LISTO -> "✨"
    ProjectVisualState.COMPLETADO -> "🎉"
}

private fun panelLabel(state: ProjectVisualState) = when (state) {
    ProjectVisualState.INICIAL -> "El proyecto todavia esta por empezar."
    ProjectVisualState.EN_PROGRESO -> "El proyecto va tomando forma."
    ProjectVisualState.CASI_LISTO -> "Casi listo para presentar."
    ProjectVisualState.COMPLETADO -> "Proyecto completado. ¡Buen trabajo en equipo!"
}
