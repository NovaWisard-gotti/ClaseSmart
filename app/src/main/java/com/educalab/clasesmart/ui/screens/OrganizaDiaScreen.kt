package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.PlannableActivity
import com.educalab.clasesmart.domain.model.TimeSlot
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.OrganizaDiaViewModel

/**
 * Modulo 2 - "Organiza el dia". Linea temporal con reloj/pizarra visual;
 * el nino/a ARRASTRA las actividades a los bloques horarios (no es un
 * formulario ni una lista de seleccion).
 */
@Composable
fun OrganizaDiaScreen(viewModel: OrganizaDiaViewModel, onExit: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var slotBounds by remember { mutableStateOf(mapOf<String, Rect>()) }
    var draggingActivity by remember { mutableStateOf<PlannableActivity?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var dragStartOrigin by remember { mutableStateOf(Offset.Zero) }

    Column(Modifier.fillMaxSize().background(C.ParedCrema).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            Text("Organiza el dia", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
        }

        Spacer(Modifier.height(12.dp))
        Text("Arrastra cada actividad hasta el bloque de tiempo donde encaje.", color = C.TextoSuave, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        // Linea temporal (pizarra horizontal con bloques)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.slots) { slot ->
                val assigned = state.assignments[slot.timeBlockId]
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 130.dp)
                        .onGloballyPositioned { coords ->
                            val pos = coords.positionInRoot()
                            slotBounds = slotBounds + (slot.timeBlockId to Rect(pos, coords.size.toSize()))
                        }
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (slot.isRecess) C.AcentoVerdeManzana.copy(alpha = 0.35f) else C.PapelBeige)
                        .padding(8.dp)
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Text(minutesLabel(slot), style = MaterialTheme.typography.labelLarge, color = C.TextoSuave)
                        Spacer(Modifier.height(4.dp))
                        if (assigned != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = C.TizaAmarilla,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                Box(Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                                    Text(assigned.title, style = MaterialTheme.typography.bodyMedium, color = C.TextoOscuro)
                                }
                            }
                        } else if (slot.isRecess) {
                            Text("Recreo", color = C.TextoSuave, modifier = Modifier.weight(1f))
                        } else {
                            Text("Vacio", color = C.TextoSuave.copy(alpha = 0.6f), modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Actividades pendientes", style = MaterialTheme.typography.titleMedium, color = C.TextoOscuro, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Box(Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.pendingActivities.forEach { activity ->
                    val isDragging = draggingActivity?.activityId == activity.activityId
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = C.AcentoAzulCielo,
                        modifier = Modifier
                            .graphicsLayer {
                                if (isDragging) {
                                    translationX = dragOffset.x
                                    translationY = dragOffset.y
                                    scaleX = 1.08f; scaleY = 1.08f
                                }
                            }
                            .onGloballyPositioned { dragStartOrigin = it.positionInRoot() }
                            .pointerInput(activity.activityId) {
                                detectDragGestures(
                                    onDragStart = { draggingActivity = activity; dragOffset = Offset.Zero },
                                    onDrag = { change, delta -> change.consume(); dragOffset += delta },
                                    onDragEnd = {
                                        val dropPoint = dragStartOrigin + dragOffset
                                        val target = slotBounds.entries.firstOrNull { it.value.contains(dropPoint) }
                                        if (target != null) {
                                            val slot = state.slots.first { it.timeBlockId == target.key }
                                            viewModel.assign(activity, slot)
                                        }
                                        draggingActivity = null
                                        dragOffset = Offset.Zero
                                    },
                                    onDragCancel = { draggingActivity = null; dragOffset = Offset.Zero }
                                )
                            }
                    ) {
                        Column(Modifier.padding(10.dp).width(120.dp)) {
                            Text(activity.title, color = C.TizaBlanca, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text("${activity.durationMinutes} min", color = C.TizaBlanca.copy(alpha = 0.85f), style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }

        state.evaluation?.let {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (it.isPlanValido) C.AcentoVerdeManzana else C.TizaRosa,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(state.consequenceText, modifier = Modifier.padding(12.dp), color = C.TextoOscuro)
            }
        }

        Spacer(Modifier.height(10.dp))
        Button(
            onClick = { viewModel.confirmPlan() },
            colors = ButtonDefaults.buttonColors(containerColor = C.PizarraVerde),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Confirmar horario de hoy", color = C.TizaBlanca) }
    }
}

private fun minutesLabel(slot: TimeSlot): String {
    val h = 8 + slot.startMinute / 60
    val m = slot.startMinute % 60
    return String.format("%02d:%02d", h, m)
}

private fun androidx.compose.ui.unit.IntSize.toSize() = androidx.compose.ui.geometry.Size(width.toFloat(), height.toFloat())
