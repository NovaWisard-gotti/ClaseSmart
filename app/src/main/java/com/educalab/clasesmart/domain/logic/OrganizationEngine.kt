package com.educalab.clasesmart.domain.logic

import com.educalab.clasesmart.domain.model.*

/**
 * Motor de organizacion horaria (Modulo 2 - "Organiza el dia").
 * Pura logica de dominio, sin dependencias de Android, 100% testeable.
 *
 * Reglas:
 *  - Dos actividades no pueden solaparse en el tiempo.
 *  - Una actividad no puede exceder la duracion del bloque donde se coloca.
 *  - Una actividad de alta energia (EXPOSICION) inmediatamente despues de
 *    otra de alta energia (EDUCACION_FISICA-like / TRABAJO_GRUPAL intenso)
 *    sin transicion se marca como secuencia poco apropiada.
 *  - Toda actividad de la lista de "actividades del dia" debe terminar
 *    colocada en algun bloque.
 */
object OrganizationEngine {

    private val HIGH_ENERGY = setOf(Subject.TRABAJO_GRUPAL, Subject.EXPOSICION, Subject.ARTE)

    fun evaluate(
        availableSlots: List<TimeSlot>,
        plannedActivities: List<PlannableActivity>,
        assignments: List<ScheduleAssignment>
    ): ScheduleEvaluation {
        val issues = mutableListOf<ScheduleIssue>()

        // 1) Solapamientos: comparar cada par de asignaciones por bloque.
        for (i in assignments.indices) {
            for (j in i + 1 until assignments.size) {
                val a = assignments[i]
                val b = assignments[j]
                if (a.slot.timeBlockId == b.slot.timeBlockId ||
                    a.slot.overlaps(b.slot)
                ) {
                    issues += ScheduleIssue.Solapamiento(a.activity, b.activity)
                }
            }
        }

        // 2) Tiempo insuficiente por bloque.
        assignments.forEach { assignment ->
            val disponible = assignment.slot.durationMinutes
            if (assignment.activity.durationMinutes > disponible) {
                issues += ScheduleIssue.TiempoInsuficiente(assignment.activity, disponible)
            }
        }

        // 3) Secuencias poco apropiadas (orden cronologico de bloques asignados).
        val ordenadas = assignments.sortedBy { it.slot.startMinute }
        for (i in 0 until ordenadas.size - 1) {
            val actual = ordenadas[i]
            val siguiente = ordenadas[i + 1]
            val huecoMin = siguiente.slot.startMinute - actual.slot.endMinute
            val ambasAltaEnergia = actual.activity.subject in HIGH_ENERGY &&
                siguiente.activity.subject in HIGH_ENERGY
            if (ambasAltaEnergia && huecoMin < 5 && !siguiente.slot.isRecess && !actual.slot.isRecess) {
                issues += ScheduleIssue.SecuenciaPocoApropiada(siguiente.activity, actual.activity)
            }
        }

        // 4) Actividades planificadas que quedaron sin colocar.
        val colocadasIds = assignments.map { it.activity.activityId }.toSet()
        plannedActivities.filter { it.activityId !in colocadasIds }.forEach {
            issues += ScheduleIssue.SinEspacio(it)
        }

        return ScheduleEvaluation(
            assignments = assignments,
            issues = issues,
            isPlanValido = issues.isEmpty() && assignments.isNotEmpty()
        )
    }
}
