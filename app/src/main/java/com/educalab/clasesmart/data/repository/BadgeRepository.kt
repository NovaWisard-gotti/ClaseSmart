package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.BadgeDao
import com.educalab.clasesmart.data.local.dao.UserBadgeDao
import com.educalab.clasesmart.data.local.entity.UserBadgeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BadgeRepository(
    private val badgeDao: BadgeDao,
    private val userBadgeDao: UserBadgeDao
) {
    fun observeEarnedIds(): Flow<Set<String>> = userBadgeDao.observeEarned().map { list -> list.map { it.badgeId }.toSet() }

    suspend fun awardBadges(badgeIds: List<String>, nowEpochMs: Long) {
        badgeIds.forEach { id ->
            if (userBadgeDao.isEarned(id) == 0) {
                userBadgeDao.insert(UserBadgeEntity(badgeId = id, earnedAtEpochMs = nowEpochMs))
            }
        }
    }
}
