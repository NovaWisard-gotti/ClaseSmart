package com.educalab.clasesmart.ui.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.educalab.clasesmart.domain.model.ClassroomObject
import com.educalab.clasesmart.domain.model.ClassroomObjectType
import com.educalab.clasesmart.domain.model.Expression
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import kotlinx.coroutines.launch

/** Altura virtual del lienzo del aula: es mas alta que la pantalla a proposito
 * para poder repartir los 30 objetos sin que se superpongan; se recorre
 * haciendo scroll vertical. */
private val SCENE_HEIGHT = 2200.dp
private val LABEL_BOX_WIDTH = 84.dp
private val LABEL_BLOCK_HEIGHT = 20.dp

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
    onBlockedTap: (ClassroomObject) -> Unit = {},
    modifier: Modifier = Modifier,
    showGuide: Boolean = false,
    onDismissGuide: () -> Unit = {},
    onOpenGuide: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 28.dp)
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(SCENE_HEIGHT)) {
                ClassroomBackground(modifier = Modifier.matchParentSize())

                objects.forEach { obj ->
                    val sizeDp = objectSizeFor(obj.type) * obj.sizeScale
                    val boxWidth = maxOf(sizeDp, LABEL_BOX_WIDTH)
                    Box(
                        modifier = Modifier
                            .width(boxWidth)
                            .offset(
                                x = (maxWidth.value * obj.zoneX - boxWidth.value / 2).dp,
                                y = (maxHeight.value * obj.zoneY - sizeDp.value / 2).dp - LABEL_BLOCK_HEIGHT
                            ),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ObjectLabel(labelFor(obj.type))
                            Spacer(Modifier.height(2.dp))
                            ClassroomObjectSlot(
                                state = obj.state,
                                onClick = {
                                    if (obj.state.name == "BLOQUEADO") {
                                        onBlockedTap(obj)
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "🔒 ${labelFor(obj.type)}: se desbloquea en el nivel ${obj.unlockLevel} (estas en el nivel $aulaLevel). Sigue ganando XP para subir."
                                            )
                                        }
                                    } else onObjectTap(obj)
                                }
                            ) {
                                ObjectVisualFor(obj.type, sizeDp)
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp))

        // Personajes idle, siempre visibles al pie de la pantalla (no se mueven con el scroll).
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp),
            horizontalArrangement = Arrangement.spacedBy((-8).dp)
        ) {
            CharacterSprite(SKIN_TONE, C.MarcoMaderaOscuro, C.AcentoAzulCielo, Expression.FELIZ, sizeDp = 64)
            CharacterSprite(SKIN_TONE_2, C.TextoOscuro, C.AcentoNaranja, Expression.COLABORANDO, sizeDp = 64)
        }

        // HUD minimo, integrado como "panel de corcho" en la esquina, NO como dashboard central.
        Row(
            modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(14.dp)),
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
            Surface(
                shape = CircleShape,
                color = C.MarcoMadera,
                tonalElevation = 4.dp,
                onClick = onOpenGuide
            ) {
                Box(Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.QuestionMark, contentDescription = "Como funciona el aula", tint = C.TizaAmarilla, modifier = Modifier.size(16.dp))
                }
            }
        }

        if (showGuide) {
            AulaGuideOverlay(onDismiss = onDismissGuide)
        }
    }
}

@Composable
private fun AulaGuideOverlay(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = C.ParedCrema,
            modifier = Modifier.padding(28.dp)
        ) {
            Column(Modifier.padding(22.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Bienvenido a tu aula", style = MaterialTheme.typography.titleLarge, color = C.TextoOscuro, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar guia", tint = C.TextoOscuro)
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "Toca los objetos para abrir cada actividad. Los que tienen candado se desbloquean subiendo de nivel.",
                    color = C.TextoSuave, style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(14.dp))
                GuideTip("Pizarra", "Organiza las actividades del dia en bloques de tiempo.")
                GuideTip("Estante", "Prepara los materiales para una mision.")
                GuideTip("Reloj", "Practica cuanto te alcanza el tiempo antes del recreo.")
                GuideTip("Carteles", "Resuelve situaciones de convivencia del aula.")
                Spacer(Modifier.height(18.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = C.TizaAmarilla),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entendido, ¡a explorar!", color = C.TextoOscuro, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun GuideTip(title: String, description: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("•", color = C.AcentoNaranja, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
        Column {
            Text(title, color = C.TextoOscuro, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(description, color = C.TextoSuave, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ObjectLabel(text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = C.MarcoMaderaOscuro.copy(alpha = 0.85f)
    ) {
        Text(
            text,
            color = C.TizaBlanca,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .widthIn(max = LABEL_BOX_WIDTH - 4.dp)
                .padding(horizontal = 5.dp, vertical = 2.dp)
        )
    }
}

private fun labelFor(type: ClassroomObjectType): String = when (type) {
    ClassroomObjectType.PIZARRA -> "Pizarra"
    ClassroomObjectType.RELOJ -> "Reloj"
    ClassroomObjectType.ESTANTE -> "Estante"
    ClassroomObjectType.PUPITRES -> "Pupitres"
    ClassroomObjectType.BIBLIOTECA -> "Biblioteca"
    ClassroomObjectType.MOCHILA -> "Mochila"
    ClassroomObjectType.PUERTA -> "Puerta"
    ClassroomObjectType.CARTEL -> "Cartel"
    ClassroomObjectType.PLANTA -> "Planta"
    ClassroomObjectType.PAPELERA -> "Papelera"
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
