package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.TeamDao
import com.educalab.clasesmart.data.local.dao.TeamMemberDao
import com.educalab.clasesmart.data.local.entity.TeamEntity
import com.educalab.clasesmart.data.local.entity.TeamMemberEntity

class TeamRepository(
    private val teamDao: TeamDao,
    private val memberDao: TeamMemberDao
) {
    suspend fun saveTeam(teamId: String, activityId: String, characterIds: List<String>, coverageScore: Int, nowEpochMs: Long) {
        teamDao.upsert(TeamEntity(teamId = teamId, activityId = activityId, name = "Equipo", createdAtEpochMs = nowEpochMs, coverageScore = coverageScore))
        memberDao.clearTeam(teamId)
        memberDao.insertAll(characterIds.map { TeamMemberEntity(teamId = teamId, characterId = it) })
    }
}
