package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.ui.scene.BibliotecaVisual
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.BibliotecaViewModel

/**
 * Modulo 7 - "Biblioteca del aula". Clasificar recursos tocando la
 * categoria correcta, con la estanteria ilustrada como fondo. Cada
 * clasificacion otorga XP real; al terminar la ronda se felicita al
 * usuario y se vuelve al aula, igual que el resto de modulos.
 */
@Composable
fun BibliotecaScreen(viewModel: BibliotecaViewModel, onExit: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val current = state.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.completionMessage) {
        val message = state.completionMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            viewModel.consumeCompletionMessage()
            onExit()
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
                Text("Clasificaste todos los recursos: ${state.correctCount} de ${state.resources.size} a la primera.", color = C.TextoOscuro, fontWeight = FontWeight.Bold)
            } else {
                Text("¿Donde va \"${current.name}\"?", style = MaterialTheme.typography.titleMedium, color = C.TextoOscuro, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.categories) { category ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = C.PapelBeige,
                            onClick = { viewModel.classify(category) }
                        ) {
                            Text(category, modifier = Modifier.padding(12.dp), color = C.TextoOscuro)
                        }
                    }
                }
            }

            state.feedback?.let {
                Spacer(Modifier.height(14.dp))
                Surface(shape = RoundedCornerShape(12.dp), color = C.TizaAmarilla, modifier = Modifier.fillMaxWidth()) {
                    Text(it, modifier = Modifier.padding(12.dp), color = C.TextoOscuro)
                }
            }
        }
    }
}
