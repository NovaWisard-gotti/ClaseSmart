package com.educalab.clasesmart.domain

import com.educalab.clasesmart.domain.logic.TeamFormationEngine
import com.educalab.clasesmart.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class TeamFormationEngineTest {

    private val alex = SchoolCharacter("alex", "Alex", "Organizado", listOf(CharacterSkillLevel(Skill.ORGANIZAR, 3)))
    private val mia = SchoolCharacter("mia", "Mia", "Creativa", listOf(CharacterSkillLevel(Skill.DIBUJAR, 3)))
    private val leo = SchoolCharacter("leo", "Leo", "Curioso", listOf(CharacterSkillLevel(Skill.INVESTIGAR, 2)))

    @Test
    fun `equipo vacio da cobertura cero y no lanza excepcion`() {
        val result = TeamFormationEngine.evaluate(TeamProposal("t1", "a1", emptyList()), listOf(Skill.ORGANIZAR))
        assertEquals(0, result.coverageScore)
        assertEquals(listOf(Skill.ORGANIZAR), result.missingSkills)
    }

    @Test
    fun `equipo que cubre todas las habilidades requeridas tiene score 100`() {
        val proposal = TeamProposal("t1", "a1", listOf(alex, mia))
        val result = TeamFormationEngine.evaluate(proposal, listOf(Skill.ORGANIZAR, Skill.DIBUJAR))
        assertEquals(100, result.coverageScore)
        assertTrue(result.missingSkills.isEmpty())
    }

    @Test
    fun `detecta habilidad faltante en el equipo`() {
        val proposal = TeamProposal("t1", "a1", listOf(alex))
        val result = TeamFormationEngine.evaluate(proposal, listOf(Skill.ORGANIZAR, Skill.DIBUJAR))
        assertEquals(listOf(Skill.DIBUJAR), result.missingSkills)
        assertEquals(50, result.coverageScore)
    }

    @Test
    fun `texto de complemento no compara personas entre si`() {
        val proposal = TeamProposal("t1", "a1", listOf(alex, mia, leo))
        val result = TeamFormationEngine.evaluate(proposal, listOf(Skill.ORGANIZAR, Skill.DIBUJAR, Skill.INVESTIGAR))
        assertFalse(result.complementText.contains("mejor"))
        assertTrue(result.complementText.contains("combina"))
    }

    @Test
    fun `lista de habilidades requeridas vacia no lanza excepcion`() {
        val result = TeamFormationEngine.evaluate(TeamProposal("t1", "a1", listOf(alex)), emptyList())
        assertEquals(0, result.coverageScore)
    }

    @Test
    fun `equipo con un solo integrante repitiendo la misma habilidad no marca redundancia con menos de tres apariciones`() {
        val result = TeamFormationEngine.evaluate(TeamProposal("t1", "a1", listOf(alex)), listOf(Skill.ORGANIZAR))
        assertTrue(result.redundantSkills.isEmpty())
    }
}
