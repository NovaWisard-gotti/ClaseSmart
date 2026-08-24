package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.InteractionHistoryDao
import com.educalab.clasesmart.data.local.dao.UserProgressDao
import com.educalab.clasesmart.data.local.entity.InteractionHistoryEntity
import com.educalab.clasesmart.data.local.entity.UserProgressEntity
import com.educalab.clasesmart.domain.logic.ProgressEngine
import com.educalab.clasesmart.domain.model.LevelUpResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgressRepository(
    private val progressDao: UserProgressDao,
    private val historyDao: InteractionHistoryDao
) {
    fun observeXp(): Flow<Int> = progressDao.observeProgress().map { it?.totalXp ?: 0 }

    /** Registra la interaccion real y aplica el motor de progreso: nunca se otorga XP "de la nada". */
    suspend fun recordInteractionAndAwardXp(
        kind: String,
        referenceId: String,
        xpAwarded: Int,
        wasSuccessful: Boolean,
        nowEpochMs: Long,
        detail: String = ""
    ): LevelUpResult {
        historyDao.insert(
            InteractionHistoryEntity(
                kind = kind,
                referenceId = referenceId,
                xpAwarded = xpAwarded,
                wasSuccessful = wasSuccessful,
                timestampEpochMs = nowEpochMs,
                detail = detail
            )
        )
        val current = progressDao.getProgress() ?: UserProgressEntity(totalXp = 0, aulaLevel = 1)
        val result = ProgressEngine.applyXp(current.totalXp, xpAwarded)
        progressDao.upsert(
            current.copy(
                totalXp = current.totalXp + xpAwarded.coerceAtLeast(0),
                aulaLevel = result.newLevel,
                lastSessionEpochMs = nowEpochMs
            )
        )
        return result
    }
}
