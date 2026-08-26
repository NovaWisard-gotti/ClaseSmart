package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.local.dao.ClassActivityDao
import com.educalab.clasesmart.data.local.dao.TimeBlockDao
import com.educalab.clasesmart.data.local.entity.ClassActivityEntity
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.data.repository.ScheduleRepository
import com.educalab.clasesmart.data.repository.toDomain
import com.educalab.clasesmart.domain.logic.BadgeEngine
import com.educalab.clasesmart.domain.logic.ConsequenceEngine
import com.educalab.clasesmart.domain.logic.OrganizationEngine
import com.educalab.clasesmart.domain.model.PlannableActivity
import com.educalab.clasesmart.domain.model.ScheduleAssignment
import com.educalab.clasesmart.domain.model.ScheduleEvaluation
import com.educalab.clasesmart.domain.model.Subject
import com.educalab.clasesmart.domain.model.TimeSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class OrganizaDiaUiState(
    val slots: List<TimeSlot> = emptyList(),
    val pendingActivities: List<PlannableActivity> = emptyList(),
    val assignments: Map<String, PlannableActivity> = emptyMap(), // timeBlockId -> activity
    val evaluation: ScheduleEvaluation? = null,
    val consequenceText: String = "",
    val confirmationMessage: String? = null,
    val isLoading: Boolean = true
)

class OrganizaDiaViewModel(
    private val timeBlockDao: TimeBlockDao,
    private val classActivityDao: ClassActivityDao,
    private val scheduleRepository: ScheduleRepository,
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganizaDiaUiState())
    val uiState: StateFlow<OrganizaDiaUiState> = _uiState.asStateFlow()

    private var successfulPlansCount = 0

    init {
        viewModelScope.launch {
            val slots = timeBlockDao.observeAll().first().map { it.toDomain() }.filterNot { it.isRecess }
            val recessSlots = timeBlockDao.observeAll().first().filter { it.isRecess }.map { it.toDomain() }
            val activities = classActivityDao.observeAll().first().map { it.toDomain() }
            _uiState.value = _uiState.value.copy(
                slots = (slots + recessSlots).sortedBy { it.startMinute },
                pendingActivities = activities,
                isLoading = false
            )
        }
    }

    fun assign(activity: PlannableActivity, slot: TimeSlot) {
        val state = _uiState.value
        val newAssignments = state.assignments.toMutableMap()
        newAssignments[slot.timeBlockId] = activity
        evaluate(state.copy(assignments = newAssignments, pendingActivities = state.pendingActivities - activity))
    }

    fun unassign(slot: TimeSlot) {
        val state = _uiState.value
        val activity = state.assignments[slot.timeBlockId] ?: return
        val newAssignments = state.assignments.toMutableMap()
        newAssignments.remove(slot.timeBlockId)
        evaluate(state.copy(assignments = newAssignments, pendingActivities = state.pendingActivities + activity))
    }

    private fun evaluate(state: OrganizaDiaUiState) {
        val assignments = state.assignments.map { (blockId, activity) ->
            ScheduleAssignment(state.slots.first { it.timeBlockId == blockId }, activity)
        }
        val evaluation = OrganizationEngine.evaluate(state.slots, state.assignments.values.toList(), assignments)
        _uiState.value = state.copy(
            evaluation = evaluation,
            consequenceText = ConsequenceEngine.forSchedule(evaluation.issues)
        )
    }

    fun confirmPlan() {
        val state = _uiState.value
        val evaluation = state.evaluation
        if (evaluation == null || evaluation.assignments.isEmpty()) {
            _uiState.value = state.copy(consequenceText = "Arrastra al menos una actividad a un bloque de tiempo antes de confirmar.")
            return
        }
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val xp = if (evaluation.isPlanValido) 20 else 5
            if (evaluation.isPlanValido) successfulPlansCount++
            progressRepository.recordInteractionAndAwardXp(
                kind = "SCHEDULE_SUBMITTED", referenceId = "hoy", xpAwarded = xp,
                wasSuccessful = evaluation.isPlanValido, nowEpochMs = now
            )
            scheduleRepository.saveAssignments("hoy", evaluation.assignments)
            val newlyEarned = BadgeEngine.evaluateNewlyEarned(BadgeEngine.UserStats(schedulesWithoutIssues = successfulPlansCount), emptySet())
            if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)

            val message = if (evaluation.isPlanValido) "¡Horario guardado! +$xp XP" else "Horario guardado, aunque con algunos problemas. +$xp XP"
            _uiState.value = _uiState.value.copy(confirmationMessage = message)
        }
    }

    fun consumeConfirmationMessage() {
        _uiState.value = _uiState.value.copy(confirmationMessage = null)
    }

    fun addCustomActivity(title: String, durationMinutes: Int) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty() || durationMinutes <= 0) return
        viewModelScope.launch {
            val id = "custom_${System.currentTimeMillis()}"
            classActivityDao.insertAll(
                listOf(
                    ClassActivityEntity(
                        activityId = id,
                        title = cleanTitle,
                        subject = Subject.TRABAJO_GRUPAL.name,
                        durationMinutes = durationMinutes
                    )
                )
            )
            val created = classActivityDao.getById(id)?.toDomain() ?: return@launch
            _uiState.value = _uiState.value.copy(pendingActivities = _uiState.value.pendingActivities + created)
        }
    }

    class Factory(
        private val timeBlockDao: TimeBlockDao,
        private val classActivityDao: ClassActivityDao,
        private val scheduleRepository: ScheduleRepository,
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            OrganizaDiaViewModel(timeBlockDao, classActivityDao, scheduleRepository, progressRepository, badgeRepository) as T
    }
}
