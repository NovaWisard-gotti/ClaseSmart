package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Personaje escolar (Alex, Mia, Leo, Sam, Dani y companeros adicionales).
 * Cada personaje tiene rasgos y una expresion visual actual, NO es un
 * avatar circular generico.
 */
@Entity(tableName = "student_character")
data class StudentCharacterEntity(
    @PrimaryKey val characterId: String,
    val name: String,
    val trait: String,          // ej. "Organizado", "Creativa", "Curioso"
    val spriteBaseId: String,   // referencia al set de ilustracion vectorial
    val defaultExpression: String = "FELIZ",
    val isMainCast: Boolean = true
)

/** Habilidades de un personaje, usadas por el motor de formacion de equipos. */
@Entity(
    tableName = "character_skill",
    foreignKeys = [
        ForeignKey(
            entity = StudentCharacterEntity::class,
            parentColumns = ["characterId"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("characterId")]
)
data class CharacterSkillEntity(
    @PrimaryKey(autoGenerate = true) val skillRowId: Long = 0,
    val characterId: String,
    val skill: String, // DIBUJAR, EXPLICAR, ORGANIZAR, OBSERVAR, CONSTRUIR, INVESTIGAR
    val strength: Int  // 1..3, usado para calcular cobertura de equipo
)
