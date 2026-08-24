package com.educalab.clasesmart.data.local.dao

import androidx.room.*
import com.educalab.clasesmart.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Query("SELECT * FROM material")
    fun observeAll(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM material WHERE materialId IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<MaterialEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(materials: List<MaterialEntity>)
}

@Dao
interface MaterialLocationDao {
    @Query("SELECT * FROM material_location")
    fun observeAll(): Flow<List<MaterialLocationEntity>>

    @Query("SELECT * FROM material_location WHERE materialId = :materialId LIMIT 1")
    suspend fun getForMaterial(materialId: String): MaterialLocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<MaterialLocationEntity>)

    @Query("UPDATE material_location SET zone = :zone WHERE materialId = :materialId")
    suspend fun moveMaterial(materialId: String, zone: String)
}
