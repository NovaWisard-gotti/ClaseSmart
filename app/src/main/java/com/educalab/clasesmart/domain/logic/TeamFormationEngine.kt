package com.educalab.clasesmart.domain.logic

import com.educalab.clasesmart.domain.model.*

/**
 * Motor de formacion de equipos (Modulo 4 - "Equipos en accion").
 * No clasifica ni jerarquiza personas: calcula cobertura de habilidades
 * y explica como se complementan, nunca compara "quien es mejor".
 */
object TeamFormationEngine {

    fun evaluate(proposal: TeamProposal, requiredSkills: List<Skill>): TeamEvaluation {
        if (proposal.members.isEmpty() || requiredSkills.isEmpty()) {
            return TeamEvaluation(
                proposal = proposal,
                coverage = requiredSkills.map { SkillCoverage(it, covered = false, bestStrength = 0) },
                coverageScore = 0,
                redundantSkills = emptyList(),
                missingSkills = requiredSkills,
                complementText = "Todavia falta formar el equipo para esta actividad."
            )
        }

        val strengthBySkill: Map<Skill, List<Int>> = requiredSkills.associateWith { skill ->
            proposal.members.flatMap { member ->
                member.skills.filter { it.skill == skill }.map { it.strength }
            }
        }

        val coverage = requiredSkills.map { skill ->
            val strengths = strengthBySkill[skill].orEmpty()
            SkillCoverage(skill = skill, covered = strengths.isNotEmpty(), bestStrength = strengths.maxOrNull() ?: 0)
        }

        val missing = coverage.filter { !it.covered }.map { it.skill }
        val redundant = requiredSkills.filter { skill -> (strengthBySkill[skill]?.size ?: 0) > 2 }

        val coveredCount = coverage.count { it.covered }
        val score = if (requiredSkills.isEmpty()) 0
            else ((coveredCount.toFloat() / requiredSkills.size) * 100).toInt()

        val distinctTraits = proposal.members.map { it.trait }.distinct()
        val complementText = when {
            missing.isNotEmpty() ->
                "Este equipo todavia necesita a alguien que sepa ${missing.joinToString(" y ") { it.name.lowercase() }}."
            distinctTraits.size >= 2 ->
                "Este equipo combina diferentes habilidades: ${distinctTraits.joinToString(", ")}. Juntas cubren todo lo que pide la actividad."
            else ->
                "El equipo cubre la actividad, aunque todos aportan un estilo parecido; podria sumar variedad."
        }

        return TeamEvaluation(
            proposal = proposal,
            coverage = coverage,
            coverageScore = score,
            redundantSkills = redundant,
            missingSkills = missing,
            complementText = complementText
        )
    }
}
