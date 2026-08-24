package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(team: TeamEntity)

    @Query("SELECT * FROM team WHERE activityId = :activityId")
    suspend fun getTeamsForActivity(activityId: String): List<TeamEntity>

    @Query("UPDATE team SET coverageScore = :score WHERE teamId = :teamId")
    suspend fun updateCoverage(teamId: String, score: Int)
}

@Dao
interface TeamMemberDao {
    @Insert
    suspend fun insertAll(members: List<TeamMemberEntity>)

    @Query("SELECT * FROM team_member WHERE teamId = :teamId")
    suspend fun getMembers(teamId: String): List<TeamMemberEntity>

    @Query("DELETE FROM team_member WHERE teamId = :teamId")
    suspend fun clearTeam(teamId: String)
}
