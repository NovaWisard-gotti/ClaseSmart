package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
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
    var activityOrigins by remember { mutableStateOf(mapOf<String, Rect>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.confirmationMessage) {
        val message = state.confirmationMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            viewModel.consumeConfirmationMessage()
            onExit()
        }
    }

    if (showAddDialog) {
        AddActivityDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, minutes ->
                viewModel.addCustomActivity(title, minutes)
                showAddDialog = false
            }
        )
    }

    Scaffold(
        containerColor = C.ParedCrema,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(C.ParedCrema)
                .padding(16.dp)
        ) {
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
                                    onClick = { viewModel.unassign(slot) },
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Actividades pendientes", style = MaterialTheme.typography.titleMedium, color = C.TextoOscuro, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                TextButton(onClick = { showAddDialog = true }) { Text("+ Nueva actividad") }
            }
            Text("Cada actividad que arrastras se guarda automaticamente.", color = C.TextoSuave.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(8.dp))

            Box(Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.pendingActivities.forEach { activity ->
                        val isDragging = draggingActivity?.activityId == activity.activityId
                        // Caja externa SIN transformacion: aqui se registra la posicion real
                        // en pantalla, para que no se contamine con el propio desplazamiento
                        // visual del arrastre (bug: antes se leia la posicion YA trasladada).
                        Box(
                            modifier = Modifier.onGloballyPositioned { coords ->
                                if (!isDragging) {
                                    activityOrigins = activityOrigins + (activity.activityId to Rect(coords.positionInRoot(), coords.size.toSize()))
                                }
                            }
                        ) {
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
                                    .pointerInput(activity.activityId) {
                                        detectDragGestures(
                                            onDragStart = { draggingActivity = activity; dragOffset = Offset.Zero },
                                            onDrag = { change, delta -> change.consume(); dragOffset += delta },
                                            onDragEnd = {
                                                // Centro de la tarjeta en su posicion original + lo arrastrado = centro actual.
                                                val origin = activityOrigins[activity.activityId]
                                                if (origin != null) {
                                                    val dropPoint = Offset(
                                                        origin.left + origin.width / 2f + dragOffset.x,
                                                        origin.top + origin.height / 2f + dragOffset.y
                                                    )
                                                    val target = slotBounds.entries.firstOrNull { it.value.contains(dropPoint) }
                                                    if (target != null) {
                                                        val slot = state.slots.first { it.timeBlockId == target.key }
                                                        viewModel.assign(activity, slot)
                                                    }
                                                }
                                                draggingActivity = null
                                                dragOffset = Offset.Zero
                                            },
                                            onDragCancel = { draggingActivity = null; dragOffset = Offset.Zero }
                                        )
                                    }
                            ) {
                                Column(Modifier.padding(10.dp).widthIn(min = 110.dp, max = 150.dp)) {
                                    Text(
                                        activity.title,
                                        color = C.TizaBlanca,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text("${activity.durationMinutes} min", color = C.TizaBlanca.copy(alpha = 0.85f), style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    }
                    if (state.pendingActivities.isEmpty()) {
                        Text("No quedan actividades pendientes por asignar.", color = C.TextoSuave.copy(alpha = 0.7f), modifier = Modifier.padding(8.dp))
                    }
                }
            }

            if (state.consequenceText.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = state.evaluation?.let { if (it.isPlanValido) C.AcentoVerdeManzana else C.TizaRosa } ?: C.TizaAmarilla,
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
}

@Composable
private fun AddActivityDialog(onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var title by remember { mutableStateOf("") }
    var minutesText by remember { mutableStateOf("20") }
    val minutes = minutesText.toIntOrNull()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva actividad") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nombre de la actividad") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = minutesText,
                    onValueChange = { minutesText = it.filter { c -> c.isDigit() } },
                    label = { Text("Duracion (minutos)") },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank() && minutes != null && minutes > 0,
                onClick = { onConfirm(title.trim(), minutes ?: 0) }
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

private fun minutesLabel(slot: TimeSlot): String {
    val h = 8 + slot.startMinute / 60
    val m = slot.startMinute % 60
    return String.format("%02d:%02d", h, m)
}

private fun androidx.compose.ui.unit.IntSize.toSize() = androidx.compose.ui.geometry.Size(width.toFloat(), height.toFloat())
