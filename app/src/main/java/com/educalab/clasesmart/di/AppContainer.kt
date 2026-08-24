package com.educalab.clasesmart.di

import android.content.Context
import com.educalab.clasesmart.data.local.AppDatabase
import com.educalab.clasesmart.data.repository.*

/**
 * Contenedor de dependencias manual (sin Hilt/Koin): el proyecto es
 * pequeno y offline, y un contenedor manual mantiene el arranque
 * transparente y facil de auditar para la documentacion tecnica.
 */
class AppContainer(context: Context) {
    private val db = AppDatabase.getInstance(context)

    val classroomRepository = ClassroomRepository(db.classroomDao(), db.classroomObjectDao(), db.classroomDecorationDao(), db.unlockedDecorationDao())
    val scheduleRepository = ScheduleRepository(db.timeBlockDao(), db.scheduleActivityDao(), db.classActivityDao())
    val materialRepository = MaterialRepository(db.materialDao(), db.materialLocationDao())
    val characterRepository = CharacterRepository(db.studentCharacterDao(), db.characterSkillDao())
    val situationRepository = SituationRepository(db.classroomSituationDao(), db.situationOptionDao(), db.situationOutcomeDao())
    val progressRepository = ProgressRepository(db.userProgressDao(), db.interactionHistoryDao())
    val teamRepository = TeamRepository(db.teamDao(), db.teamMemberDao())
    val projectRepository = ProjectRepository(db.projectDao(), db.projectTaskDao(), db.projectProgressDao())
    val badgeRepository = BadgeRepository(db.badgeDao(), db.userBadgeDao())

    val userProfileDao = db.userProfileDao()
    val timeBlockDao = db.timeBlockDao()
    val classActivityDao = db.classActivityDao()
    val badgeDao = db.badgeDao()
}
