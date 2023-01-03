package com.zjutjh.ijh.ui.screen

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: WeJhUserRepository) :
    ViewModel() {

    private val _uiState = MutableLoginUIState()
    val uiState: LoginUIState = _uiState

    fun updateUsername(value: String) {
        _uiState.username = value
    }

    fun updatePassword(value: String) {
        _uiState.password = value
    }

    fun login() {
        _uiState.loading = true
        viewModelScope.launch {
            val result = userRepository.login(_uiState.username, _uiState.password)
            result.onSuccess {
                Log.i("Login", it.toString())
            }
            result.onFailure {
                Log.e("Login", it.toString())
            }
            delay(300)
            _uiState.loading = false
        }
    }

}

@Stable
interface LoginUIState {
    val username: String
    val password: String
    val loading: Boolean
}

private class MutableLoginUIState : LoginUIState {
    override var username: String by mutableStateOf(String())
    override var password: String by mutableStateOf(String())
    override var loading: Boolean by mutableStateOf(false)
}