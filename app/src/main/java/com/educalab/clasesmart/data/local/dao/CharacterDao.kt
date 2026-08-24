package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentCharacterDao {
    @Query("SELECT * FROM student_character")
    fun observeAll(): Flow<List<StudentCharacterEntity>>

    @Query("SELECT * FROM student_character WHERE characterId = :id")
    suspend fun getById(id: String): StudentCharacterEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<StudentCharacterEntity>)
}

@Dao
interface CharacterSkillDao {
    @Query("SELECT * FROM character_skill WHERE characterId = :characterId")
    suspend fun getSkills(characterId: String): List<CharacterSkillEntity>

    @Query("SELECT * FROM character_skill")
    suspend fun getAll(): List<CharacterSkillEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(skills: List<CharacterSkillEntity>)
}
