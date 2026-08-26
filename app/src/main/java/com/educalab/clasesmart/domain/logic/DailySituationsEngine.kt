package com.educalab.clasesmart.domain.logic

import com.educalab.clasesmart.domain.model.ClassroomSituation
import com.educalab.clasesmart.domain.model.SituationCategory
import java.util.TimeZone
import kotlin.math.ceil

/**
 * Reparte las situaciones del aula en sesiones diarias: cada dia se
 * desbloquea un grupo fijo (DAILY_SITUATION_COUNT), tomado de una rotacion
 * estable que recorre TODAS las situaciones antes de repetir ninguna. Esto
 * garantiza que una misma situacion no vuelva a aparecer antes de que pase
 * cycleLengthDays() dias (siempre >= 4 mientras haya suficiente contenido).
 */
object DailySituationsEngine {
    const val DAILY_SITUATION_COUNT = 5

    /** Indice de dia (calendario local del dispositivo), estable durante todo el dia real. */
    fun currentDayIndex(nowEpochMs: Long = System.currentTimeMillis()): Long {
        val offsetMs = TimeZone.getDefault().getOffset(nowEpochMs)
        return Math.floorDiv(nowEpochMs + offsetMs, 86_400_000L)
    }

    /** Marca de tiempo UTC del inicio del dia local actual, para filtrar el historial de hoy. */
    fun startOfDayEpochMs(nowEpochMs: Long = System.currentTimeMillis()): Long {
        val offsetMs = TimeZone.getDefault().getOffset(nowEpochMs)
        val localMs = nowEpochMs + offsetMs
        val dayFloorLocalMs = Math.floorDiv(localMs, 86_400_000L) * 86_400_000L
        return dayFloorLocalMs - offsetMs
    }

    /** Orden estable intercalado por categoria, para que cada bloque diario mezcle tipos de situacion. */
    private fun stableRotationOrder(all: List<ClassroomSituation>): List<ClassroomSituation> {
        val byCategory = SituationCategory.values().map { cat ->
            all.filter { it.category == cat }.sortedBy { it.situationId }
        }
        val maxSize = byCategory.maxOfOrNull { it.size } ?: 0
        val result = mutableListOf<ClassroomSituation>()
        for (i in 0 until maxSize) {
            for (group in byCategory) {
                group.getOrNull(i)?.let { result.add(it) }
            }
        }
        return result
    }

    fun situationsForDay(all: List<ClassroomSituation>, dayIndex: Long): List<ClassroomSituation> {
        if (all.isEmpty()) return emptyList()
        val ordered = stableRotationOrder(all)
        val total = ordered.size
        val dailyCount = DAILY_SITUATION_COUNT.coerceAtMost(total)
        val cycleLength = cycleLengthDays(total)
        val dayInCycle = Math.floorMod(dayIndex, cycleLength.toLong()).toInt()
        val startIdx = dayInCycle * dailyCount
        if (startIdx >= total) return emptyList()
        val endIdx = (startIdx + dailyCount).coerceAtMost(total)
        return ordered.subList(startIdx, endIdx)
    }

    /** Cuantos dias reales garantiza el ciclo antes de que una situacion se repita. */
    fun cycleLengthDays(totalSituations: Int): Int =
        ceil(totalSituations.toDouble() / DAILY_SITUATION_COUNT).toInt().coerceAtLeast(1)
}
