package com.educalab.clasesmart.domain

import com.educalab.clasesmart.domain.logic.BadgeEngine
import org.junit.Assert.*
import org.junit.Test

class BadgeEngineTest {

    @Test
    fun `estadisticas en cero no otorgan ninguna insignia`() {
        val result = BadgeEngine.evaluateNewlyEarned(BadgeEngine.UserStats(), emptySet())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `otorga gran organizador al alcanzar tres horarios sin problemas`() {
        val stats = BadgeEngine.UserStats(schedulesWithoutIssues = 3)
        val result = BadgeEngine.evaluateNewlyEarned(stats, emptySet())
        assertTrue(result.contains("gran_organizador"))
    }

    @Test
    fun `no repite una insignia que ya se tenia`() {
        val stats = BadgeEngine.UserStats(schedulesWithoutIssues = 3)
        val result = BadgeEngine.evaluateNewlyEarned(stats, setOf("gran_organizador"))
        assertFalse(result.contains("gran_organizador"))
    }

    @Test
    fun `puede otorgar varias insignias a la vez si se cumplen varias condiciones`() {
        val stats = BadgeEngine.UserStats(schedulesWithoutIssues = 8, teamsWithFullCoverage = 6)
        val result = BadgeEngine.evaluateNewlyEarned(stats, emptySet())
        assertTrue(result.contains("gran_organizador"))
        assertTrue(result.contains("maestro_del_tiempo"))
        assertTrue(result.contains("planificador_creativo"))
        assertTrue(result.contains("equipo_imparable"))
        assertTrue(result.contains("experto_en_colaboracion"))
    }

    @Test
    fun `lider de proyecto se otorga con un solo proyecto completado`() {
        val stats = BadgeEngine.UserStats(projectsCompleted = 1)
        val result = BadgeEngine.evaluateNewlyEarned(stats, emptySet())
        assertTrue(result.contains("lider_de_proyecto"))
    }
}
