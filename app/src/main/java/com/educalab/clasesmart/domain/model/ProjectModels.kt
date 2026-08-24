package com.educalab.clasesmart.domain.model

enum class ProjectTaskType { DECIDIR, MATERIALES, EQUIPO, ESPACIO, TIEMPO, IMPREVISTO, PRESENTAR }
enum class ProjectVisualState { INICIAL, EN_PROGRESO, CASI_LISTO, COMPLETADO }

data class ProjectTask(val taskId: String, val orderIndex: Int, val type: ProjectTaskType, val label: String)

data class SchoolProject(
    val projectId: String,
    val title: String,
    val description: String,
    val tasks: List<ProjectTask>
)

data class ProjectProgressState(
    val projectId: String,
    val completedTaskIds: Set<String>,
    val visualState: ProjectVisualState
) {
    fun completionRatio(totalTasks: Int): Float =
        if (totalTasks == 0) 0f else completedTaskIds.size.toFloat() / totalTasks
}
