package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeBlockDao {
    @Query("SELECT * FROM time_block ORDER BY startMinute ASC")
    fun observeAll(): Flow<List<TimeBlockEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(blocks: List<TimeBlockEntity>)
}

@Dao
interface ScheduleActivityDao {
    @Query("SELECT * FROM schedule_activity WHERE planDate = :date")
    fun observeForDate(date: String): Flow<List<ScheduleActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<ScheduleActivityEntity>)

    @Query("DELETE FROM schedule_activity WHERE planDate = :date")
    suspend fun clearDate(date: String)
}
