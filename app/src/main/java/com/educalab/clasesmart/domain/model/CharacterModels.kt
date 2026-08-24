package com.educalab.clasesmart.domain.model

enum class Expression {
    FELIZ, CONFUNDIDO, PREOCUPADO, CONCENTRADO, SORPRENDIDO, ORGULLOSO, PENSATIVO, COLABORANDO
}

enum class Skill { DIBUJAR, EXPLICAR, ORGANIZAR, OBSERVAR, CONSTRUIR, INVESTIGAR }

data class CharacterSkillLevel(val skill: Skill, val strength: Int)

data class SchoolCharacter(
    val characterId: String,
    val name: String,
    val trait: String,
    val skills: List<CharacterSkillLevel>,
    val expression: Expression = Expression.FELIZ
)
