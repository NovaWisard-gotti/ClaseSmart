package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.MaterialRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.domain.logic.BadgeEngine
import com.educalab.clasesmart.domain.logic.ConsequenceEngine
import com.educalab.clasesmart.domain.logic.MaterialAssignmentEngine
import com.educalab.clasesmart.domain.model.MaterialEvaluation
import com.educalab.clasesmart.domain.model.MaterialMission
import com.educalab.clasesmart.domain.model.MaterialZone
import com.educalab.clasesmart.domain.model.SchoolMaterial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MaterialesUiState(
    val mission: MaterialMission,
    val shelfMaterials: List<SchoolMaterial> = emptyList(),
    val tableMaterials: List<SchoolMaterial> = emptyList(),
    val evaluation: MaterialEvaluation? = null,
    val consequenceText: String = "",
    val isLoading: Boolean = true
)

private val EXPERIMENTO_MISSION = MaterialMission("mision_experimento", "Preparar el experimento de observacion", listOf("mat_lupa", "mat_recipiente", "mat_cuaderno_campo"))

class MaterialesViewModel(
    private val materialRepository: MaterialRepository,
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MaterialesUiState(mission = EXPERIMENTO_MISSION))
    val uiState: StateFlow<MaterialesUiState> = _uiState.asStateFlow()

    private var readyMissionsCount = 0

    init {
        viewModelScope.launch {
            val all = materialRepository.getAllWithLocation()
            _uiState.value = _uiState.value.copy(shelfMaterials = all, isLoading = false)
        }
    }

    fun moveToTable(material: SchoolMaterial) {
        val state = _uiState.value
        if (state.tableMaterials.any { it.materialId == material.materialId }) return
        val updated = state.copy(
            shelfMaterials = state.shelfMaterials - material,
            tableMaterials = state.tableMaterials + material
        )
        evaluate(updated)
    }

    fun returnToShelf(material: SchoolMaterial) {
        val state = _uiState.value
        val updated = state.copy(
            tableMaterials = state.tableMaterials - material,
            shelfMaterials = state.shelfMaterials + material
        )
        evaluate(updated)
    }

    private fun evaluate(state: MaterialesUiState) {
        val evaluation = MaterialAssignmentEngine.evaluate(state.mission, state.shelfMaterials + state.tableMaterials, state.tableMaterials)
        _uiState.value = state.copy(evaluation = evaluation, consequenceText = ConsequenceEngine.forMaterials(evaluation.issues))
    }

    fun confirmMission() {
        val evaluation = _uiState.value.evaluation ?: return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (evaluation.isReady) readyMissionsCount++
            progressRepository.recordInteractionAndAwardXp(
                kind = "MATERIAL_MISSION", referenceId = _uiState.value.mission.missionId,
                xpAwarded = if (evaluation.isReady) 18 else 4, wasSuccessful = evaluation.isReady, nowEpochMs = now
            )
            val newlyEarned = BadgeEngine.evaluateNewlyEarned(BadgeEngine.UserStats(materialMissionsReady = readyMissionsCount), emptySet())
            if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)
        }
    }

    class Factory(
        private val materialRepository: MaterialRepository,
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MaterialesViewModel(materialRepository, progressRepository, badgeRepository) as T
    }
}
