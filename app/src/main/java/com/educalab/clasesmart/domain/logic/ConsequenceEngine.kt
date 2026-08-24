package com.educalab.clasesmart.domain.logic

import com.educalab.clasesmart.domain.model.MaterialIssue
import com.educalab.clasesmart.domain.model.ScheduleIssue

/**
 * Convierte los "issues" tecnicos de los demas motores en texto de
 * consecuencia homogeneo para la UI (Regla 10: nunca solo "correcto/incorrecto").
 */
object ConsequenceEngine {

    fun forSchedule(issues: List<ScheduleIssue>): String {
        if (issues.isEmpty()) return "\u00a1Funciona! El horario deja tiempo suficiente para cada actividad."
        return issues.joinToString(separator = " ") { it.message }
    }

    fun forMaterials(issues: List<MaterialIssue>): String {
        if (issues.isEmpty()) return "\u00a1Todo listo! La mesa de trabajo tiene justo lo que hace falta."
        return issues.joinToString(separator = " ") { it.message }
    }
}
