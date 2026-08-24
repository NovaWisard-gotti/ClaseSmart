package com.educalab.clasesmart.domain

import com.educalab.clasesmart.domain.logic.MaterialAssignmentEngine
import com.educalab.clasesmart.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class MaterialAssignmentEngineTest {

    private val lapiz = SchoolMaterial("mat_lapiz", "Lapiz", MaterialCategory.ESCRITURA, distanceUnits = 1)
    private val lupa = SchoolMaterial("mat_lupa", "Lupa", MaterialCategory.CIENCIAS, distanceUnits = 4)
    private val papel = SchoolMaterial("mat_papel", "Papel", MaterialCategory.ARTE, distanceUnits = 1)
    private val mission = MaterialMission("m1", "Experimento", listOf("mat_lapiz", "mat_lupa"))

    @Test
    fun `mision lista cuando todos los materiales requeridos estan en la mesa`() {
        val result = MaterialAssignmentEngine.evaluate(mission, listOf(lapiz, lupa, papel), listOf(lapiz, lupa.copy(distanceUnits = 1)))
        assertTrue(result.isReady)
    }

    @Test
    fun `detecta material faltante`() {
        val result = MaterialAssignmentEngine.evaluate(mission, listOf(lapiz, lupa), listOf(lapiz))
        assertFalse(result.isReady)
        assertTrue(result.issues.any { it is MaterialIssue.Falta })
    }

    @Test
    fun `detecta material requerido que vino de demasiado lejos`() {
        val result = MaterialAssignmentEngine.evaluate(mission, listOf(lapiz, lupa), listOf(lapiz, lupa))
        assertTrue(result.issues.any { it is MaterialIssue.DemasiadoLejos })
    }

    @Test
    fun `detecta material extra que no hace falta`() {
        val result = MaterialAssignmentEngine.evaluate(mission, listOf(lapiz, lupa, papel), listOf(lapiz, lupa.copy(distanceUnits = 1), papel))
        assertTrue(result.issues.any { it is MaterialIssue.ExtraSinUso })
    }

    @Test
    fun `mesa vacia nunca esta lista`() {
        val result = MaterialAssignmentEngine.evaluate(mission, listOf(lapiz, lupa), emptyList())
        assertFalse(result.isReady)
        assertEquals(2, result.issues.count { it is MaterialIssue.Falta })
    }

    @Test
    fun `mision sin materiales requeridos nunca se marca lista aunque la mesa este vacia`() {
        val vacio = MaterialMission("m2", "Actividad libre", emptyList())
        val result = MaterialAssignmentEngine.evaluate(vacio, listOf(lapiz), emptyList())
        assertFalse(result.isReady)
        assertTrue(result.issues.isEmpty())
    }

    @Test
    fun `materiales requeridos duplicados en la mision no generan doble error de falta`() {
        val duplicada = MaterialMission("m3", "Con duplicado", listOf("mat_lapiz", "mat_lapiz"))
        val result = MaterialAssignmentEngine.evaluate(duplicada, listOf(lapiz), emptyList())
        assertEquals(1, result.issues.count { it is MaterialIssue.Falta })
    }
}
