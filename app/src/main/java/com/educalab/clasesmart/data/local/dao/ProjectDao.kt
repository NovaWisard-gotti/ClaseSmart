package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun observeAll(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(projects: List<ProjectEntity>)
}

@Dao
interface ProjectTaskDao {
    @Query("SELECT * FROM project_task WHERE projectId = :projectId ORDER BY orderIndex ASC")
    fun observeTasks(projectId: String): Flow<List<ProjectTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(tasks: List<ProjectTaskEntity>)
}

@Dao
interface ProjectProgressDao {
    @Query("SELECT * FROM project_progress WHERE projectId = :projectId")
    fun observeProgress(projectId: String): Flow<ProjectProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: ProjectProgressEntity)
}
