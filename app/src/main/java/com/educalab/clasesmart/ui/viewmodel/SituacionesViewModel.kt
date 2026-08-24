package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.data.repository.SituationRepository
import com.educalab.clasesmart.domain.logic.BadgeEngine
import com.educalab.clasesmart.domain.logic.SituationResolutionEngine
import com.educalab.clasesmart.domain.model.ClassroomSituation
import com.educalab.clasesmart.domain.model.SituationCategory
import com.educalab.clasesmart.domain.model.SituationOutcome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SituacionesUiState(
    val category: SituationCategory = SituationCategory.CONVIVENCIA,
    val situations: List<ClassroomSituation> = emptyList(),
    val currentIndex: Int = 0,
    val lastOutcome: SituationOutcome? = null,
    val resolvedCount: Int = 0,
    val isLoading: Boolean = true
) {
    val current: ClassroomSituation? get() = situations.getOrNull(currentIndex)
}

class SituacionesViewModel(
    private val category: SituationCategory,
    private val situationRepository: SituationRepository,
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SituacionesUiState(category = category))
    val uiState: StateFlow<SituacionesUiState> = _uiState.asStateFlow()

    private var highQualityCount = 0

    init {
        viewModelScope.launch {
            val loaded = situationRepository.getByCategory(category)
            _uiState.value = _uiState.value.copy(situations = loaded, isLoading = false)
        }
    }

    fun resolve(optionId: String) {
        val state = _uiState.value
        val situation = state.current ?: return
        viewModelScope.launch {
            val outcomes = situationRepository.getOutcomeMap(situation.situationId)
            val outcome = SituationResolutionEngine.resolve(situation, optionId, outcomes)
            if (outcome.qualityLevel == 2) highQualityCount++

            val now = System.currentTimeMillis()
            progressRepository.recordInteractionAndAwardXp(
                kind = "SITUATION_RESOLVED",
                referenceId = situation.situationId,
                xpAwarded = outcome.xpAwarded,
                wasSuccessful = outcome.qualityLevel >= 1,
                nowEpochMs = now
            )

            val newlyEarned = BadgeEngine.evaluateNewlyEarned(
                BadgeEngine.UserStats(situationsHighQuality = highQualityCount, convivenciaSituationsResolved = if (category == SituationCategory.CONVIVENCIA) state.resolvedCount + 1 else 0),
                emptySet()
            )
            if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)

            _uiState.value = state.copy(lastOutcome = outcome, resolvedCount = state.resolvedCount + 1)
        }
    }

    fun nextSituation() {
        val state = _uiState.value
        _uiState.value = state.copy(
            currentIndex = (state.currentIndex + 1).coerceAtMost(state.situations.lastIndex.coerceAtLeast(0)),
            lastOutcome = null
        )
    }

    class Factory(
        private val category: SituationCategory,
        private val situationRepository: SituationRepository,
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SituacionesViewModel(category, situationRepository, progressRepository, badgeRepository) as T
    }
}
