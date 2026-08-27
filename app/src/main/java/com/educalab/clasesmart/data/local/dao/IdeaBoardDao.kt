package com.educalab.clasesmart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.clasesmart.data.local.entity.IdeaNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaNoteDao {
    @Query("SELECT * FROM idea_note ORDER BY createdAtEpochMs ASC")
    fun observeAll(): Flow<List<IdeaNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: IdeaNoteEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(notes: List<IdeaNoteEntity>)

    @Query("DELETE FROM idea_note WHERE noteId = :noteId")
    suspend fun delete(noteId: String)
}
