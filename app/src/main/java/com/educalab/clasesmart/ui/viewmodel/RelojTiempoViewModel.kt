package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.domain.logic.TimeManagementEngine
import com.educalab.clasesmart.domain.model.PlannableActivity
import com.educalab.clasesmart.domain.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Banco de tareas posibles; cada visita al modulo sortea un subconjunto para que no sea siempre el mismo reto. */
private val TASK_POOL = listOf(
    PlannableActivity("t1", "Terminar el resumen", Subject.LECTURA, 10),
    PlannableActivity("t2", "Revisar el experimento", Subject.CIENCIAS, 15),
    PlannableActivity("t3", "Ensayar la exposicion", Subject.EXPOSICION, 12),
    PlannableActivity("t4", "Ordenar el estante", Subject.LIMPIEZA, 8),
    PlannableActivity("t5", "Terminar el dibujo del mural", Subject.ARTE, 14),
    PlannableActivity("t6", "Resolver los problemas de matematicas", Subject.MATEMATICAS, 18),
    PlannableActivity("t7", "Repartir el material del equipo", Subject.TRABAJO_GRUPAL, 6),
    PlannableActivity("t8", "Anotar las conclusiones del experimento", Subject.CIENCIAS, 9),
    PlannableActivity("t9", "Practicar la lectura en voz alta", Subject.LECTURA, 7),
    PlannableActivity("t10", "Guardar los materiales compartidos", Subject.LIMPIEZA, 5)
)

private fun randomBudget() = listOf(25, 30, 35).random()
private fun randomTasks() = TASK_POOL.shuffled().take(4)

data class RelojTiempoUiState(
    val budgetMinutes: Int = 30,
    val availableTasks: List<PlannableActivity> = TASK_POOL.take(4),
    val chosenTasks: List<PlannableActivity> = emptyList(),
    val result: TimeManagementEngine.TimeChallengeResult? = null,
    val confirmationMessage: String? = null,
    val validationMessage: String? = null
)

class RelojTiempoViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RelojTiempoUiState(budgetMinutes = randomBudget(), availableTasks = randomTasks()))
    val uiState: StateFlow<RelojTiempoUiState> = _uiState.asStateFlow()

    fun toggle(task: PlannableActivity) {
        val state = _uiState.value
        val chosen = if (state.chosenTasks.any { it.activityId == task.activityId }) state.chosenTasks - task else state.chosenTasks + task
        val result = TimeManagementEngine.evaluate(state.budgetMinutes, chosen)
        _uiState.value = state.copy(chosenTasks = chosen, result = result)
    }

    fun confirm() {
        val state = _uiState.value
        val result = state.result
        if (result == null || state.chosenTasks.isEmpty()) {
            _uiState.value = state.copy(validationMessage = "Elige al menos una tarea antes de confirmar.")
            return
        }
        viewModelScope.launch {
            val xp = if (result.fits) 12 else 3
            progressRepository.recordInteractionAndAwardXp(
                kind = "TIME_CHALLENGE", referenceId = "reto_tiempo",
                xpAwarded = xp, wasSuccessful = result.fits, nowEpochMs = System.currentTimeMillis()
            )
            val message = if (result.fits) "¡Plan confirmado! +$xp XP" else "Plan confirmado con ajustes pendientes. +$xp XP"
            _uiState.value = _uiState.value.copy(confirmationMessage = message)
        }
    }

    fun consumeConfirmationMessage() {
        _uiState.value = _uiState.value.copy(confirmationMessage = null)
    }

    fun consumeValidationMessage() {
        _uiState.value = _uiState.value.copy(validationMessage = null)
    }

    class Factory(private val progressRepository: ProgressRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = RelojTiempoViewModel(progressRepository) as T
    }
}
