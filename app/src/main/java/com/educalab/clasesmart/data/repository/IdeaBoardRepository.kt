package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.IdeaNoteDao
import com.educalab.clasesmart.data.local.entity.IdeaNoteEntity
import com.educalab.clasesmart.domain.model.IdeaNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IdeaBoardRepository(private val ideaNoteDao: IdeaNoteDao) {
    fun observeNotes(): Flow<List<IdeaNote>> =
        ideaNoteDao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun saveNote(note: IdeaNote) {
        ideaNoteDao.upsert(
            IdeaNoteEntity(
                noteId = note.noteId,
                text = note.text,
                colorTag = note.colorTag,
                offsetX = note.offsetX,
                offsetY = note.offsetY,
                createdAtEpochMs = note.createdAtEpochMs
            )
        )
    }

    suspend fun deleteNote(noteId: String) {
        ideaNoteDao.delete(noteId)
    }
}

private fun IdeaNoteEntity.toDomain() = IdeaNote(
    noteId = noteId,
    text = text,
    colorTag = colorTag,
    offsetX = offsetX,
    offsetY = offsetY,
    createdAtEpochMs = createdAtEpochMs
)
