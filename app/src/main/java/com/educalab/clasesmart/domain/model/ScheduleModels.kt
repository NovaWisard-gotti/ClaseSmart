package com.educalab.clasesmart.domain.model

data class TimeSlot(
    val timeBlockId: String,
    val startMinute: Int,
    val endMinute: Int,
    val isRecess: Boolean = false
) {
    val durationMinutes: Int get() = endMinute - startMinute
    fun overlaps(other: TimeSlot): Boolean = startMinute < other.endMinute && other.startMinute < endMinute
}

enum class Subject {
    LECTURA, RECREO, CIENCIAS, ARTE, MATEMATICAS, TRABAJO_GRUPAL, LIMPIEZA, EXPOSICION
}

data class PlannableActivity(
    val activityId: String,
    val title: String,
    val subject: Subject,
    val durationMinutes: Int,
    val requiredMaterialIds: List<String> = emptyList()
)

data class ScheduleAssignment(val slot: TimeSlot, val activity: PlannableActivity)

/** Un problema detectado por el motor de organizacion horaria. */
sealed class ScheduleIssue(val message: String) {
    data class Solapamiento(val a: PlannableActivity, val b: PlannableActivity) :
        ScheduleIssue("\"${a.title}\" y \"${b.title}\" quedaron en el mismo horario.")
    data class TiempoInsuficiente(val activity: PlannableActivity, val disponible: Int) :
        ScheduleIssue("\"${activity.title}\" necesita ${activity.durationMinutes} min, pero el bloque solo tiene $disponible.")
    data class SecuenciaPocoApropiada(val despues: PlannableActivity, val antes: PlannableActivity) :
        ScheduleIssue("Poner \"${despues.title}\" justo despues de \"${antes.title}\" deja poco tiempo para que el grupo cambie de ritmo.")
    data class SinEspacio(val activity: PlannableActivity) :
        ScheduleIssue("\"${activity.title}\" todavia no tiene un horario asignado.")
}

data class ScheduleEvaluation(
    val assignments: List<ScheduleAssignment>,
    val issues: List<ScheduleIssue>,
    val isPlanValido: Boolean
)
