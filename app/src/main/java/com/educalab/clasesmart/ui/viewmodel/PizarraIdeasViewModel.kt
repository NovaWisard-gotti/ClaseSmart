package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.IdeaBoardRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.domain.logic.BadgeEngine
import com.educalab.clasesmart.domain.model.IdeaNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

val NOTE_COLORS = listOf("AMARILLO", "AZUL", "ROSA")

data class PizarraIdeasUiState(
    val notes: List<IdeaNote> = emptyList(),
    val isLoading: Boolean = true,
    val savedMessage: String? = null
)

/**
 * Modulo 6 - "Pizarra de ideas". Antes las notas eran solo de sesion (se
 * perdian al salir); ahora se guardan en Room, con edicion y borrado, y un
 * boton "Guardar" que ademas otorga XP y confirma visualmente.
 */
class PizarraIdeasViewModel(
    private val repository: IdeaBoardRepository,
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PizarraIdeasUiState())
    val uiState: StateFlow<PizarraIdeasUiState> = _uiState.asStateFlow()

    private var boardsSavedCount = 0

    init {
        viewModelScope.launch {
            repository.observeNotes().collect { notes ->
                _uiState.value = _uiState.value.copy(notes = notes, isLoading = false)
            }
        }
    }

    fun addNote(text: String, colorTag: String) {
        val cleanText = text.trim()
        if (cleanText.isEmpty()) return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.saveNote(
                IdeaNote(
                    noteId = "note_${now}",
                    text = cleanText,
                    colorTag = colorTag,
                    offsetX = (20..180).random().toFloat(),
                    offsetY = (20..180).random().toFloat(),
                    createdAtEpochMs = now
                )
            )
        }
    }

    fun updateText(noteId: String, newText: String) {
        val cleanText = newText.trim()
        if (cleanText.isEmpty()) return
        val note = _uiState.value.notes.find { it.noteId == noteId } ?: return
        viewModelScope.launch { repository.saveNote(note.copy(text = cleanText)) }
    }

    fun updatePosition(noteId: String, offsetX: Float, offsetY: Float) {
        val note = _uiState.value.notes.find { it.noteId == noteId } ?: return
        viewModelScope.launch { repository.saveNote(note.copy(offsetX = offsetX, offsetY = offsetY)) }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch { repository.deleteNote(noteId) }
    }

    fun saveBoard() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            boardsSavedCount++
            progressRepository.recordInteractionAndAwardXp(
                kind = "IDEAS_BOARD_SAVED", referenceId = "pizarra_ideas", xpAwarded = 8, wasSuccessful = true, nowEpochMs = now
            )
            val newlyEarned = BadgeEngine.evaluateNewlyEarned(BadgeEngine.UserStats(ideasBoardsCreated = boardsSavedCount), emptySet())
            if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)
            _uiState.value = _uiState.value.copy(savedMessage = "¡Pizarra guardada! +8 XP")
        }
    }

    fun consumeSavedMessage() {
        _uiState.value = _uiState.value.copy(savedMessage = null)
    }

    class Factory(
        private val repository: IdeaBoardRepository,
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PizarraIdeasViewModel(repository, progressRepository, badgeRepository) as T
    }
}
