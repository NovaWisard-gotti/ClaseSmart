package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "badge")
data class BadgeEntity(
    @PrimaryKey val badgeId: String,
    val name: String,          // "Gran Organizador", "Maestro del Tiempo"...
    val description: String,
    val iconAssetId: String,
    val skillArea: String      // ORGANIZACION, TIEMPO, EQUIPO, MATERIALES, CONVIVENCIA, COMUNICACION, PROYECTO
)

@Entity(
    tableName = "user_badge",
    foreignKeys = [
        ForeignKey(
            entity = BadgeEntity::class,
            parentColumns = ["badgeId"],
            childColumns = ["badgeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("badgeId")]
)
data class UserBadgeEntity(
    @PrimaryKey(autoGenerate = true) val userBadgeRowId: Long = 0,
    val badgeId: String,
    val earnedAtEpochMs: Long
)
