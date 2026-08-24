package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.CharacterSkillDao
import com.educalab.clasesmart.data.local.dao.StudentCharacterDao
import com.educalab.clasesmart.data.local.entity.CharacterSkillEntity
import com.educalab.clasesmart.data.local.entity.StudentCharacterEntity
import com.educalab.clasesmart.domain.model.CharacterSkillLevel
import com.educalab.clasesmart.domain.model.Expression
import com.educalab.clasesmart.domain.model.SchoolCharacter
import com.educalab.clasesmart.domain.model.Skill
import kotlinx.coroutines.flow.first

class CharacterRepository(
    private val characterDao: StudentCharacterDao,
    private val skillDao: CharacterSkillDao
) {
    suspend fun getAllWithSkills(): List<SchoolCharacter> {
        val characters = characterDao.observeAll().first()
        val allSkills = skillDao.getAll().groupBy { it.characterId }
        return characters.map { c -> c.toDomain(allSkills[c.characterId].orEmpty()) }
    }
}

private fun StudentCharacterEntity.toDomain(skills: List<CharacterSkillEntity>): SchoolCharacter = SchoolCharacter(
    characterId = characterId,
    name = name,
    trait = trait,
    skills = skills.map { CharacterSkillLevel(Skill.valueOf(it.skill), it.strength) },
    expression = Expression.valueOf(defaultExpression)
)
