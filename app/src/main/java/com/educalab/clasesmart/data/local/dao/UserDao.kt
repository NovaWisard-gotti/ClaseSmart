package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = :id")
    fun observeProfile(id: String = "local_user"): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = :id")
    suspend fun getProfile(id: String = "local_user"): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET onboardingCompleted = 1, lastOpenedEpochMs = :now WHERE id = :id")
    suspend fun markOnboardingCompleted(id: String = "local_user", now: Long)

    @Query("UPDATE user_profile SET soundEnabled = :enabled WHERE id = :id")
    suspend fun setSoundEnabled(enabled: Boolean, id: String = "local_user")

    @Query("UPDATE user_profile SET hapticEnabled = :enabled WHERE id = :id")
    suspend fun setHapticEnabled(enabled: Boolean, id: String = "local_user")
}

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = :id")
    fun observeProgress(id: String = "local_user"): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE id = :id")
    suspend fun getProgress(id: String = "local_user"): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: UserProgressEntity)
}

@Dao
interface InteractionHistoryDao {
    @Insert
    suspend fun insert(entry: InteractionHistoryEntity): Long

    @Query("SELECT * FROM interaction_history ORDER BY timestampEpochMs DESC LIMIT :limit")
    fun observeRecent(limit: Int = 50): Flow<List<InteractionHistoryEntity>>

    @Query("SELECT * FROM interaction_history WHERE kind = :kind ORDER BY timestampEpochMs DESC")
    suspend fun getByKind(kind: String): List<InteractionHistoryEntity>

    @Query("SELECT COUNT(*) FROM interaction_history WHERE kind = :kind AND wasSuccessful = 1")
    suspend fun countSuccessfulByKind(kind: String): Int
}
