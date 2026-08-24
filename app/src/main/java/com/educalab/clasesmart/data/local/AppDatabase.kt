package com.educalab.clasesmart.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.educalab.clasesmart.data.local.converters.Converters
import com.educalab.clasesmart.data.local.dao.*
import com.educalab.clasesmart.data.local.entity.*
import com.educalab.clasesmart.data.seed.SeedDataProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de datos Room de ClaseSmart. 100% local, sin sincronizacion remota.
 * Version 1: esquema inicial documentado en database/schema.sql.
 */
@Database(
    entities = [
        UserProfileEntity::class,
        UserProgressEntity::class,
        InteractionHistoryEntity::class,
        ClassroomEntity::class,
        ClassroomObjectEntity::class,
        ClassroomDecorationEntity::class,
        UnlockedDecorationEntity::class,
        StudentCharacterEntity::class,
        CharacterSkillEntity::class,
        ClassActivityEntity::class,
        ActivityStepEntity::class,
        ActivityAttemptEntity::class,
        MaterialEntity::class,
        MaterialLocationEntity::class,
        TeamEntity::class,
        TeamMemberEntity::class,
        TimeBlockEntity::class,
        ScheduleActivityEntity::class,
        ClassroomSituationEntity::class,
        SituationOptionEntity::class,
        SituationOutcomeEntity::class,
        ProjectEntity::class,
        ProjectTaskEntity::class,
        ProjectProgressEntity::class,
        BadgeEntity::class,
        UserBadgeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun interactionHistoryDao(): InteractionHistoryDao
    abstract fun classroomDao(): ClassroomDao
    abstract fun classroomObjectDao(): ClassroomObjectDao
    abstract fun classroomDecorationDao(): ClassroomDecorationDao
    abstract fun unlockedDecorationDao(): UnlockedDecorationDao
    abstract fun studentCharacterDao(): StudentCharacterDao
    abstract fun characterSkillDao(): CharacterSkillDao
    abstract fun classActivityDao(): ClassActivityDao
    abstract fun activityStepDao(): ActivityStepDao
    abstract fun activityAttemptDao(): ActivityAttemptDao
    abstract fun materialDao(): MaterialDao
    abstract fun materialLocationDao(): MaterialLocationDao
    abstract fun teamDao(): TeamDao
    abstract fun teamMemberDao(): TeamMemberDao
    abstract fun timeBlockDao(): TimeBlockDao
    abstract fun scheduleActivityDao(): ScheduleActivityDao
    abstract fun classroomSituationDao(): ClassroomSituationDao
    abstract fun situationOptionDao(): SituationOptionDao
    abstract fun situationOutcomeDao(): SituationOutcomeDao
    abstract fun projectDao(): ProjectDao
    abstract fun projectTaskDao(): ProjectTaskDao
    abstract fun projectProgressDao(): ProjectProgressDao
    abstract fun badgeDao(): BadgeDao
    abstract fun userBadgeDao(): UserBadgeDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: build(context).also { instance = it }
            }

        private fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "clasesmart.db"
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Sembrado inicial: se ejecuta UNA sola vez, en la creacion
                    // fisica del fichero .db (ver docs/BASE_DE_DATOS.md).
                    CoroutineScope(Dispatchers.IO).launch {
                        instance?.let { SeedDataProvider.seed(it) }
                    }
                }
            }).build()
        }
    }
}
