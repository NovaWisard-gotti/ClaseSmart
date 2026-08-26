package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** El aula del usuario. Una sola fila en la v1.0.0 (un aula por perfil). */
@Entity(tableName = "classroom")
data class ClassroomEntity(
    @PrimaryKey val id: String = "main_classroom",
    val ownerId: String = "local_user",
    val currentLevel: Int = 1,
    val activeThemeId: String = "aula_base",
    val lastVisitedZone: String = "aula_general"
)

/**
 * Objeto interactivo dentro del aula (pizarra, reloj, estante, pupitres...).
 * Cada fila representa UNA instancia colocable con posicion y estado, no
 * un simple icono de menu.
 */
@Entity(
    tableName = "classroom_object",
    foreignKeys = [
        ForeignKey(
            entity = ClassroomEntity::class,
            parentColumns = ["id"],
            childColumns = ["classroomId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("classroomId"), Index("objectType")]
)
data class ClassroomObjectEntity(
    @PrimaryKey val objectId: String,
    val classroomId: String = "main_classroom",
    val objectType: String,   // PIZARRA, RELOJ, ESTANTE, PUPITRES, BIBLIOTECA, MOCHILA, PUERTA, CARTEL...
    val zoneX: Float,
    val zoneY: Float,
    val state: String = "DISPONIBLE", // BLOQUEADO, DISPONIBLE, INICIADO, COMPLETADO, DOMINADO
    val unlockLevel: Int = 1,
    val sizeScale: Float = 1f // escala del tamano base del tipo, para objetos "accesorio" mas pequenos
)

@Entity(tableName = "classroom_decoration")
data class ClassroomDecorationEntity(
    @PrimaryKey val decorationId: String,
    val name: String,
    val category: String, // CARTEL, PLANTA, COLOR_MUEBLE, PIZARRA_BORDE, ESTANTE_ADORNO
    val unlockRequirement: String, // descripcion legible de que la desbloquea
    val requiredXp: Int
)

@Entity(
    tableName = "unlocked_decoration",
    foreignKeys = [
        ForeignKey(
            entity = ClassroomDecorationEntity::class,
            parentColumns = ["decorationId"],
            childColumns = ["decorationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("decorationId")]
)
data class UnlockedDecorationEntity(
    @PrimaryKey(autoGenerate = true) val unlockedId: Long = 0,
    val decorationId: String,
    val classroomId: String = "main_classroom",
    val unlockedAtEpochMs: Long,
    val isActive: Boolean = false
)
