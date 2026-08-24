package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Gran Proyecto del Aula (Modulo 10), ej. "Feria de Ideas". */
@Entity(tableName = "project")
data class ProjectEntity(
    @PrimaryKey val projectId: String,
    val title: String,
    val description: String,
    val minAgeBand: String = "10-12"
)

@Entity(
    tableName = "project_task",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["projectId"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class ProjectTaskEntity(
    @PrimaryKey val taskId: String,
    val projectId: String,
    val orderIndex: Int,
    val taskType: String, // DECIDIR, MATERIALES, EQUIPO, ESPACIO, TIEMPO, IMPREVISTO, PRESENTAR
    val label: String
)

@Entity(
    tableName = "project_progress",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["projectId"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class ProjectProgressEntity(
    @PrimaryKey val projectId: String,
    val completedTaskIds: String = "", // ids separados por coma
    val visualState: String = "INICIAL", // INICIAL, EN_PROGRESO, CASI_LISTO, COMPLETADO
    val lastUpdatedEpochMs: Long = 0L
)
