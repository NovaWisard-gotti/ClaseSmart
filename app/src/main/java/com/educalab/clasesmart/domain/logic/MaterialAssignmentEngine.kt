package com.educalab.clasesmart.domain.logic

import com.educalab.clasesmart.domain.model.*

/**
 * Motor de preparacion de materiales (Modulo 3 - "Mision de materiales").
 * Evalua si los materiales colocados en la mesa de trabajo cubren lo que
 * requiere la mision, y detecta materiales fuera de lugar o innecesarios.
 */
object MaterialAssignmentEngine {

    private const val DISTANCIA_LEJOS = 3

    fun evaluate(
        mission: MaterialMission,
        allMaterials: List<SchoolMaterial>,
        placedOnTable: List<SchoolMaterial>
    ): MaterialEvaluation {
        val issues = mutableListOf<MaterialIssue>()
        val placedIds = placedOnTable.map { it.materialId }.toSet()
        val byId = allMaterials.associateBy { it.materialId }

        // 1) Materiales requeridos que faltan sobre la mesa.
        mission.requiredMaterialIds.distinct().forEach { requiredId ->
            if (requiredId !in placedIds) {
                val name = byId[requiredId]?.name ?: requiredId
                issues += MaterialIssue.Falta(name)
            }
        }

        // 2) Materiales requeridos que vinieron desde muy lejos.
        placedOnTable.filter { it.materialId in mission.requiredMaterialIds }.forEach { material ->
            if (material.distanceUnits >= DISTANCIA_LEJOS) {
                issues += MaterialIssue.DemasiadoLejos(material.name, material.distanceUnits)
            }
        }

        // 3) Materiales sobre la mesa que no hacen falta para esta mision.
        placedOnTable.filter { it.materialId !in mission.requiredMaterialIds }.forEach {
            issues += MaterialIssue.ExtraSinUso(it.name)
        }

        val requiredPresent = mission.requiredMaterialIds.isNotEmpty() &&
            mission.requiredMaterialIds.all { it in placedIds }

        return MaterialEvaluation(
            placedOnTable = placedOnTable,
            issues = issues,
            isReady = requiredPresent && issues.none { it is MaterialIssue.Falta }
        )
    }
}
