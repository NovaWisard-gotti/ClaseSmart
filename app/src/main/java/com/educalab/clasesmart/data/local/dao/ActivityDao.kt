package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassActivityDao {
    @Query("SELECT * FROM class_activity")
    fun observeAll(): Flow<List<ClassActivityEntity>>

    @Query("SELECT * FROM class_activity WHERE activityId = :id")
    suspend fun getById(id: String): ClassActivityEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(activities: List<ClassActivityEntity>)
}

@Dao
interface ActivityStepDao {
    @Query("SELECT * FROM activity_step WHERE activityId = :activityId ORDER BY orderIndex ASC")
    suspend fun getSteps(activityId: String): List<ActivityStepEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(steps: List<ActivityStepEntity>)
}

@Dao
interface ActivityAttemptDao {
    @Insert
    suspend fun insert(attempt: ActivityAttemptEntity): Long

    @Query("SELECT * FROM activity_attempt WHERE activityId = :activityId ORDER BY timestampEpochMs DESC")
    suspend fun getAttempts(activityId: String): List<ActivityAttemptEntity>
}
