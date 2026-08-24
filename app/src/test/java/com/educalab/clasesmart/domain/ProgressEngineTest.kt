package com.educalab.clasesmart.domain

import com.educalab.clasesmart.domain.logic.ProgressEngine
import com.educalab.clasesmart.domain.model.ClassroomObjectType
import org.junit.Assert.*
import org.junit.Test

class ProgressEngineTest {

    @Test
    fun `nivel 1 para xp cero`() {
        assertEquals(1, ProgressEngine.levelForXp(0))
    }

    @Test
    fun `xp negativo se trata como cero`() {
        assertEquals(1, ProgressEngine.levelForXp(-50))
    }

    @Test
    fun `sube de nivel al cruzar un umbral`() {
        assertEquals(2, ProgressEngine.levelForXp(60))
        assertEquals(1, ProgressEngine.levelForXp(59))
    }

    @Test
    fun `xp muy alto no supera el ultimo nivel definido`() {
        assertEquals(6, ProgressEngine.levelForXp(999_999))
    }

    @Test
    fun `applyXp detecta subida de nivel y desbloqueos nuevos`() {
        val result = ProgressEngine.applyXp(previousXp = 55, xpGained = 10)
        assertTrue(result.leveledUp)
        assertEquals(2, result.newLevel)
        assertTrue(result.newlyUnlockedObjectTypes.contains(ClassroomObjectType.PUPITRES))
    }

    @Test
    fun `applyXp sin cruzar umbral no marca leveledUp`() {
        val result = ProgressEngine.applyXp(previousXp = 0, xpGained = 10)
        assertFalse(result.leveledUp)
        assertTrue(result.newlyUnlockedObjectTypes.isEmpty())
    }

    @Test
    fun `applyXp con ganancia negativa no revienta ni resta xp`() {
        val result = ProgressEngine.applyXp(previousXp = 10, xpGained = -100)
        assertFalse(result.leveledUp)
        assertEquals(1, result.newLevel)
    }

    @Test
    fun `subir varios niveles de golpe acumula todos los desbloqueos intermedios`() {
        // Partir de xp=0 ya corresponde al nivel 1 (PIZARRA ya estaba disponible desde el inicio,
        // por lo que NO debe listarse como "nuevo" desbloqueo). Se comprueba que se acumulan
        // todos los desbloqueos de los niveles intermedios realmente cruzados: 2, 3 y 4.
        val result = ProgressEngine.applyXp(previousXp = 0, xpGained = 300)
        assertEquals(4, result.newLevel)
        assertFalse(result.newlyUnlockedObjectTypes.contains(ClassroomObjectType.PIZARRA))
        assertTrue(result.newlyUnlockedObjectTypes.contains(ClassroomObjectType.PUPITRES))
        assertTrue(result.newlyUnlockedObjectTypes.contains(ClassroomObjectType.BIBLIOTECA))
        assertTrue(result.newlyUnlockedObjectTypes.contains(ClassroomObjectType.MOCHILA))
    }
}
