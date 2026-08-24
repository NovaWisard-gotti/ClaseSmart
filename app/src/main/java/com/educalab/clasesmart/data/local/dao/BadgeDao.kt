package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badge")
    fun observeAll(): Flow<List<BadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(badges: List<BadgeEntity>)
}

@Dao
interface UserBadgeDao {
    @Query("SELECT * FROM user_badge")
    fun observeEarned(): Flow<List<UserBadgeEntity>>

    @Query("SELECT COUNT(*) FROM user_badge WHERE badgeId = :badgeId")
    suspend fun isEarned(badgeId: String): Int

    @Insert
    suspend fun insert(userBadge: UserBadgeEntity): Long
}
