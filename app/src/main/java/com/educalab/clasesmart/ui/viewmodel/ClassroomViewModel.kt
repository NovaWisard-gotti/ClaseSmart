package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.repository.ClassroomRepository
import com.educalab.clasesmart.data.repository.ProgressRepository
import com.educalab.clasesmart.domain.logic.ProgressEngine
import com.educalab.clasesmart.domain.model.ClassroomObject
import com.educalab.clasesmart.domain.model.ModuleState
import kotlinx.coroutines.flow.*

data class AulaUiState(
    val objects: List<ClassroomObject> = emptyList(),
    val totalXp: Int = 0,
    val aulaLevel: Int = 1,
    val xpForNextLevel: Int? = null
)

class ClassroomViewModel(
    private val classroomRepository: ClassroomRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val uiState: StateFlow<AulaUiState> = combine(
        classroomRepository.observeSnapshot(),
        progressRepository.observeXp()
    ) { snapshot, xp ->
        val level = ProgressEngine.levelForXp(xp)
        // El estado BLOQUEADO se sembro una sola vez en Room y nunca se reescribia
        // al subir de nivel: aqui se recalcula en cada lectura para que un objeto
        // se muestre disponible en cuanto el nivel actual alcanza su unlockLevel,
        // sin depender de una escritura explicita que nunca ocurria.
        val effectiveObjects = snapshot.objects.map { obj ->
            if (obj.state == ModuleState.BLOQUEADO && level >= obj.unlockLevel) {
                obj.copy(state = ModuleState.DISPONIBLE)
            } else obj
        }
        AulaUiState(
            objects = effectiveObjects,
            totalXp = xp,
            aulaLevel = level,
            xpForNextLevel = ProgressEngine.xpForNextLevel(level)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AulaUiState())

    class Factory(
        private val classroomRepository: ClassroomRepository,
        private val progressRepository: ProgressRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ClassroomViewModel(classroomRepository, progressRepository) as T
    }
}
