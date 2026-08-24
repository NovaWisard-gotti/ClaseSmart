package com.educalab.clasesmart.domain

import com.educalab.clasesmart.domain.logic.TimeManagementEngine
import com.educalab.clasesmart.domain.model.PlannableActivity
import com.educalab.clasesmart.domain.model.Subject
import org.junit.Assert.*
import org.junit.Test

class TimeManagementEngineTest {

    private val a = PlannableActivity("a", "Lectura", Subject.LECTURA, 15)
    private val b = PlannableActivity("b", "Ciencias", Subject.CIENCIAS, 20)

    @Test
    fun `tareas que caben en el presupuesto marcan fits true`() {
        val result = TimeManagementEngine.evaluate(40, listOf(a, b))
        assertTrue(result.fits)
        assertEquals(5, result.remainingMinutes)
    }

    @Test
    fun `tareas que no caben marcan fits false y sugieren quitar la mas larga`() {
        val result = TimeManagementEngine.evaluate(20, listOf(a, b))
        assertFalse(result.fits)
        assertEquals(b, result.suggestionToRemove)
    }

    @Test
    fun `presupuesto negativo se trata como cero sin lanzar excepcion`() {
        val result = TimeManagementEngine.evaluate(-10, listOf(a))
        assertEquals(0, result.budgetMinutes)
        assertFalse(result.fits)
    }

    @Test
    fun `lista de tareas vacia siempre cabe`() {
        val result = TimeManagementEngine.evaluate(30, emptyList())
        assertTrue(result.fits)
        assertEquals(30, result.remainingMinutes)
    }

    @Test
    fun `presupuesto exacto cabe justo`() {
        val result = TimeManagementEngine.evaluate(15, listOf(a))
        assertTrue(result.fits)
        assertEquals(0, result.remainingMinutes)
    }
}
