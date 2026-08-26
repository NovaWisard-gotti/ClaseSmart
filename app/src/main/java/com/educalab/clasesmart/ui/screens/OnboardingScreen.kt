package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.Expression
import com.educalab.clasesmart.ui.scene.CharacterSprite
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.AVAILABLE_AVATARS
import com.educalab.clasesmart.ui.viewmodel.ProfileViewModel

/**
 * Onboarding de 4 pantallas maximo (Regla 16). Se muestra UNA sola vez;
 * despues se entra directo al aula. Nunca pide nombre real ni datos
 * personales (Regla 27).
 */
@Composable
fun OnboardingScreen(viewModel: ProfileViewModel, onFinished: () -> Unit) {
    var page by remember { mutableStateOf(0) }
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().background(C.PizarraVerde).padding(24.dp)) {
        Spacer(Modifier.weight(1f))
        when (page) {
            0 -> OnboardingIntro()
            1 -> OnboardingAgeBand(state.ageBand, viewModel::setAgeBand)
            2 -> OnboardingAvatarPicker(state.avatarId, viewModel::setAvatar, state.alias, viewModel::setAlias)
            3 -> OnboardingPrivacy()
        }
        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.align(Alignment.CenterHorizontally)) {
            repeat(4) { i ->
                Box(
                    Modifier
                        .size(8.dp)
                        .background(if (i == page) C.TizaAmarilla else C.TizaBlanca.copy(alpha = 0.3f), androidx.compose.foundation.shape.CircleShape)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (page < 3) page++ else viewModel.completeOnboarding(onFinished)
            },
            colors = ButtonDefaults.buttonColors(containerColor = C.TizaAmarilla),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (page < 3) "Continuar" else "Entrar al aula", color = C.TextoOscuro, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun OnboardingIntro() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CharacterSprite(Color(0xFFE8B98A), C.MarcoMaderaOscuro, C.AcentoNaranja, Expression.FELIZ, sizeDp = 140)
        Spacer(Modifier.height(16.dp))
        Text("Bienvenido a ClaseSmart", style = MaterialTheme.typography.headlineMedium, color = C.TizaBlanca, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Aqui vas a organizar, planificar y resolver situaciones dentro de tu propia aula.", color = C.ParedCrema, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun OnboardingAgeBand(selected: String, onSelect: (String) -> Unit) {
    Column {
        Text("¿Con que grupo te sientes mas comodo?", style = MaterialTheme.typography.titleLarge, color = C.TizaBlanca, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        listOf("8-9" to "Retos mas guiados", "10-12" to "Retos mas abiertos").forEach { (band, desc) ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (selected == band) C.TizaAmarilla else C.MarcoMadera,
                onClick = { onSelect(band) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(band + " años", fontWeight = FontWeight.Bold, color = C.TextoOscuro)
                    Text(desc, color = C.TextoOscuro.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
private fun OnboardingAvatarPicker(selected: String, onSelect: (String) -> Unit, alias: String, onAlias: (String) -> Unit) {
    Column {
        Text("Elige tu avatar y un alias (no tu nombre real)", style = MaterialTheme.typography.titleLarge, color = C.TizaBlanca, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(260.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(AVAILABLE_AVATARS) { idx, avatarId ->
                val isSelected = selected == avatarId
                Box {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) C.TizaAmarilla else C.MarcoMadera,
                        border = if (isSelected) BorderStroke(2.dp, C.TizaBlanca) else null,
                        onClick = { onSelect(avatarId) },
                        modifier = Modifier.aspectRatio(1f)
                    ) {
                        Box(Modifier.fillMaxSize().padding(6.dp), contentAlignment = Alignment.Center) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(avatarBackdrop(idx)),
                                contentAlignment = Alignment.Center
                            ) {
                                CharacterSprite(avatarSkin(idx), avatarHair(idx), avatarShirt(idx), Expression.FELIZ, sizeDp = 48)
                            }
                        }
                    }
                    if (isSelected) {
                        Box(
                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(2.dp)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(C.EstadoCompletado),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = "Seleccionado", tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = alias, onValueChange = onAlias,
            label = { Text("Tu alias") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = C.TizaBlanca,
                unfocusedTextColor = C.TizaBlanca,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = C.TizaAmarilla,
                unfocusedBorderColor = C.TizaBlanca.copy(alpha = 0.6f),
                cursorColor = C.TizaAmarilla,
                focusedLabelColor = C.TizaAmarilla,
                unfocusedLabelColor = C.ParedCrema
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun OnboardingPrivacy() {
    Column {
        Text("Tu privacidad", style = MaterialTheme.typography.titleLarge, color = C.TizaBlanca, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(
            "ClaseSmart funciona sin internet. No pedimos tu nombre real, email ni ubicacion. " +
                "Todo tu progreso se guarda solo en este dispositivo.",
            color = C.ParedCrema, style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun avatarSkin(i: Int) = listOf(Color(0xFFE8B98A), Color(0xFFC98A5B), Color(0xFFF2D0A9), Color(0xFF8D5B3E))[i % 4]
private fun avatarHair(i: Int) = listOf(C.TextoOscuro, C.MarcoMaderaOscuro, Color(0xFFD9A441), Color(0xFF6B4A2F))[i % 4]
private fun avatarShirt(i: Int) = listOf(C.AcentoAzulCielo, C.AcentoNaranja, C.AcentoVerdeManzana, C.AcentoMorado, C.TizaRosa, C.TizaAmarilla)[i % 6]
private fun avatarBackdrop(i: Int) = listOf(C.PapelNota, C.PapelNotaAzul, C.PapelNotaRosa, C.PapelBeige)[i % 4]
