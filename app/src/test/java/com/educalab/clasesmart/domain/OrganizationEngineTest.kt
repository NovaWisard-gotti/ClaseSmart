package com.educalab.clasesmart.domain

import com.educalab.clasesmart.domain.logic.OrganizationEngine
import com.educalab.clasesmart.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class OrganizationEngineTest {

    private val ciencias = PlannableActivity("act_ciencias", "Ciencias", Subject.CIENCIAS, 40)
    private val arte = PlannableActivity("act_arte", "Arte", Subject.ARTE, 30)
    private val expo = PlannableActivity("act_expo", "Exposicion", Subject.EXPOSICION, 20)
    private val recreo = PlannableActivity("act_recreo", "Recreo", Subject.RECREO, 20)

    @Test
    fun `plan sin problemas es valido`() {
        val slots = listOf(TimeSlot("b1", 0, 40), TimeSlot("b2", 40, 60, isRecess = true))
        val assignments = listOf(
            ScheduleAssignment(slots[0], ciencias),
            ScheduleAssignment(slots[1], recreo)
        )
        val result = OrganizationEngine.evaluate(slots, listOf(ciencias, recreo), assignments)
        assertTrue(result.isPlanValido)
        assertTrue(result.issues.isEmpty())
    }

    @Test
    fun `detecta solapamiento en el mismo bloque`() {
        val slot = TimeSlot("b1", 0, 40)
        val assignments = listOf(ScheduleAssignment(slot, ciencias), ScheduleAssignment(slot, arte))
        val result = OrganizationEngine.evaluate(listOf(slot), listOf(ciencias, arte), assignments)
        assertTrue(result.issues.any { it is ScheduleIssue.Solapamiento })
        assertFalse(result.isPlanValido)
    }

    @Test
    fun `detecta solapamiento por horario aunque el bloque tenga distinto id`() {
        val slotA = TimeSlot("b1", 0, 40)
        val slotB = TimeSlot("b2", 20, 60)
        val assignments = listOf(ScheduleAssignment(slotA, ciencias), ScheduleAssignment(slotB, arte))
        val result = OrganizationEngine.evaluate(listOf(slotA, slotB), listOf(ciencias, arte), assignments)
        assertTrue(result.issues.any { it is ScheduleIssue.Solapamiento })
    }

    @Test
    fun `detecta tiempo insuficiente cuando la actividad no cabe en el bloque`() {
        val slot = TimeSlot("b1", 0, 10)
        val assignments = listOf(ScheduleAssignment(slot, ciencias))
        val result = OrganizationEngine.evaluate(listOf(slot), listOf(ciencias), assignments)
        assertTrue(result.issues.any { it is ScheduleIssue.TiempoInsuficiente })
    }

    @Test
    fun `detecta secuencia poco apropiada entre dos actividades de alta energia seguidas`() {
        val slotArte = TimeSlot("b1", 0, 30)
        val slotExpo = TimeSlot("b2", 30, 50)
        val assignments = listOf(ScheduleAssignment(slotArte, arte), ScheduleAssignment(slotExpo, expo))
        val result = OrganizationEngine.evaluate(listOf(slotArte, slotExpo), listOf(arte, expo), assignments)
        assertTrue(result.issues.any { it is ScheduleIssue.SecuenciaPocoApropiada })
    }

    @Test
    fun `no marca secuencia poco apropiada si hay recreo de por medio`() {
        val slotArte = TimeSlot("b1", 0, 30)
        val slotRecreo = TimeSlot("b2", 30, 50, isRecess = true)
        val slotExpo = TimeSlot("b3", 50, 70)
        val assignments = listOf(
            ScheduleAssignment(slotArte, arte),
            ScheduleAssignment(slotRecreo, recreo),
            ScheduleAssignment(slotExpo, expo)
        )
        val result = OrganizationEngine.evaluate(listOf(slotArte, slotRecreo, slotExpo), listOf(arte, recreo, expo), assignments)
        assertFalse(result.issues.any { it is ScheduleIssue.SecuenciaPocoApropiada })
    }

    @Test
    fun `detecta actividades planificadas sin espacio asignado`() {
        val result = OrganizationEngine.evaluate(emptyList(), listOf(ciencias), emptyList())
        assertTrue(result.issues.any { it is ScheduleIssue.SinEspacio })
        assertFalse(result.isPlanValido)
    }

    @Test
    fun `lista vacia de actividades y bloques no lanza excepcion y no es un plan valido`() {
        val result = OrganizationEngine.evaluate(emptyList(), emptyList(), emptyList())
        assertTrue(result.issues.isEmpty())
        assertFalse(result.isPlanValido) // sin asignaciones, no hay nada que celebrar
    }

    @Test
    fun `caso limite doble asignacion identica no lanza excepcion`() {
        val slot = TimeSlot("b1", 0, 40)
        val assignments = listOf(ScheduleAssignment(slot, ciencias), ScheduleAssignment(slot, ciencias))
        val result = OrganizationEngine.evaluate(listOf(slot), listOf(ciencias), assignments)
        assertTrue(result.issues.any { it is ScheduleIssue.Solapamiento })
    }
}
