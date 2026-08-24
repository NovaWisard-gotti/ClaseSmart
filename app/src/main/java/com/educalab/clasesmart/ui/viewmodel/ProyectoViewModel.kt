package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.data.repository.ProjectRepository
import com.educalab.clasesmart.domain.logic.BadgeEngine
import com.educalab.clasesmart.domain.model.ProjectVisualState
import com.educalab.clasesmart.domain.model.SchoolProject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProyectoUiState(
    val project: SchoolProject? = null,
    val completedTaskIds: Set<String> = emptySet(),
    val visualState: ProjectVisualState = ProjectVisualState.INICIAL,
    val isLoading: Boolean = true
)

class ProyectoViewModel(
    private val projectId: String,
    private val projectRepository: ProjectRepository,
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProyectoUiState())
    val uiState: StateFlow<ProyectoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val project = projectRepository.getAllProjects().first { it.projectId == projectId }
            _uiState.value = _uiState.value.copy(project = project, isLoading = false)
        }
    }

    fun completeTask(taskId: String) {
        val state = _uiState.value
        val project = state.project ?: return
        if (taskId in state.completedTaskIds) return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val progress = projectRepository.markTaskCompleted(project.projectId, taskId, project.tasks.size, now)
            _uiState.value = state.copy(completedTaskIds = progress.completedTaskIds, visualState = progress.visualState)

            if (progress.visualState == ProjectVisualState.COMPLETADO) {
                progressRepository.recordInteractionAndAwardXp(
                    kind = "PROJECT_COMPLETED", referenceId = project.projectId, xpAwarded = 40, wasSuccessful = true, nowEpochMs = now
                )
                val newlyEarned = BadgeEngine.evaluateNewlyEarned(BadgeEngine.UserStats(projectsCompleted = 1), emptySet())
                if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)
            } else {
                progressRepository.recordInteractionAndAwardXp(
                    kind = "PROJECT_TASK", referenceId = taskId, xpAwarded = 5, wasSuccessful = true, nowEpochMs = now
                )
            }
        }
    }

    class Factory(
        private val projectId: String,
        private val projectRepository: ProjectRepository,
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ProyectoViewModel(projectId, projectRepository, progressRepository, badgeRepository) as T
    }
}
