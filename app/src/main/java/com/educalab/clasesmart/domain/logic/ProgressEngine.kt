package com.educalab.clasesmart.domain.logic

import com.educalab.clasesmart.domain.model.ClassroomObjectType
import com.educalab.clasesmart.domain.model.LevelUpResult
import kotlin.math.max

/**
 * Motor de progreso general del aula. Convierte XP acumulado (siempre
 * derivado de InteractionHistory, nunca inventado) en nivel del aula y
 * en desbloqueos de nuevas zonas/objetos.
 */
object ProgressEngine {

    // Umbrales de XP por nivel: crecimiento suave, pensado para sesiones de 5-20 min.
    private val LEVEL_THRESHOLDS = listOf(0, 60, 150, 280, 450, 660)

    private val UNLOCKS_BY_LEVEL: Map<Int, List<ClassroomObjectType>> = mapOf(
        1 to listOf(ClassroomObjectType.PIZARRA, ClassroomObjectType.RELOJ, ClassroomObjectType.ESTANTE),
        2 to listOf(ClassroomObjectType.PUPITRES),
        3 to listOf(ClassroomObjectType.BIBLIOTECA),
        4 to listOf(ClassroomObjectType.MOCHILA, ClassroomObjectType.CARTEL),
        5 to listOf(ClassroomObjectType.PUERTA),
        6 to listOf(ClassroomObjectType.PLANTA)
    )

    fun levelForXp(totalXp: Int): Int {
        val safeXp = max(0, totalXp)
        var level = 1
        for (i in LEVEL_THRESHOLDS.indices) {
            if (safeXp >= LEVEL_THRESHOLDS[i]) level = i + 1
        }
        return level.coerceAtMost(LEVEL_THRESHOLDS.size)
    }

    fun xpForNextLevel(currentLevel: Int): Int? =
        LEVEL_THRESHOLDS.getOrNull(currentLevel) // indice = nivel actual -> umbral del siguiente

    fun applyXp(previousXp: Int, xpGained: Int): LevelUpResult {
        val safePreviousXp = max(0, previousXp)
        val safeGain = max(0, xpGained)
        val previousLevel = levelForXp(safePreviousXp)
        val newLevel = levelForXp(safePreviousXp + safeGain)
        val newlyUnlocked = if (newLevel > previousLevel) {
            (previousLevel + 1..newLevel).flatMap { UNLOCKS_BY_LEVEL[it].orEmpty() }
        } else emptyList()
        return LevelUpResult(
            leveledUp = newLevel > previousLevel,
            newLevel = newLevel,
            newlyUnlockedObjectTypes = newlyUnlocked
        )
    }
}
