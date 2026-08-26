package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.data.repository.SituationRepository
import com.educalab.clasesmart.domain.logic.BadgeEngine
import com.educalab.clasesmart.domain.logic.DailySituationsEngine
import com.educalab.clasesmart.domain.logic.SituationResolutionEngine
import com.educalab.clasesmart.domain.model.ClassroomSituation
import com.educalab.clasesmart.domain.model.SituationCategory
import com.educalab.clasesmart.domain.model.SituationOutcome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SituacionesUiState(
    val situations: List<ClassroomSituation> = emptyList(),
    val currentIndex: Int = 0,
    val lastOutcome: SituationOutcome? = null,
    val resolvedToday: Int = 0,
    val totalToday: Int = 0,
    val isDayComplete: Boolean = false,
    val isLoading: Boolean = true
) {
    val current: ClassroomSituation? get() = situations.getOrNull(currentIndex)
}

/**
 * Muestra el grupo de situaciones del dia (ver DailySituationsEngine): un
 * mismo conjunto se repite dentro del mismo dia si el usuario reabre la
 * pantalla (retoma en la que dejo pendiente), pero cambia automaticamente
 * al dia siguiente y no repite ninguna situacion antes de varios dias.
 */
class SituacionesViewModel(
    private val situationRepository: SituationRepository,
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SituacionesUiState())
    val uiState: StateFlow<SituacionesUiState> = _uiState.asStateFlow()

    private var highQualityCount = 0
    private var convivenciaResolvedCount = 0

    init {
        viewModelScope.launch {
            val all = situationRepository.getAll()
            val dayIndex = DailySituationsEngine.currentDayIndex()
            val today = DailySituationsEngine.situationsForDay(all, dayIndex)
            val resolvedToday = if (today.isEmpty()) 0 else progressRepository
                .countInteractionsSince("SITUATION_RESOLVED", DailySituationsEngine.startOfDayEpochMs())
                .coerceIn(0, today.size)

            _uiState.value = SituacionesUiState(
                situations = today,
                currentIndex = resolvedToday.coerceAtMost((today.size - 1).coerceAtLeast(0)),
                resolvedToday = resolvedToday,
                totalToday = today.size,
                isDayComplete = today.isEmpty() || resolvedToday >= today.size,
                isLoading = false
            )
        }
    }

    fun resolve(optionId: String) {
        val state = _uiState.value
        val situation = state.current ?: return
        if (state.lastOutcome != null) return
        viewModelScope.launch {
            val outcomes = situationRepository.getOutcomeMap(situation.situationId)
            val outcome = SituationResolutionEngine.resolve(situation, optionId, outcomes)
            if (outcome.qualityLevel == 2) highQualityCount++
            if (situation.category == SituationCategory.CONVIVENCIA && outcome.qualityLevel >= 1) convivenciaResolvedCount++

            val now = System.currentTimeMillis()
            progressRepository.recordInteractionAndAwardXp(
                kind = "SITUATION_RESOLVED",
                referenceId = situation.situationId,
                xpAwarded = outcome.xpAwarded,
                wasSuccessful = outcome.qualityLevel >= 1,
                nowEpochMs = now
            )

            val newlyEarned = BadgeEngine.evaluateNewlyEarned(
                BadgeEngine.UserStats(situationsHighQuality = highQualityCount, convivenciaSituationsResolved = convivenciaResolvedCount),
                emptySet()
            )
            if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)

            val resolvedToday = (state.resolvedToday + 1).coerceAtMost(state.totalToday)
            _uiState.value = state.copy(
                lastOutcome = outcome,
                resolvedToday = resolvedToday,
                isDayComplete = resolvedToday >= state.totalToday
            )
        }
    }

    fun nextSituation() {
        val state = _uiState.value
        if (state.currentIndex >= state.situations.lastIndex) return
        _uiState.value = state.copy(
            currentIndex = state.currentIndex + 1,
            lastOutcome = null
        )
    }

    class Factory(
        private val situationRepository: SituationRepository,
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SituacionesViewModel(situationRepository, progressRepository, badgeRepository) as T
    }
}
