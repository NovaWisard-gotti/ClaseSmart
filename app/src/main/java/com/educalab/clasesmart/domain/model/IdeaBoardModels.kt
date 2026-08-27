package com.educalab.clasesmart.domain.model

data class IdeaNote(
    val noteId: String,
    val text: String,
    val colorTag: String,
    val offsetX: Float,
    val offsetY: Float,
    val createdAtEpochMs: Long
)
