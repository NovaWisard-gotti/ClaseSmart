package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Actividad escolar general (lectura, ciencias, arte...) usada en varios modulos. */
@Entity(tableName = "class_activity")
data class ClassActivityEntity(
    @PrimaryKey val activityId: String,
    val title: String,
    val subject: String,       // LECTURA, CIENCIAS, ARTE, MATEMATICAS, TRABAJO_GRUPAL, LIMPIEZA, EXPOSICION, RECREO
    val durationMinutes: Int,
    val minAgeBand: String = "8-9",
    val requiredMaterialIds: String = "" // ids separados por coma
)

@Entity(
    tableName = "activity_step",
    foreignKeys = [
        ForeignKey(
            entity = ClassActivityEntity::class,
            parentColumns = ["activityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("activityId")]
)
data class ActivityStepEntity(
    @PrimaryKey(autoGenerate = true) val stepId: Long = 0,
    val activityId: String,
    val orderIndex: Int,
    val instruction: String
)

@Entity(
    tableName = "activity_attempt",
    foreignKeys = [
        ForeignKey(
            entity = ClassActivityEntity::class,
            parentColumns = ["activityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("activityId")]
)
data class ActivityAttemptEntity(
    @PrimaryKey(autoGenerate = true) val attemptId: Long = 0,
    val activityId: String,
    val wasSuccessful: Boolean,
    val timestampEpochMs: Long,
    val notes: String = ""
)
