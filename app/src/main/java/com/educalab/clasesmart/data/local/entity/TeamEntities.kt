package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey val teamId: String,
    val activityId: String,
    val name: String,
    val createdAtEpochMs: Long,
    val coverageScore: Int = 0 // calculado por TeamFormationEngine
)

@Entity(
    tableName = "team_member",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["teamId"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StudentCharacterEntity::class,
            parentColumns = ["characterId"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("teamId"), Index("characterId")]
)
data class TeamMemberEntity(
    @PrimaryKey(autoGenerate = true) val memberRowId: Long = 0,
    val teamId: String,
    val characterId: String
)
