package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.BadgeRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.domain.logic.BadgeEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LibraryResource(val id: String, val name: String, val correctCategory: String)

private val CATEGORIES = listOf("Leer", "Investigar", "Crear", "Construir", "Comunicar", "Organizar")
private val RESOURCE_POOL = listOf(
    LibraryResource("r1", "Libro de cuentos", "Leer"),
    LibraryResource("r2", "Lupa de observacion", "Investigar"),
    LibraryResource("r3", "Caja de pinturas", "Crear"),
    LibraryResource("r4", "Bloques de madera", "Construir"),
    LibraryResource("r5", "Mapa del aula", "Comunicar"),
    LibraryResource("r6", "Carpeta clasificadora", "Organizar"),
    LibraryResource("r7", "Diccionario ilustrado", "Leer"),
    LibraryResource("r8", "Cuaderno de campo", "Investigar"),
    LibraryResource("r9", "Plastilina", "Crear"),
    LibraryResource("r10", "Piezas de construccion", "Construir"),
    LibraryResource("r11", "Cartel de anuncios", "Comunicar"),
    LibraryResource("r12", "Etiquetas de colores", "Organizar")
)

data class BibliotecaUiState(
    val categories: List<String> = CATEGORIES,
    val resources: List<LibraryResource> = RESOURCE_POOL.shuffled().take(6),
    val currentIndex: Int = 0,
    val feedback: String? = null,
    val correctCount: Int = 0,
    val completionMessage: String? = null
) {
    val current: LibraryResource? get() = resources.getOrNull(currentIndex)
}

/**
 * Modulo 7 - "Biblioteca del aula". Antes esta pantalla no tenia ViewModel
 * ni otorgaba XP: ahora cada clasificacion suma XP real y terminar la ronda
 * muestra una felicitacion y vuelve al aula, igual que el resto de modulos.
 * Cada visita sortea un subconjunto distinto de recursos para que no sea
 * siempre la misma ronda.
 */
class BibliotecaViewModel(
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BibliotecaUiState())
    val uiState: StateFlow<BibliotecaUiState> = _uiState.asStateFlow()

    private var perfectRunsCount = 0

    fun classify(category: String) {
        val state = _uiState.value
        val resource = state.current ?: return
        val isCorrect = category == resource.correctCategory
        val newCorrectCount = if (isCorrect) state.correctCount + 1 else state.correctCount
        val feedback = if (isCorrect) "¡Bien clasificado! \"${resource.name}\" ayuda a ${category.lowercase()}."
                        else "Prueba otra categoria: \"${resource.name}\" encaja mejor en ${resource.correctCategory}."

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            progressRepository.recordInteractionAndAwardXp(
                kind = "LIBRARY_CLASSIFIED", referenceId = resource.id,
                xpAwarded = if (isCorrect) 5 else 1, wasSuccessful = isCorrect, nowEpochMs = now
            )

            if (isCorrect && state.currentIndex >= state.resources.lastIndex) {
                val perfect = newCorrectCount == state.resources.size
                if (perfect) perfectRunsCount++
                val bonusXp = if (perfect) 15 else 5
                progressRepository.recordInteractionAndAwardXp(
                    kind = "LIBRARY_ROUND_COMPLETED", referenceId = "biblioteca", xpAwarded = bonusXp, wasSuccessful = perfect, nowEpochMs = now
                )
                val newlyEarned = BadgeEngine.evaluateNewlyEarned(BadgeEngine.UserStats(libraryPerfectRounds = perfectRunsCount), emptySet())
                if (newlyEarned.isNotEmpty()) badgeRepository.awardBadges(newlyEarned, now)

                val message = if (perfect) "¡Biblioteca ordenada a la primera! +$bonusXp XP" else "¡Ronda terminada! +$bonusXp XP"
                _uiState.value = state.copy(
                    correctCount = newCorrectCount,
                    feedback = feedback,
                    currentIndex = state.currentIndex + 1,
                    completionMessage = message
                )
            } else {
                _uiState.value = state.copy(
                    correctCount = newCorrectCount,
                    feedback = feedback,
                    currentIndex = if (isCorrect) state.currentIndex + 1 else state.currentIndex
                )
            }
        }
    }

    fun consumeCompletionMessage() {
        _uiState.value = _uiState.value.copy(completionMessage = null)
    }

    class Factory(
        private val progressRepository: ProgressRepository,
        private val badgeRepository: BadgeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            BibliotecaViewModel(progressRepository, badgeRepository) as T
    }
}
