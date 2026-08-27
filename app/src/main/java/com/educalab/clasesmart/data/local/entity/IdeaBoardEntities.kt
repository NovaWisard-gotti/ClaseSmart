package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Nota libre de la Pizarra de ideas (Modulo 6). Persiste texto, color y posicion. */
@Entity(tableName = "idea_note")
data class IdeaNoteEntity(
    @PrimaryKey val noteId: String,
    val text: String,
    val colorTag: String, // AMARILLO, AZUL, ROSA
    val offsetX: Float,
    val offsetY: Float,
    val createdAtEpochMs: Long
)
