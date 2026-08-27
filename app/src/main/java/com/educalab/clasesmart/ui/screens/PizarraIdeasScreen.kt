package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.IdeaNote
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.NOTE_COLORS
import com.educalab.clasesmart.ui.viewmodel.PizarraIdeasViewModel

/**
 * Modulo 6 - "Pizarra de ideas". Notas libres que se pueden arrastrar,
 * agrupar, editar y borrar sobre una pizarra real. Se guardan en Room: al
 * salir y volver a entrar quedan tal como se dejaron la ultima vez.
 */
@Composable
fun PizarraIdeasScreen(viewModel: PizarraIdeasViewModel, onExit: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<IdeaNote?>(null) }
    var draggingId by remember { mutableStateOf<String?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.savedMessage) {
        val message = state.savedMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            viewModel.consumeSavedMessage()
        }
    }

    if (showAddDialog) {
        NoteEditDialog(
            title = "Nueva idea",
            initialText = "",
            onDismiss = { showAddDialog = false },
            onConfirm = { text, color -> viewModel.addNote(text, color); showAddDialog = false }
        )
    }
    editingNote?.let { note ->
        NoteEditDialog(
            title = "Editar idea",
            initialText = note.text,
            onDismiss = { editingNote = null },
            onConfirm = { text, _ -> viewModel.updateText(note.noteId, text); editingNote = null }
        )
    }

    Scaffold(containerColor = C.ParedCrema, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
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
                Text("Pizarra de ideas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro, modifier = Modifier.weight(1f))
                TextButton(onClick = { showAddDialog = true }) { Text("+ Idea") }
            }
            Text("Arrastra las notas para agrupar ideas parecidas. Toca una nota para editarla.", color = C.TextoSuave)
            Spacer(Modifier.height(12.dp))

            Surface(shape = RoundedCornerShape(20.dp), color = C.PizarraVerde, modifier = Modifier.fillMaxWidth().weight(1f)) {
                Box(Modifier.fillMaxSize()) {
                    state.notes.forEach { note ->
                        val isDragging = draggingId == note.noteId
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colorFor(note.colorTag),
                            modifier = Modifier
                                .graphicsLayer {
                                    val x = note.offsetX + if (isDragging) dragOffset.x else 0f
                                    val y = note.offsetY + if (isDragging) dragOffset.y else 0f
                                    translationX = x
                                    translationY = y
                                }
                                .size(120.dp, 90.dp)
                                .pointerInput(note.noteId) {
                                    detectDragGestures(
                                        onDragStart = { draggingId = note.noteId; dragOffset = Offset.Zero },
                                        onDrag = { change, delta -> change.consume(); dragOffset += delta },
                                        onDragEnd = {
                                            viewModel.updatePosition(note.noteId, note.offsetX + dragOffset.x, note.offsetY + dragOffset.y)
                                            draggingId = null
                                            dragOffset = Offset.Zero
                                        },
                                        onDragCancel = { draggingId = null; dragOffset = Offset.Zero }
                                    )
                                }
                        ) {
                            Column(Modifier.padding(8.dp).fillMaxSize()) {
                                Text(
                                    note.text,
                                    color = C.TextoOscuro,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 3,
                                    modifier = Modifier.weight(1f)
                                )
                                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                    IconButton(onClick = { editingNote = note }, modifier = Modifier.size(22.dp)) {
                                        Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = C.TextoOscuro.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                                    }
                                    Spacer(Modifier.width(6.dp))
                                    IconButton(onClick = { viewModel.deleteNote(note.noteId) }, modifier = Modifier.size(22.dp)) {
                                        Icon(Icons.Filled.Close, contentDescription = "Borrar", tint = C.TextoOscuro.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                    if (state.notes.isEmpty() && !state.isLoading) {
                        Text(
                            "Toca \"+ Idea\" para agregar la primera nota.",
                            color = C.TizaBlanca.copy(alpha = 0.7f),
                            modifier = Modifier.align(Alignment.Center).padding(24.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { viewModel.saveBoard() },
                colors = ButtonDefaults.buttonColors(containerColor = C.PizarraVerde),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar", color = C.TizaBlanca) }
        }
    }
}

@Composable
private fun NoteEditDialog(title: String, initialText: String, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var text by remember { mutableStateOf(initialText) }
    var color by remember { mutableStateOf(NOTE_COLORS.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Idea") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (initialText.isEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NOTE_COLORS.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = colorFor(tag),
                                border = if (color == tag) BorderStroke(2.dp, C.TextoOscuro) else null,
                                onClick = { color = tag }
                            ) {
                                Box(Modifier.size(28.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(enabled = text.isNotBlank(), onClick = { onConfirm(text, color) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

private fun colorFor(tag: String): Color = when (tag) {
    "AZUL" -> C.PapelNotaAzul
    "ROSA" -> C.PapelNotaRosa
    else -> C.PapelNota
}
