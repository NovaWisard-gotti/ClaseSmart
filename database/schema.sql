-- ============================================================
-- ClaseSmart - database/schema.sql
-- Esquema SQLite generado a partir de las entidades Room reales
-- de app/src/main/java/com/educalab/clasesmart/data/local/entity/
-- (version de esquema Room: 1)
-- ============================================================

PRAGMA foreign_keys = ON;

-- ---------- Usuario y progreso ----------

CREATE TABLE IF NOT EXISTS user_profile (
    id TEXT NOT NULL PRIMARY KEY,
    alias TEXT NOT NULL,
    avatarId TEXT NOT NULL,
    ageBand TEXT NOT NULL,
    soundEnabled INTEGER NOT NULL DEFAULT 1,
    hapticEnabled INTEGER NOT NULL DEFAULT 1,
    createdAtEpochMs INTEGER NOT NULL,
    lastOpenedEpochMs INTEGER NOT NULL,
    onboardingCompleted INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS user_progress (
    id TEXT NOT NULL PRIMARY KEY,
    totalXp INTEGER NOT NULL DEFAULT 0,
    aulaLevel INTEGER NOT NULL DEFAULT 1,
    sessionsCount INTEGER NOT NULL DEFAULT 0,
    situationsResolved INTEGER NOT NULL DEFAULT 0,
    projectsCompleted INTEGER NOT NULL DEFAULT 0,
    lastSessionEpochMs INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS interaction_history (
    historyId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    kind TEXT NOT NULL,
    referenceId TEXT NOT NULL,
    xpAwarded INTEGER NOT NULL,
    wasSuccessful INTEGER NOT NULL,
    timestampEpochMs INTEGER NOT NULL,
    detail TEXT NOT NULL DEFAULT ''
);

-- ---------- Aula ----------

CREATE TABLE IF NOT EXISTS classroom (
    id TEXT NOT NULL PRIMARY KEY,
    ownerId TEXT NOT NULL DEFAULT 'local_user',
    currentLevel INTEGER NOT NULL DEFAULT 1,
    activeThemeId TEXT NOT NULL DEFAULT 'aula_base',
    lastVisitedZone TEXT NOT NULL DEFAULT 'aula_general'
);

CREATE TABLE IF NOT EXISTS classroom_object (
    objectId TEXT NOT NULL PRIMARY KEY,
    classroomId TEXT NOT NULL DEFAULT 'main_classroom',
    objectType TEXT NOT NULL,
    zoneX REAL NOT NULL,
    zoneY REAL NOT NULL,
    state TEXT NOT NULL DEFAULT 'DISPONIBLE',
    unlockLevel INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (classroomId) REFERENCES classroom(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_classroom_object_classroomId ON classroom_object(classroomId);
CREATE INDEX IF NOT EXISTS index_classroom_object_objectType ON classroom_object(objectType);

CREATE TABLE IF NOT EXISTS classroom_decoration (
    decorationId TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    unlockRequirement TEXT NOT NULL,
    requiredXp INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS unlocked_decoration (
    unlockedId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    decorationId TEXT NOT NULL,
    classroomId TEXT NOT NULL DEFAULT 'main_classroom',
    unlockedAtEpochMs INTEGER NOT NULL,
    isActive INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (decorationId) REFERENCES classroom_decoration(decorationId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_unlocked_decoration_decorationId ON unlocked_decoration(decorationId);

-- ---------- Personajes ----------

CREATE TABLE IF NOT EXISTS student_character (
    characterId TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    trait TEXT NOT NULL,
    spriteBaseId TEXT NOT NULL,
    defaultExpression TEXT NOT NULL DEFAULT 'FELIZ',
    isMainCast INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS character_skill (
    skillRowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    characterId TEXT NOT NULL,
    skill TEXT NOT NULL,
    strength INTEGER NOT NULL,
    FOREIGN KEY (characterId) REFERENCES student_character(characterId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_character_skill_characterId ON character_skill(characterId);

-- ---------- Actividades ----------

CREATE TABLE IF NOT EXISTS class_activity (
    activityId TEXT NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    subject TEXT NOT NULL,
    durationMinutes INTEGER NOT NULL,
    minAgeBand TEXT NOT NULL DEFAULT '8-9',
    requiredMaterialIds TEXT NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS activity_step (
    stepId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    activityId TEXT NOT NULL,
    orderIndex INTEGER NOT NULL,
    instruction TEXT NOT NULL,
    FOREIGN KEY (activityId) REFERENCES class_activity(activityId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_activity_step_activityId ON activity_step(activityId);

CREATE TABLE IF NOT EXISTS activity_attempt (
    attemptId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    activityId TEXT NOT NULL,
    wasSuccessful INTEGER NOT NULL,
    timestampEpochMs INTEGER NOT NULL,
    notes TEXT NOT NULL DEFAULT '',
    FOREIGN KEY (activityId) REFERENCES class_activity(activityId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_activity_attempt_activityId ON activity_attempt(activityId);

-- ---------- Materiales ----------

CREATE TABLE IF NOT EXISTS material (
    materialId TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    iconAssetId TEXT NOT NULL,
    isFragile INTEGER NOT NULL DEFAULT 0,
    isShared INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS material_location (
    locationRowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    materialId TEXT NOT NULL,
    zone TEXT NOT NULL,
    distanceUnits INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (materialId) REFERENCES material(materialId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_material_location_materialId ON material_location(materialId);

-- ---------- Equipos ----------

CREATE TABLE IF NOT EXISTS team (
    teamId TEXT NOT NULL PRIMARY KEY,
    activityId TEXT NOT NULL,
    name TEXT NOT NULL,
    createdAtEpochMs INTEGER NOT NULL,
    coverageScore INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS team_member (
    memberRowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    teamId TEXT NOT NULL,
    characterId TEXT NOT NULL,
    FOREIGN KEY (teamId) REFERENCES team(teamId) ON DELETE CASCADE,
    FOREIGN KEY (characterId) REFERENCES student_character(characterId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_team_member_teamId ON team_member(teamId);
CREATE INDEX IF NOT EXISTS index_team_member_characterId ON team_member(characterId);

-- ---------- Horario ----------

CREATE TABLE IF NOT EXISTS time_block (
    timeBlockId TEXT NOT NULL PRIMARY KEY,
    dayLabel TEXT NOT NULL,
    startMinute INTEGER NOT NULL,
    endMinute INTEGER NOT NULL,
    isRecess INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS schedule_activity (
    scheduleRowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    timeBlockId TEXT NOT NULL,
    activityId TEXT NOT NULL,
    planDate TEXT NOT NULL,
    FOREIGN KEY (timeBlockId) REFERENCES time_block(timeBlockId) ON DELETE CASCADE,
    FOREIGN KEY (activityId) REFERENCES class_activity(activityId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_schedule_activity_timeBlockId ON schedule_activity(timeBlockId);
CREATE INDEX IF NOT EXISTS index_schedule_activity_activityId ON schedule_activity(activityId);

-- ---------- Situaciones ----------

CREATE TABLE IF NOT EXISTS classroom_situation (
    situationId TEXT NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    sceneDescription TEXT NOT NULL,
    category TEXT NOT NULL,
    minAgeBand TEXT NOT NULL DEFAULT '8-9',
    involvedCharacterIds TEXT NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS situation_option (
    optionId TEXT NOT NULL PRIMARY KEY,
    situationId TEXT NOT NULL,
    actionLabel TEXT NOT NULL,
    qualityLevel INTEGER NOT NULL,
    FOREIGN KEY (situationId) REFERENCES classroom_situation(situationId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_situation_option_situationId ON situation_option(situationId);

CREATE TABLE IF NOT EXISTS situation_outcome (
    outcomeId TEXT NOT NULL PRIMARY KEY,
    optionId TEXT NOT NULL,
    consequenceText TEXT NOT NULL,
    xpAwarded INTEGER NOT NULL,
    FOREIGN KEY (optionId) REFERENCES situation_option(optionId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_situation_outcome_optionId ON situation_outcome(optionId);

-- ---------- Proyectos ----------

CREATE TABLE IF NOT EXISTS project (
    projectId TEXT NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    minAgeBand TEXT NOT NULL DEFAULT '10-12'
);

CREATE TABLE IF NOT EXISTS project_task (
    taskId TEXT NOT NULL PRIMARY KEY,
    projectId TEXT NOT NULL,
    orderIndex INTEGER NOT NULL,
    taskType TEXT NOT NULL,
    label TEXT NOT NULL,
    FOREIGN KEY (projectId) REFERENCES project(projectId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_project_task_projectId ON project_task(projectId);

CREATE TABLE IF NOT EXISTS project_progress (
    projectId TEXT NOT NULL PRIMARY KEY,
    completedTaskIds TEXT NOT NULL DEFAULT '',
    visualState TEXT NOT NULL DEFAULT 'INICIAL',
    lastUpdatedEpochMs INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (projectId) REFERENCES project(projectId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_project_progress_projectId ON project_progress(projectId);

-- ---------- Insignias ----------

CREATE TABLE IF NOT EXISTS badge (
    badgeId TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    iconAssetId TEXT NOT NULL,
    skillArea TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_badge (
    userBadgeRowId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    badgeId TEXT NOT NULL,
    earnedAtEpochMs INTEGER NOT NULL,
    FOREIGN KEY (badgeId) REFERENCES badge(badgeId) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_user_badge_badgeId ON user_badge(badgeId);

-- ============================================================
-- Consultas importantes de referencia (usadas conceptualmente
-- por los repositorios de app/src/main/.../data/repository/)
-- ============================================================

-- Objetos visibles del aula actual:
-- SELECT * FROM classroom_object WHERE classroomId = 'main_classroom';

-- Insignias obtenidas con su fecha, mas recientes primero:
-- SELECT b.name, ub.earnedAtEpochMs
-- FROM user_badge ub JOIN badge b ON b.badgeId = ub.badgeId
-- ORDER BY ub.earnedAtEpochMs DESC;

-- Historial de exito por tipo de interaccion (para BadgeEngine):
-- SELECT kind, COUNT(*) FROM interaction_history
-- WHERE wasSuccessful = 1 GROUP BY kind;

-- Situaciones de una categoria con sus opciones:
-- SELECT s.title, o.actionLabel, o.qualityLevel
-- FROM classroom_situation s JOIN situation_option o ON o.situationId = s.situationId
-- WHERE s.category = 'CONVIVENCIA';
