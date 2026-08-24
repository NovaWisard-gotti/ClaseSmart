package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.*
import com.educalab.clasesmart.data.local.entity.*
import com.educalab.clasesmart.domain.model.*
import kotlinx.coroutines.flow.first

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val taskDao: ProjectTaskDao,
    private val progressDao: ProjectProgressDao
) {
    suspend fun getAllProjects(): List<SchoolProject> {
        val projects = projectDao.observeAll().first()
        return projects.map { p ->
            val tasks = taskDao.observeTasks(p.projectId).first()
            SchoolProject(
                projectId = p.projectId,
                title = p.title,
                description = p.description,
                tasks = tasks.map { ProjectTask(it.taskId, it.orderIndex, ProjectTaskType.valueOf(it.taskType), it.label) }
            )
        }
    }

    suspend fun markTaskCompleted(projectId: String, taskId: String, totalTasks: Int, nowEpochMs: Long): ProjectProgressState {
        val current = progressDao.observeProgress(projectId).first()
        val completed = (current?.completedTaskIds?.split(",")?.filter { it.isNotBlank() }?.toMutableSet() ?: mutableSetOf())
        completed += taskId
        val ratio = if (totalTasks == 0) 0f else completed.size.toFloat() / totalTasks
        val visualState = when {
            ratio >= 1f -> ProjectVisualState.COMPLETADO
            ratio >= 0.66f -> ProjectVisualState.CASI_LISTO
            ratio > 0f -> ProjectVisualState.EN_PROGRESO
            else -> ProjectVisualState.INICIAL
        }
        progressDao.upsert(
            ProjectProgressEntity(
                projectId = projectId,
                completedTaskIds = completed.joinToString(","),
                visualState = visualState.name,
                lastUpdatedEpochMs = nowEpochMs
            )
        )
        return ProjectProgressState(projectId, completed, visualState)
    }
}
