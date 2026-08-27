package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.domain.logic.BadgeEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AulaChore(val id: String, val label: String)

/** Banco de tareas de cuidado del aula; cada visita sortea un subconjunto distinto. */
private val CHORE_POOL = listOf(
    AulaChore("chore_riego", "Regar las plantas del rincon verde"),
    AulaChore("chore_papelera", "Sacar la papelera y vaciarla"),
    AulaChore("chore_estante", "Ordenar los libros del estante"),
    AulaChore("chore_ventilar", "Ventilar el aula abriendo las ventanas"),
    AulaChore("chore_pizarron", "Limpiar el pizarron"),
    AulaChore("chore_suelo", "Recoger los papeles del suelo"),
    AulaChore("chore_materiales", "Guardar los materiales sueltos"),
    AulaChore("chore_sillas", "Enderezar las sillas y mesas")
)

data class CuidadoAulaUiState(
    val chores: List<AulaChore> = CHORE_POOL.shuffled().take(5),
    val chosenChoreIds: Set<String> = emptySet(),
    val confirmationMessage: String? = null,
    val validationMessage: String? = null
)

/**
 * Modulo de "Cuidado del aula": se abre al tocar las plantas o la papelera
 * en la escena (antes no llevaban a ninguna pantalla). Es una lista de
 * tareas de cuidado; el usuario elige cuales va a hacer hoy y confirma.
 */
class CuidadoAulaViewModel(
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CuidadoAulaUiState())
    val uiState: StateFlow<CuidadoAulaUiState> = _uiState.asStateFlow()

    private var careCompletedCount = 0

    fun toggle(chore: AulaChore) {
        val state = _uiState.value
        val newChosen = if (chore.id in state.chosenChoreIds) state.chosenChoreIds - chore.id else state.chosenChoreIds + chore.id
        _uiState.value = state.copy(chosenChoreIds = newChosen)
    }

    fun confirmCare() {
        val state = _uiState.value
        if (state.chosenChoreIds.isEmpty()) {
            _uiState.value = state.copy(validationMessage = "Elige al menos una tarea de cuidado del aula antes de confirmar.")
            return
        }
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val allChosen = state.chosenChoreIds.size == state.chores.size
            val xp = state.chosenChoreIds.size * 4 + if (allChosen) 10 else 0
            careCompletedCount++
            progressRepository.recordInteractionAndAwardXp(
                kind = "AULA_CARE", referenceId = "cuidado_aula",
                xpAwarded = xp, wasSuccessful = true, nowEpochMs = now
            )
            val newlyEarned = BadgeEngine.evaluateNewlyEarned(BadgeEngine.UserStats(aulaCareCompleted = careCompletedCount), emptySet())
            if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)

            val message = if (allChosen) "¡Aula impecable! +$xp XP" else "¡Gracias por cuidar el aula! +$xp XP"
            _uiState.value = _uiState.value.copy(confirmationMessage = message)
        }
    }

    fun consumeConfirmationMessage() {
        _uiState.value = _uiState.value.copy(confirmationMessage = null)
    }

    fun consumeValidationMessage() {
        _uiState.value = _uiState.value.copy(validationMessage = null)
    }

    class Factory(
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CuidadoAulaViewModel(progressRepository, badgeRepository) as T
    }
}
