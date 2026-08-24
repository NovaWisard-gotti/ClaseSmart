package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Situacion de convivencia/organizacion del aula (Modulo 5 y 9). */
@Entity(tableName = "classroom_situation")
data class ClassroomSituationEntity(
    @PrimaryKey val situationId: String,
    val title: String,
    val sceneDescription: String,
    val category: String, // CONVIVENCIA, ORGANIZACION, TIEMPO, MATERIALES, EQUIPO
    val minAgeBand: String = "8-9",
    val involvedCharacterIds: String = "" // ids separados por coma
)

@Entity(
    tableName = "situation_option",
    foreignKeys = [
        ForeignKey(
            entity = ClassroomSituationEntity::class,
            parentColumns = ["situationId"],
            childColumns = ["situationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("situationId")]
)
data class SituationOptionEntity(
    @PrimaryKey val optionId: String,
    val situationId: String,
    val actionLabel: String,   // "Ayudar", "Repartir el material", "Esperar el turno"
    val qualityLevel: Int      // 0=poco efectiva, 1=aceptable, 2=muy efectiva (NO correcto/incorrecto binario)
)

@Entity(
    tableName = "situation_outcome",
    foreignKeys = [
        ForeignKey(
            entity = SituationOptionEntity::class,
            parentColumns = ["optionId"],
            childColumns = ["optionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("optionId")]
)
data class SituationOutcomeEntity(
    @PrimaryKey val outcomeId: String,
    val optionId: String,
    val consequenceText: String,   // explicacion educativa breve, nunca "correcto/incorrecto"
    val xpAwarded: Int
)
