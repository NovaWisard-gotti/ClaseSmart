package com.educalab.clasesmart.domain.model

data class UserProgressState(
    val totalXp: Int,
    val aulaLevel: Int,
    val sessionsCount: Int,
    val situationsResolved: Int,
    val projectsCompleted: Int
)

data class Badge(
    val badgeId: String,
    val name: String,
    val description: String,
    val skillArea: String
)

data class XpAward(val amount: Int, val reason: String)

data class LevelUpResult(val leveledUp: Boolean, val newLevel: Int, val newlyUnlockedObjectTypes: List<ClassroomObjectType>)
