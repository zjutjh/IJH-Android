package com.zjutjh.ijh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.model.WeJhUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val weJhUserRepository: WeJhUserRepository
) : ViewModel() {

    /**
     * Null means loading state
     */
    val userState: StateFlow<WeJhUser?> = weJhUserRepository.userStream
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun logout(callback: () -> Unit) {
        viewModelScope.launch {
            weJhUserRepository.logout()
            callback()
        }
    }
}