package com.educalab.clasesmart.data.seed

import com.educalab.clasesmart.data.local.AppDatabase
import com.educalab.clasesmart.data.local.entity.ClassroomEntity
import com.educalab.clasesmart.data.local.entity.UserProgressEntity

/**
 * Sembrado inicial de la base de datos, ejecutado UNA sola vez cuando se
 * crea fisicamente el fichero clasesmart.db (ver AppDatabase.Callback.onCreate).
 *
 * Sin este sembrado, la app instalada estaria vacia y no cumpliria la
 * Regla 24 del master spec ("Nunca entregar una aplicacion... solo porque
 * tecnologicamente funciona"). El resumen exacto de cantidades sembradas
 * (y las reducciones respecto a los minimos sugeridos por la especificacion)
 * esta documentado en docs/BUILD_REPORT.md.
 */
object SeedDataProvider {

    suspend fun seed(db: AppDatabase) {
        db.classroomDao().upsert(ClassroomEntity())
        db.userProgressDao().upsert(UserProgressEntity())

        db.classroomObjectDao().insertAll(SeedClassroomObjects.objects)
        db.classroomDecorationDao().insertAll(SeedClassroomObjects.decorations)

        db.studentCharacterDao().insertAll(SeedCharacters.characters)
        db.characterSkillDao().insertAll(SeedCharacters.skills)

        db.materialDao().insertAll(SeedMaterials.materials)
        db.materialLocationDao().insertAll(SeedMaterials.locations)

        db.badgeDao().insertAll(SeedBadges.badges)

        db.classActivityDao().insertAll(SeedActivitiesAndSchedule.activities)
        db.activityStepDao().insertAll(SeedActivitiesAndSchedule.steps)
        db.timeBlockDao().insertAll(SeedActivitiesAndSchedule.timeBlocks)

        db.classroomSituationDao().insertAll(SeedSituationsConvivenciaOrganizacion.situations)
        db.situationOptionDao().insertAll(SeedSituationsConvivenciaOrganizacion.options)
        db.situationOutcomeDao().insertAll(SeedSituationsConvivenciaOrganizacion.outcomes)

        db.classroomSituationDao().insertAll(SeedSituationsTiempoMateriales.situations)
        db.situationOptionDao().insertAll(SeedSituationsTiempoMateriales.options)
        db.situationOutcomeDao().insertAll(SeedSituationsTiempoMateriales.outcomes)

        db.classroomSituationDao().insertAll(SeedSituationsEquipoPlanificacion.situations)
        db.situationOptionDao().insertAll(SeedSituationsEquipoPlanificacion.options)
        db.situationOutcomeDao().insertAll(SeedSituationsEquipoPlanificacion.outcomes)

        db.projectDao().insertAll(SeedProjects.projects)
        db.projectTaskDao().insertAll(SeedProjects.tasks)
    }
}
