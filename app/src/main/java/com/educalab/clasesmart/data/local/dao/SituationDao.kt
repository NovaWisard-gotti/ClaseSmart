package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassroomSituationDao {
    @Query("SELECT * FROM classroom_situation")
    fun observeAll(): Flow<List<ClassroomSituationEntity>>

    @Query("SELECT * FROM classroom_situation WHERE category = :category")
    suspend fun getByCategory(category: String): List<ClassroomSituationEntity>

    @Query("SELECT * FROM classroom_situation WHERE situationId = :id")
    suspend fun getById(id: String): ClassroomSituationEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(situations: List<ClassroomSituationEntity>)
}

@Dao
interface SituationOptionDao {
    @Query("SELECT * FROM situation_option WHERE situationId = :situationId")
    suspend fun getOptions(situationId: String): List<SituationOptionEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(options: List<SituationOptionEntity>)
}

@Dao
interface SituationOutcomeDao {
    @Query("SELECT * FROM situation_outcome WHERE optionId = :optionId LIMIT 1")
    suspend fun getForOption(optionId: String): SituationOutcomeEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(outcomes: List<SituationOutcomeEntity>)
}
