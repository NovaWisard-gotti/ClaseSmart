package com.educalab.clasesmart.domain.model

data class TeamProposal(val teamId: String, val activityId: String, val members: List<SchoolCharacter>)

data class SkillCoverage(val skill: Skill, val covered: Boolean, val bestStrength: Int)

data class TeamEvaluation(
    val proposal: TeamProposal,
    val coverage: List<SkillCoverage>,
    val coverageScore: Int,       // 0..100
    val redundantSkills: List<Skill>,
    val missingSkills: List<Skill>,
    val complementText: String    // explicacion tipo "Este equipo combina..."
)
