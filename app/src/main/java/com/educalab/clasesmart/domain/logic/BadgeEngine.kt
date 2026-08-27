package com.educalab.clasesmart.domain.logic

/**
 * Motor de insignias. Cada insignia se desbloquea por una condicion sobre
 * estadisticas REALES acumuladas (no por decision aleatoria ni evaluacion
 * psicologica del nino/a).
 */
object BadgeEngine {

    data class UserStats(
        val schedulesWithoutIssues: Int = 0,
        val materialMissionsReady: Int = 0,
        val teamsWithFullCoverage: Int = 0,
        val situationsHighQuality: Int = 0,
        val convivenciaSituationsResolved: Int = 0,
        val projectsCompleted: Int = 0,
        val decorationsUnlocked: Int = 0,
        val ideasBoardsCreated: Int = 0,
        val libraryPerfectRounds: Int = 0,
        val aulaCareCompleted: Int = 0
    )

    data class BadgeRule(val badgeId: String, val condition: (UserStats) -> Boolean)

    val rules: List<BadgeRule> = listOf(
        BadgeRule("gran_organizador") { it.schedulesWithoutIssues >= 3 },
        BadgeRule("maestro_del_tiempo") { it.schedulesWithoutIssues >= 5 },
        BadgeRule("equipo_imparable") { it.teamsWithFullCoverage >= 3 },
        BadgeRule("guardian_de_materiales") { it.materialMissionsReady >= 5 },
        BadgeRule("solucionador_de_problemas") { it.situationsHighQuality >= 5 },
        BadgeRule("gran_comunicador") { it.convivenciaSituationsResolved >= 5 },
        BadgeRule("constructor_de_ideas") { it.ideasBoardsCreated >= 3 },
        BadgeRule("ayudante_del_aula") { it.situationsHighQuality >= 10 },
        BadgeRule("planificador_creativo") { it.schedulesWithoutIssues >= 8 },
        BadgeRule("experto_en_colaboracion") { it.teamsWithFullCoverage >= 6 },
        BadgeRule("cuidador_del_espacio") { it.decorationsUnlocked >= 5 },
        BadgeRule("lider_de_proyecto") { it.projectsCompleted >= 1 },
        BadgeRule("bibliotecario_experto") { it.libraryPerfectRounds >= 3 },
        BadgeRule("guardian_del_aula") { it.aulaCareCompleted >= 3 }
    )

    /** Devuelve los ids de insignia que se cumplen ahora mismo y todavia no se tenian. */
    fun evaluateNewlyEarned(stats: UserStats, alreadyEarnedIds: Set<String>): List<String> =
        rules.filter { it.badgeId !in alreadyEarnedIds && it.condition(stats) }.map { it.badgeId }
}
