package com.educalab.clasesmart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Perfil local del niño/a. NUNCA contiene datos personales reales:
 * ni nombre real, ni email, ni telefono. Solo alias + avatar local.
 */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "local_user",
    val alias: String,
    val avatarId: String,
    val ageBand: String, // "8-9" o "10-12"
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = true,
    val createdAtEpochMs: Long,
    val lastOpenedEpochMs: Long,
    val onboardingCompleted: Boolean = false
)

/**
 * Progreso global del usuario: XP, nivel del aula, racha de sesiones.
 * Derivado exclusivamente de acciones reales registradas en InteractionHistory.
 */
@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: String = "local_user",
    val totalXp: Int = 0,
    val aulaLevel: Int = 1,
    val sessionsCount: Int = 0,
    val situationsResolved: Int = 0,
    val projectsCompleted: Int = 0,
    val lastSessionEpochMs: Long = 0L
)

/**
 * Historial de interacciones reales (auditable). Toda insignia, XP o
 * desbloqueo debe poder trazarse a una fila de esta tabla: nada se
 * regala sin una accion real detras.
 */
@Entity(tableName = "interaction_history")
data class InteractionHistoryEntity(
    @PrimaryKey(autoGenerate = true) val historyId: Long = 0,
    val kind: String,        // ej: "SITUATION_RESOLVED", "SCHEDULE_SUBMITTED"
    val referenceId: String, // id de la entidad relacionada (situacion, proyecto...)
    val xpAwarded: Int,
    val wasSuccessful: Boolean,
    val timestampEpochMs: Long,
    val detail: String = ""
)
