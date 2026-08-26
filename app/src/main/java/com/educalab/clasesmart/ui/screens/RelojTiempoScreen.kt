package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.ui.scene.RelojVisual
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.RelojTiempoViewModel

/**
 * Modulo 8 - "Reloj y tiempo". Reto de presupuesto de minutos: el nino/a
 * elige tareas y ve, con el reloj visual, si caben en el tiempo real.
 * NOTA DE ALCANCE: modulo mas ligero que Organiza el Dia (sin drag&drop de
 * bloques), documentado en docs/BUILD_REPORT.md.
 */
@Composable
fun RelojTiempoScreen(viewModel: RelojTiempoViewModel, onExit: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.confirmationMessage) {
        val message = state.confirmationMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            viewModel.consumeConfirmationMessage()
            onExit()
        }
    }
    LaunchedEffect(state.validationMessage) {
        val message = state.validationMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            viewModel.consumeValidationMessage()
        }
    }

    Scaffold(containerColor = C.ParedCrema, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .background(C.ParedCrema)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            Text("Reloj y tiempo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
        }
        Spacer(Modifier.height(12.dp))
        Text("Tenemos ${state.budgetMinutes} minutos antes del recreo. ¿Que se puede terminar?", color = C.TextoSuave)
        Spacer(Modifier.height(16.dp))

        val angle = ((state.chosenTasks.sumOf { it.durationMinutes }).coerceAtMost(state.budgetMinutes).toFloat() / state.budgetMinutes) * 360f
        RelojVisual(modifier = Modifier.size(140.dp).align(Alignment.CenterHorizontally), minuteAngleDeg = angle, hourAngleDeg = angle / 3)

        Spacer(Modifier.height(16.dp))
        state.availableTasks.forEach { task ->
            val chosen = state.chosenTasks.any { it.activityId == task.activityId }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (chosen) C.TizaAmarilla else C.PapelBeige,
                onClick = { viewModel.toggle(task) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(task.title, color = C.TextoOscuro, modifier = Modifier.weight(1f))
                    Text("${task.durationMinutes} min", color = C.TextoSuave)
                }
            }
        }

        state.result?.let { result ->
            Spacer(Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(14.dp), color = if (result.fits) C.AcentoVerdeManzana else C.TizaRosa, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    if (result.fits) {
                        Text("¡Cabe todo! Sobran ${result.remainingMinutes} minutos.", color = C.TextoOscuro)
                    } else {
                        Text("No cabe todo. Prueba quitar \"${result.suggestionToRemove?.title}\".", color = C.TextoOscuro)
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Button(onClick = { viewModel.confirm() }, colors = ButtonDefaults.buttonColors(containerColor = C.PizarraVerde), modifier = Modifier.fillMaxWidth()) {
            Text("Confirmar plan de tiempo", color = C.TizaBlanca)
        }
    }
    }
}
