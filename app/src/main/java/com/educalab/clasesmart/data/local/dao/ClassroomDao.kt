package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassroomDao {
    @Query("SELECT * FROM classroom WHERE id = :id")
    fun observeClassroom(id: String = "main_classroom"): Flow<ClassroomEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(classroom: ClassroomEntity)

    @Query("UPDATE classroom SET currentLevel = :level WHERE id = :id")
    suspend fun updateLevel(level: Int, id: String = "main_classroom")

    @Query("UPDATE classroom SET lastVisitedZone = :zone WHERE id = :id")
    suspend fun updateLastZone(zone: String, id: String = "main_classroom")
}

@Dao
interface ClassroomObjectDao {
    @Query("SELECT * FROM classroom_object WHERE classroomId = :classroomId")
    fun observeObjects(classroomId: String = "main_classroom"): Flow<List<ClassroomObjectEntity>>

    @Query("SELECT * FROM classroom_object WHERE objectId = :objectId")
    suspend fun getObject(objectId: String): ClassroomObjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objects: List<ClassroomObjectEntity>)

    @Query("UPDATE classroom_object SET state = :state WHERE objectId = :objectId")
    suspend fun updateState(objectId: String, state: String)
}

@Dao
interface ClassroomDecorationDao {
    @Query("SELECT * FROM classroom_decoration")
    fun observeAllDecorations(): Flow<List<ClassroomDecorationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(decorations: List<ClassroomDecorationEntity>)

    @Query("SELECT * FROM classroom_decoration WHERE requiredXp <= :xp")
    suspend fun getUnlockableAt(xp: Int): List<ClassroomDecorationEntity>
}

@Dao
interface UnlockedDecorationDao {
    @Query("SELECT * FROM unlocked_decoration WHERE classroomId = :classroomId")
    fun observeUnlocked(classroomId: String = "main_classroom"): Flow<List<UnlockedDecorationEntity>>

    @Insert
    suspend fun insert(unlocked: UnlockedDecorationEntity): Long

    @Query("SELECT COUNT(*) FROM unlocked_decoration WHERE decorationId = :decorationId")
    suspend fun isUnlocked(decorationId: String): Int
}
