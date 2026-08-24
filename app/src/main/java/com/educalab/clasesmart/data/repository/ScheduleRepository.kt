package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.*
import com.educalab.clasesmart.data.local.entity.*
import com.educalab.clasesmart.domain.model.*

class ScheduleRepository(
    private val timeBlockDao: TimeBlockDao,
    private val scheduleActivityDao: ScheduleActivityDao,
    private val classActivityDao: ClassActivityDao
) {
    suspend fun getAvailableSlots(): List<TimeSlot> =
        // observeAll() es Flow; para una lectura puntual se usa first() en el caller.
        emptyList() // sustituido por uso real desde el UseCase con Flow.first()

    suspend fun saveAssignments(date: String, assignments: List<ScheduleAssignment>) {
        scheduleActivityDao.clearDate(date)
        scheduleActivityDao.insertAll(
            assignments.map {
                ScheduleActivityEntity(
                    timeBlockId = it.slot.timeBlockId,
                    activityId = it.activity.activityId,
                    planDate = date
                )
            }
        )
    }
}

fun TimeBlockEntity.toDomain(): TimeSlot = TimeSlot(timeBlockId, startMinute, endMinute, isRecess)

fun ClassActivityEntity.toDomain(): PlannableActivity = PlannableActivity(
    activityId = activityId,
    title = title,
    subject = Subject.valueOf(subject),
    durationMinutes = durationMinutes,
    requiredMaterialIds = if (requiredMaterialIds.isBlank()) emptyList() else requiredMaterialIds.split(",")
)
