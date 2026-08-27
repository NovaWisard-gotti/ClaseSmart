package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.Expression
import com.educalab.clasesmart.domain.model.SchoolCharacter
import com.educalab.clasesmart.ui.scene.CharacterSprite
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.EquiposViewModel

/**
 * Modulo 4 - "Equipos en accion". Los pupitres se representan como
 * posiciones de equipo; tocar un personaje lo sienta o lo levanta. NUNCA
 * se compara "quien es mejor": solo se explica como se complementan.
 */
@Composable
fun EquiposScreen(viewModel: EquiposViewModel, onExit: () -> Unit) {
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
                Text("Equipos en accion", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "${state.mission.title} necesita: ${state.mission.requiredSkills.joinToString(", ") { it.name.lowercase() }}.",
                color = C.TextoSuave, style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))

            // Pupitres = equipo elegido
            Text("Pupitres del equipo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = C.TextoOscuro)
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                repeat(4) { index ->
                    val character = state.selected.getOrNull(index)
                    Surface(shape = RoundedCornerShape(14.dp), color = C.PapelBeige, modifier = Modifier.size(72.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            if (character != null) {
                                CharacterSprite(SKIN, C.MarcoMaderaOscuro, C.AcentoAzulCielo, Expression.COLABORANDO, sizeDp = 64)
                            } else {
                                Text("vacio", color = C.TextoSuave.copy(alpha = 0.5f), style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("Companeros disponibles (toca para sentar/levantar)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = C.TextoOscuro)
            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(260.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.allCharacters) { character ->
                    val isSelected = state.selected.any { it.characterId == character.characterId }
                    CharacterPickCard(character, isSelected) { viewModel.toggle(character) }
                }
            }

            state.evaluation?.let { evaluation ->
                Spacer(Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (evaluation.missingSkills.isEmpty() && state.selected.isNotEmpty()) C.AcentoVerdeManzana else C.TizaAmarilla,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Cobertura: ${evaluation.coverageScore}%", fontWeight = FontWeight.Bold, color = C.TextoOscuro)
                        Spacer(Modifier.height(4.dp))
                        Text(evaluation.complementText, color = C.TextoOscuro)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            Button(onClick = { viewModel.confirmTeam() }, colors = ButtonDefaults.buttonColors(containerColor = C.PizarraVerde), modifier = Modifier.fillMaxWidth()) {
                Text("Confirmar equipo", color = C.TizaBlanca)
            }
        }
    }
}

@Composable
private fun CharacterPickCard(character: SchoolCharacter, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) C.TizaAmarilla else C.PapelBeige,
        onClick = onClick
    ) {
        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            CharacterSprite(SKIN, C.MarcoMaderaOscuro, C.AcentoAzulCielo, Expression.FELIZ, sizeDp = 56)
            Text(character.name, style = MaterialTheme.typography.labelLarge, color = C.TextoOscuro, fontWeight = FontWeight.SemiBold)
            Text(character.trait, style = MaterialTheme.typography.labelLarge, color = C.TextoSuave)
        }
    }
}

private val SKIN = Color(0xFFE8B98A)
