package com.educalab.clasesmart.domain

import com.educalab.clasesmart.domain.logic.ConsequenceEngine
import com.educalab.clasesmart.domain.model.MaterialIssue
import com.educalab.clasesmart.domain.model.PlannableActivity
import com.educalab.clasesmart.domain.model.ScheduleIssue
import com.educalab.clasesmart.domain.model.Subject
import org.junit.Assert.*
import org.junit.Test

class ConsequenceEngineTest {

    @Test
    fun `sin issues de horario devuelve mensaje positivo`() {
        val text = ConsequenceEngine.forSchedule(emptyList())
        assertTrue(text.contains("Funciona"))
    }

    @Test
    fun `con issues de horario concatena los mensajes`() {
        val a = PlannableActivity("a", "Arte", Subject.ARTE, 30)
        val b = PlannableActivity("b", "Exposicion", Subject.EXPOSICION, 20)
        val text = ConsequenceEngine.forSchedule(listOf(ScheduleIssue.Solapamiento(a, b)))
        assertTrue(text.contains("Arte") && text.contains("Exposicion"))
    }

    @Test
    fun `sin issues de materiales devuelve mensaje positivo`() {
        val text = ConsequenceEngine.forMaterials(emptyList())
        assertTrue(text.contains("listo", ignoreCase = true))
    }

    @Test
    fun `con issues de materiales incluye el nombre del material`() {
        val text = ConsequenceEngine.forMaterials(listOf(MaterialIssue.Falta("Lupa")))
        assertTrue(text.contains("Lupa"))
    }
}
