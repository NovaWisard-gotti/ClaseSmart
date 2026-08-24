package com.educalab.clasesmart.domain.logic

import com.educalab.clasesmart.domain.model.PlannableActivity

/**
 * Motor de retos de tiempo (Modulo 8 - "Reloj y tiempo").
 * Dado un presupuesto de minutos y una seleccion de tareas, calcula si
 * caben, cuanto sobra/falta y sugiere que quitar si no caben (la tarea
 * mas larga primero, criterio simple y explicable a un nino/a).
 */
object TimeManagementEngine {

    data class TimeChallengeResult(
        val totalNeeded: Int,
        val budgetMinutes: Int,
        val fits: Boolean,
        val remainingMinutes: Int,
        val suggestionToRemove: PlannableActivity?
    )

    fun evaluate(budgetMinutes: Int, chosenTasks: List<PlannableActivity>): TimeChallengeResult {
        val safeBudget = budgetMinutes.coerceAtLeast(0)
        val total = chosenTasks.sumOf { it.durationMinutes.coerceAtLeast(0) }
        val fits = total <= safeBudget
        return TimeChallengeResult(
            totalNeeded = total,
            budgetMinutes = safeBudget,
            fits = fits,
            remainingMinutes = safeBudget - total,
            suggestionToRemove = if (fits) null else chosenTasks.maxByOrNull { it.durationMinutes }
        )
    }
}
