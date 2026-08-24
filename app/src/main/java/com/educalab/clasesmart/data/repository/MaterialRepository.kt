package com.educalab.clasesmart.data.repository

import com.educalab.clasesmart.data.local.dao.MaterialDao
import com.educalab.clasesmart.data.local.dao.MaterialLocationDao
import com.educalab.clasesmart.data.local.entity.MaterialEntity
import com.educalab.clasesmart.domain.model.MaterialCategory
import com.educalab.clasesmart.domain.model.MaterialZone
import com.educalab.clasesmart.domain.model.SchoolMaterial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class MaterialRepository(
    private val materialDao: MaterialDao,
    private val locationDao: MaterialLocationDao
) {
    suspend fun getAllWithLocation(): List<SchoolMaterial> {
        val materials = materialDao.observeAll().first()
        val locations = locationDao.observeAll().first().associateBy { it.materialId }
        return materials.map { m ->
            val loc = locations[m.materialId]
            SchoolMaterial(
                materialId = m.materialId,
                name = m.name,
                category = MaterialCategory.valueOf(m.category),
                isFragile = m.isFragile,
                currentZone = loc?.zone?.let { MaterialZone.valueOf(it) } ?: MaterialZone.ESTANTE,
                distanceUnits = loc?.distanceUnits ?: 1
            )
        }
    }

    suspend fun moveMaterial(materialId: String, zone: MaterialZone) {
        locationDao.moveMaterial(materialId, zone.name)
    }
}
