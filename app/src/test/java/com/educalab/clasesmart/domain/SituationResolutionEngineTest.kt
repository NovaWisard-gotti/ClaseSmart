package com.educalab.clasesmart.domain

import com.educalab.clasesmart.domain.logic.SituationResolutionEngine
import com.educalab.clasesmart.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class SituationResolutionEngineTest {

    private val situation = ClassroomSituation(
        situationId = "s1",
        title = "Dos estudiantes quieren el mismo material",
        sceneDescription = "...",
        category = SituationCategory.CONVIVENCIA,
        involvedCharacterIds = listOf("alex", "mia"),
        options = listOf(
            SituationChoice("opt_repartir", "Repartir el turno de uso", qualityLevel = 2),
            SituationChoice("opt_esperar", "Decirle que espere sin mas", qualityLevel = 1),
            SituationChoice("opt_ignorar", "Ignorar la situacion", qualityLevel = 0)
        )
    )

    @Test
    fun `devuelve el outcome registrado para la opcion elegida`() {
        val outcomes = mapOf("opt_repartir" to SituationOutcome("Genial, ambos pudieron participar.", 15, 2))
        val result = SituationResolutionEngine.resolve(situation, "opt_repartir", outcomes)
        assertEquals(15, result.xpAwarded)
        assertFalse(result.consequenceText.contains("Correcto"))
        assertFalse(result.consequenceText.contains("Incorrecto"))
    }

    @Test
    fun `usa un outcome de respaldo si no hay uno registrado para la opcion`() {
        val result = SituationResolutionEngine.resolve(situation, "opt_esperar", emptyMap())
        assertEquals(8, result.xpAwarded)
    }

    @Test
    fun `opcion inexistente no lanza excepcion y no otorga xp`() {
        val result = SituationResolutionEngine.resolve(situation, "opt_no_existe", emptyMap())
        assertEquals(0, result.xpAwarded)
    }

    @Test
    fun `opcion de baja calidad nunca humilla en el texto`() {
        val result = SituationResolutionEngine.resolve(situation, "opt_ignorar", emptyMap())
        assertFalse(result.consequenceText.contains("mal", ignoreCase = true))
    }
}
