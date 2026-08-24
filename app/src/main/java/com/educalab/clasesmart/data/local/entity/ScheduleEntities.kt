package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Bloque de tiempo disponible en la jornada (Modulo 2 y 8: reloj/pizarra). */
@Entity(tableName = "time_block")
data class TimeBlockEntity(
    @PrimaryKey val timeBlockId: String,
    val dayLabel: String,       // "Hoy"
    val startMinute: Int,       // minutos desde inicio de jornada (0..480)
    val endMinute: Int,
    val isRecess: Boolean = false
)

/** Actividad asignada a un bloque de tiempo por el nino/a. */
@Entity(
    tableName = "schedule_activity",
    foreignKeys = [
        ForeignKey(
            entity = TimeBlockEntity::class,
            parentColumns = ["timeBlockId"],
            childColumns = ["timeBlockId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ClassActivityEntity::class,
            parentColumns = ["activityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("timeBlockId"), Index("activityId")]
)
data class ScheduleActivityEntity(
    @PrimaryKey(autoGenerate = true) val scheduleRowId: Long = 0,
    val timeBlockId: String,
    val activityId: String,
    val planDate: String // "YYYY-MM-DD" simplificado, jornada del dia actual
)
