package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.*
import com.educalab.clasesmart.data.local.entity.*
import com.educalab.clasesmart.domain.model.*
import kotlinx.coroutines.flow.first

class SituationRepository(
    private val situationDao: ClassroomSituationDao,
    private val optionDao: SituationOptionDao,
    private val outcomeDao: SituationOutcomeDao
) {
    suspend fun getByCategory(category: SituationCategory): List<ClassroomSituation> =
        situationDao.getByCategory(category.name).map { it.toDomainWithOptions() }

    suspend fun getAll(): List<ClassroomSituation> =
        situationDao.observeAll().first().map { it.toDomainWithOptions() }

    suspend fun getOutcomeMap(situationId: String): Map<String, SituationOutcome> {
        val options = optionDao.getOptions(situationId)
        return options.associate { opt ->
            val outcomeEntity = outcomeDao.getForOption(opt.optionId)
            opt.optionId to (outcomeEntity?.let {
                SituationOutcome(it.consequenceText, it.xpAwarded, opt.qualityLevel)
            } ?: SituationOutcome("", 0, opt.qualityLevel))
        }
    }

    private suspend fun ClassroomSituationEntity.toDomainWithOptions(): ClassroomSituation {
        val options = optionDao.getOptions(situationId)
        return ClassroomSituation(
            situationId = situationId,
            title = title,
            sceneDescription = sceneDescription,
            category = SituationCategory.valueOf(category),
            involvedCharacterIds = if (involvedCharacterIds.isBlank()) emptyList() else involvedCharacterIds.split(","),
            options = options.map { SituationChoice(it.optionId, it.actionLabel, it.qualityLevel) }
        )
    }
}
