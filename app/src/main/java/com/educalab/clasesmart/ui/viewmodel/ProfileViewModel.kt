package com.educalab.clasesmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.educalab.clasesmart.data.local.dao.UserProfileDao
import com.educalab.clasesmart.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val alias: String = "",
    val avatarId: String = "avatar_1",
    val ageBand: String = "8-9",
    val onboardingCompleted: Boolean = false,
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = true,
    val isLoading: Boolean = true
)

/** 8 avatares base (Regla 17). Ninguno pide nombre real ni foto. */
val AVAILABLE_AVATARS = listOf("avatar_1", "avatar_2", "avatar_3", "avatar_4", "avatar_5", "avatar_6", "avatar_7", "avatar_8")

class ProfileViewModel(private val profileDao: UserProfileDao) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = profileDao.getProfile()
            if (profile != null) {
                _uiState.value = ProfileUiState(
                    alias = profile.alias, avatarId = profile.avatarId, ageBand = profile.ageBand,
                    onboardingCompleted = profile.onboardingCompleted, soundEnabled = profile.soundEnabled,
                    hapticEnabled = profile.hapticEnabled, isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun setAlias(alias: String) { _uiState.value = _uiState.value.copy(alias = alias.take(16)) }
    fun setAvatar(avatarId: String) { _uiState.value = _uiState.value.copy(avatarId = avatarId) }
    fun setAgeBand(ageBand: String) { _uiState.value = _uiState.value.copy(ageBand = ageBand) }
    fun setSound(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(soundEnabled = enabled)
        viewModelScope.launch { profileDao.setSoundEnabled(enabled) }
    }
    fun setHaptic(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(hapticEnabled = enabled)
        viewModelScope.launch { profileDao.setHapticEnabled(enabled) }
    }

    fun completeOnboarding(onDone: () -> Unit) {
        val state = _uiState.value
        viewModelScope.launch {
            val alias = state.alias.ifBlank { "Explorador" }
            val now = System.currentTimeMillis()
            profileDao.upsert(
                UserProfileEntity(
                    alias = alias, avatarId = state.avatarId, ageBand = state.ageBand,
                    createdAtEpochMs = now, lastOpenedEpochMs = now, onboardingCompleted = true
                )
            )
            _uiState.value = state.copy(alias = alias, onboardingCompleted = true)
            onDone()
        }
    }

    class Factory(private val profileDao: UserProfileDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = ProfileViewModel(profileDao) as T
    }
}
