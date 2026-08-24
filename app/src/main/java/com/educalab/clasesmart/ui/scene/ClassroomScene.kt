package com.educalab.clasesmart.ui.scene

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.ClassroomObject
import com.educalab.clasesmart.domain.model.ClassroomObjectType
import com.educalab.clasesmart.domain.model.Expression
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C

/**
 * EL AULA. Pantalla principal de ClaseSmart (Regla "Home - regla especial":
 * NO existe una Home tradicional de bienvenida+botones; esta escena ES la
 * pantalla de inicio). La navegacion ocurre tocando objetos dentro de la
 * escena, no a traves de una lista ni de un BottomNavigation.
 */
@Composable
fun ClassroomScene(
    objects: List<ClassroomObject>,
    aulaLevel: Int,
    totalXp: Int,
    onObjectTap: (ClassroomObject) -> Unit,
    onBlockedTap: (ClassroomObject) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        ClassroomBackground(modifier = Modifier.fillMaxSize())

        // Personajes idle en la escena, dando sensacion de vida al espacio.
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp),
            horizontalArrangement = Arrangement.spacedBy((-8).dp)
        ) {
            CharacterSprite(SKIN_TONE, C.MarcoMaderaOscuro, C.AcentoAzulCielo, Expression.FELIZ, sizeDp = 64)
            CharacterSprite(SKIN_TONE_2, C.TextoOscuro, C.AcentoNaranja, Expression.COLABORANDO, sizeDp = 64)
        }

        objects.forEach { obj ->
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val sizeDp = objectSizeFor(obj.type)
                Box(
                    modifier = Modifier
                        .offset(
                            x = (maxWidth.value * obj.zoneX - sizeDp.value / 2).dp,
                            y = (maxHeight.value * obj.zoneY - sizeDp.value / 2).dp
                        )
                ) {
                    ClassroomObjectSlot(
                        state = obj.state,
                        onClick = {
                            if (obj.state.name == "BLOQUEADO") onBlockedTap(obj) else onObjectTap(obj)
                        }
                    ) {
                        ObjectVisualFor(obj.type, sizeDp)
                    }
                }
            }
        }

        // HUD minimo, integrado como "panel de corcho" en la esquina, NO como dashboard central.
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(14.dp)),
            color = C.MarcoMadera,
            tonalElevation = 4.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Nivel $aulaLevel", color = C.TizaBlanca, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.width(8.dp))
                Text("$totalXp XP", color = C.TizaAmarilla, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

private fun objectSizeFor(type: ClassroomObjectType) = when (type) {
    ClassroomObjectType.PIZARRA -> 150.dp
    ClassroomObjectType.RELOJ -> 64.dp
    ClassroomObjectType.ESTANTE -> 110.dp
    ClassroomObjectType.PUPITRES -> 80.dp
    ClassroomObjectType.BIBLIOTECA -> 130.dp
    ClassroomObjectType.MOCHILA -> 70.dp
    ClassroomObjectType.PUERTA -> 90.dp
    ClassroomObjectType.CARTEL -> 70.dp
    ClassroomObjectType.PLANTA -> 60.dp
    ClassroomObjectType.PAPELERA -> 44.dp
}

@Composable
private fun ObjectVisualFor(type: ClassroomObjectType, sizeDp: androidx.compose.ui.unit.Dp) {
    val mod = Modifier.size(sizeDp)
    when (type) {
        ClassroomObjectType.PIZARRA -> PizarraVisual(mod)
        ClassroomObjectType.RELOJ -> RelojVisual(mod)
        ClassroomObjectType.ESTANTE -> EstanteVisual(mod)
        ClassroomObjectType.PUPITRES -> MesaVisual(mod)
        ClassroomObjectType.BIBLIOTECA -> BibliotecaVisual(mod)
        ClassroomObjectType.MOCHILA -> MochilaVisual(mod)
        ClassroomObjectType.PUERTA -> PuertaVisual(mod)
        ClassroomObjectType.CARTEL -> CartelVisual(mod)
        ClassroomObjectType.PLANTA -> PlantaVisual(mod)
        ClassroomObjectType.PAPELERA -> PapeleraVisual(mod)
    }
}

private val SKIN_TONE = androidx.compose.ui.graphics.Color(0xFFE8B98A)
private val SKIN_TONE_2 = androidx.compose.ui.graphics.Color(0xFFC98A5B)
