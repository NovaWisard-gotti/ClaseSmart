package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.CharacterRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.data.repository.TeamRepository
import com.educalab.clasesmart.domain.logic.BadgeEngine
import com.educalab.clasesmart.domain.logic.TeamFormationEngine
import com.educalab.clasesmart.domain.model.SchoolCharacter
import com.educalab.clasesmart.domain.model.Skill
import com.educalab.clasesmart.domain.model.TeamEvaluation
import com.educalab.clasesmart.domain.model.TeamProposal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EquiposUiState(
    val allCharacters: List<SchoolCharacter> = emptyList(),
    val selected: List<SchoolCharacter> = emptyList(),
    val requiredSkills: List<Skill> = listOf(Skill.INVESTIGAR, Skill.OBSERVAR, Skill.EXPLICAR),
    val evaluation: TeamEvaluation? = null,
    val confirmationMessage: String? = null,
    val validationMessage: String? = null,
    val isLoading: Boolean = true
)

class EquiposViewModel(
    private val characterRepository: CharacterRepository,
    private val teamRepository: TeamRepository,
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquiposUiState())
    val uiState: StateFlow<EquiposUiState> = _uiState.asStateFlow()

    private var fullCoverageCount = 0

    init {
        viewModelScope.launch {
            val all = characterRepository.getAllWithSkills()
            _uiState.value = _uiState.value.copy(allCharacters = all, isLoading = false)
        }
    }

    fun toggle(character: SchoolCharacter) {
        val state = _uiState.value
        val newSelected = if (state.selected.any { it.characterId == character.characterId }) {
            state.selected.filterNot { it.characterId == character.characterId }
        } else {
            state.selected + character
        }
        val evaluation = TeamFormationEngine.evaluate(TeamProposal("equipo_actividad", "act_ciencias", newSelected), state.requiredSkills)
        _uiState.value = state.copy(selected = newSelected, evaluation = evaluation)
    }

    fun confirmTeam() {
        val state = _uiState.value
        val evaluation = state.evaluation
        if (evaluation == null || state.selected.isEmpty()) {
            _uiState.value = state.copy(validationMessage = "Toca al menos un companero para formar el equipo antes de confirmar.")
            return
        }
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            teamRepository.saveTeam("equipo_actividad", "act_ciencias", state.selected.map { it.characterId }, evaluation.coverageScore, now)
            val fullCoverage = evaluation.missingSkills.isEmpty() && state.selected.isNotEmpty()
            if (fullCoverage) fullCoverageCount++
            val xp = if (fullCoverage) 16 else 6
            progressRepository.recordInteractionAndAwardXp(
                kind = "TEAM_FORMED", referenceId = "equipo_actividad",
                xpAwarded = xp, wasSuccessful = fullCoverage, nowEpochMs = now
            )
            val newlyEarned = BadgeEngine.evaluateNewlyEarned(BadgeEngine.UserStats(teamsWithFullCoverage = fullCoverageCount), emptySet())
            if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)

            val message = if (fullCoverage) "¡Equipo confirmado! +$xp XP" else "Equipo confirmado, aunque faltan habilidades por cubrir. +$xp XP"
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
        private val characterRepository: CharacterRepository,
        private val teamRepository: TeamRepository,
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            EquiposViewModel(characterRepository, teamRepository, progressRepository, badgeRepository) as T
    }
}
