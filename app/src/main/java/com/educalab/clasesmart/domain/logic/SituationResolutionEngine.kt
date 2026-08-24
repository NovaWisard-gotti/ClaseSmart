package com.educalab.clasesmart.domain.logic

import com.educalab.clasesmart.domain.model.*

/**
 * Motor de resolucion de situaciones (Modulo 5 y 9).
 * Traduce la eleccion del nino/a en una consecuencia con XP asociado,
 * evitando siempre el par binario "correcto/incorrecto".
 */
object SituationResolutionEngine {

    fun resolve(
        situation: ClassroomSituation,
        chosenOptionId: String,
        outcomesByOption: Map<String, SituationOutcome>
    ): SituationOutcome {
        val chosen = situation.options.firstOrNull { it.optionId == chosenOptionId }
            ?: return SituationOutcome(
                consequenceText = "Todavia no se eligio ninguna opcion para esta situacion.",
                xpAwarded = 0,
                qualityLevel = 0
            )

        return outcomesByOption[chosen.optionId] ?: fallbackOutcome(chosen)
    }

    private fun fallbackOutcome(choice: SituationChoice): SituationOutcome {
        val text = when (choice.qualityLevel) {
            2 -> "Esta opcion ayudo a que todos pudieran participar."
            1 -> "Esta opcion funciono, aunque otra companera todavia necesitaba ayuda."
            else -> "Esta opcion no resolvio la situacion; el grupo sigue teniendo el mismo problema."
        }
        val xp = when (choice.qualityLevel) { 2 -> 15; 1 -> 8; else -> 2 }
        return SituationOutcome(consequenceText = text, xpAwarded = xp, qualityLevel = choice.qualityLevel)
    }
}
