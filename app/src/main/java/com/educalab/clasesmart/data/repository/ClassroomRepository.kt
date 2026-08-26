package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.*
import com.educalab.clasesmart.data.local.entity.*
import com.educalab.clasesmart.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository que traduce entidades Room <-> modelos de dominio.
 * Los ViewModels y motores de dominio NUNCA ven una Entity directamente.
 */
class ClassroomRepository(
    private val classroomDao: ClassroomDao,
    private val objectDao: ClassroomObjectDao,
    private val decorationDao: ClassroomDecorationDao,
    private val unlockedDecorationDao: UnlockedDecorationDao
) {
    fun observeSnapshot(): Flow<ClassroomSnapshot> =
        classroomDao.observeClassroom().let { classroomFlow ->
            // Se combina de forma simple para mantener el repositorio legible;
            // en un incremento futuro se podria usar combine() de coroutines.
            objectDao.observeObjects().map { objects ->
                ClassroomSnapshot(
                    level = 1,
                    objects = objects.map { it.toDomain() },
                    activeDecorationIds = emptySet()
                )
            }
        }

    suspend fun updateObjectState(objectId: String, state: ModuleState) {
        objectDao.updateState(objectId, state.name)
    }

    suspend fun unlockDecoration(decorationId: String, nowEpochMs: Long) {
        unlockedDecorationDao.insert(
            UnlockedDecorationEntity(decorationId = decorationId, unlockedAtEpochMs = nowEpochMs)
        )
    }

    fun observeUnlockedDecorationIds(): Flow<Set<String>> =
        unlockedDecorationDao.observeUnlocked().map { list -> list.map { it.decorationId }.toSet() }
}

private fun ClassroomObjectEntity.toDomain(): ClassroomObject = ClassroomObject(
    objectId = objectId,
    type = ClassroomObjectType.valueOf(objectType),
    zoneX = zoneX,
    zoneY = zoneY,
    state = ModuleState.valueOf(state),
    unlockLevel = unlockLevel,
    sizeScale = sizeScale
)
