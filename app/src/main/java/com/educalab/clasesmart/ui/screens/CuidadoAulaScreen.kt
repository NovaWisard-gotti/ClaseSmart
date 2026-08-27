package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.AulaChore
import com.educalab.clasesmart.ui.viewmodel.CuidadoAulaViewModel

/**
 * Modulo "Cuidado del aula": tareas de riego/orden/limpieza. Se abre al
 * tocar las plantas o la papelera en la escena.
 */
@Composable
fun CuidadoAulaScreen(viewModel: CuidadoAulaViewModel, onExit: () -> Unit) {
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
                Text("Cuidado del aula", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
            }
            Spacer(Modifier.height(12.dp))
            Text("Hoy toca cuidar el rincon verde y mantener el aula ordenada. Elige que tareas vas a hacer:", color = C.TextoSuave, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))

            state.chores.forEach { chore ->
                ChoreRow(chore, chosen = chore.id in state.chosenChoreIds) { viewModel.toggle(chore) }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { viewModel.confirmCare() },
                colors = ButtonDefaults.buttonColors(containerColor = C.PizarraVerde),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Confirmar cuidado del aula", color = C.TizaBlanca) }
        }
    }
}

@Composable
private fun ChoreRow(chore: AulaChore, chosen: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (chosen) C.AcentoVerdeManzana else C.PapelBeige,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            if (chosen) {
                Icon(Icons.Filled.Check, contentDescription = "Elegida", tint = C.TextoOscuro, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(chore.label, color = C.TextoOscuro, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
