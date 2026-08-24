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

data class RelojTiempoUiState(
    val budgetMinutes: Int = 30,
    val availableTasks: List<PlannableActivity> = listOf(
        PlannableActivity("t1", "Terminar el resumen", Subject.LECTURA, 10),
        PlannableActivity("t2", "Revisar el experimento", Subject.CIENCIAS, 15),
        PlannableActivity("t3", "Ensayar la exposicion", Subject.EXPOSICION, 12),
        PlannableActivity("t4", "Ordenar el estante", Subject.LIMPIEZA, 8)
    ),
    val chosenTasks: List<PlannableActivity> = emptyList(),
    val result: TimeManagementEngine.TimeChallengeResult? = null
)

class RelojTiempoViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RelojTiempoUiState())
    val uiState: StateFlow<RelojTiempoUiState> = _uiState.asStateFlow()

    fun toggle(task: PlannableActivity) {
        val state = _uiState.value
        val chosen = if (state.chosenTasks.any { it.activityId == task.activityId }) state.chosenTasks - task else state.chosenTasks + task
        val result = TimeManagementEngine.evaluate(state.budgetMinutes, chosen)
        _uiState.value = state.copy(chosenTasks = chosen, result = result)
    }

    fun confirm() {
        val result = _uiState.value.result ?: return
        viewModelScope.launch {
            progressRepository.recordInteractionAndAwardXp(
                kind = "TIME_CHALLENGE", referenceId = "reto_tiempo",
                xpAwarded = if (result.fits) 12 else 3, wasSuccessful = result.fits, nowEpochMs = System.currentTimeMillis()
            )
        }
    }

    class Factory(private val progressRepository: ProgressRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = RelojTiempoViewModel(progressRepository) as T
    }
}
