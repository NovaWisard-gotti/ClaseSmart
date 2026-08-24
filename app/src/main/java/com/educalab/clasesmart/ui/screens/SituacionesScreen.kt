package com.educalab.clasesmart.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.Expression
import com.educalab.clasesmart.ui.scene.CharacterSprite
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.SituacionesViewModel

/**
 * Modulo 5 / 9 - "Situaciones del aula" y "Retos de convivencia".
 * NO se presenta como un cuestionario de opcion multiple con
 * Correcto/Incorrecto: cada opcion es una accion dentro de la escena y
 * la consecuencia se explica siempre en una frase educativa.
 */
@Composable
fun SituacionesScreen(viewModel: SituacionesViewModel, onExit: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val situation = state.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(C.ParedCrema)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButtonBack(onExit)
            Spacer(Modifier.width(8.dp))
            Text("Situaciones del aula", style = MaterialTheme.typography.titleLarge, color = C.TextoOscuro, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator(color = C.PizarraVerde)
            return
        }
        if (situation == null) {
            Text("Ya resolviste todas las situaciones de esta categoria por ahora.", color = C.TextoSuave)
            return
        }

        // "Escenario": tarjeta tipo papel con los personajes involucrados, no un dialogo modal generico.
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = C.PapelBeige,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Row {
                    CharacterSprite(SkinA, C.MarcoMaderaOscuro, C.AcentoAzulCielo, Expression.PREOCUPADO, sizeDp = 72)
                    Spacer(Modifier.width(4.dp))
                    CharacterSprite(SkinB, C.TextoOscuro, C.TizaRosa, Expression.CONFUNDIDO, sizeDp = 72)
                }
                Spacer(Modifier.height(12.dp))
                Text(situation.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
                Spacer(Modifier.height(6.dp))
                Text(situation.sceneDescription, style = MaterialTheme.typography.bodyMedium, color = C.TextoSuave)
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("¿Que se puede hacer?", style = MaterialTheme.typography.titleMedium, color = C.TextoOscuro, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(10.dp))

        situation.options.forEach { option ->
            OptionChip(label = option.actionLabel, enabled = state.lastOutcome == null) {
                viewModel.resolve(option.optionId)
            }
            Spacer(Modifier.height(10.dp))
        }

        AnimatedVisibility(
            visible = state.lastOutcome != null,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 3 }
        ) {
            state.lastOutcome?.let { outcome ->
                Spacer(Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = qualityColor(outcome.qualityLevel),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Lo que paso:", fontWeight = FontWeight.Bold, color = C.TextoOscuro)
                        Spacer(Modifier.height(4.dp))
                        Text(outcome.consequenceText, color = C.TextoOscuro)
                        Spacer(Modifier.height(10.dp))
                        Text("+${outcome.xpAwarded} XP", color = C.TextoOscuro, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        TextButton(onClick = { viewModel.nextSituation() }) { Text("Siguiente situacion") }
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionChip(label: String, enabled: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (enabled) C.TizaBlanca else C.ParedCremaSombra,
        tonalElevation = 2.dp,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(C.AcentoNaranja)
            )
            Spacer(Modifier.width(10.dp))
            Text(label, color = C.TextoOscuro, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun IconButtonBack(onExit: () -> Unit) {
    Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
        Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
    }
}

private fun qualityColor(level: Int): Color = when (level) {
    2 -> C.AcentoVerdeManzana
    1 -> C.TizaAmarilla
    else -> C.TizaRosa
}

private val SkinA = Color(0xFFE8B98A)
private val SkinB = Color(0xFFC98A5B)
