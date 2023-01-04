package com.zjutjh.ijh.ui.screen

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.network.exception.ApiResponseException
import com.zjutjh.ijh.ui.model.CancellableLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

private const val CANCELLABLE_INTERVAL = 1000L

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: WeJhUserRepository) :
    ViewModel() {

    private val _uiState = MutableLoginUIState()
    val uiState: LoginUIState = _uiState

    private var currentJob: Job? = null

    fun loginOrCancel() {
        when (_uiState.loading) {
            CancellableLoadingState.READY -> {
                currentJob = viewModelScope.launch {
                    _uiState.loading = CancellableLoadingState.LOADING
                    val task = async {
                        userRepository.login(_uiState.username, _uiState.password)
                    }

                    val timeoutCancel = launch {
                        delay(CANCELLABLE_INTERVAL)
                        _uiState.loading = CancellableLoadingState.CANCELLABLE
                    }

                    val result = task.await()
                    timeoutCancel.cancelAndJoin()
                    _uiState.loading = CancellableLoadingState.PENDING

                    result.onSuccess {
                        Log.i("Login", it.toString())
                        return@launch
                    }
                    result.onFailure {
                        when (it) {
                            is ApiResponseException -> {
                                Log.e("Login", "code: ${it.code}, msg: ${it.message}")
                                /* TODO: locale */
                                when (it.code) {
                                    200501 -> {
                                        _uiState.isUsernameError = true
                                        _uiState.isPasswordError = true
                                        _uiState.snackbarHostState.showDismissibleSnackBar("Bad inputs.")
                                    }
                                    200503 -> {
                                        _uiState.isUsernameError = true
                                        _uiState.snackbarHostState.showDismissibleSnackBar("Unknown username.")
                                    }
                                    else -> {
                                        _uiState.snackbarHostState.showDismissibleSnackBar("Unknown error.")
                                    }
                                }
                            }
                            else -> {
                                Log.e("Login", it.toString())
                                _uiState.isUsernameError = true
                                _uiState.isPasswordError = true
                            }
                        }
                    }
                    _uiState.loading = CancellableLoadingState.READY
                }
            }
            CancellableLoadingState.CANCELLABLE -> {
                viewModelScope.launch {
                    currentJob?.cancelAndJoin()
                    _uiState.loading = CancellableLoadingState.READY
                }
            }
            else -> {}
        }
    }

    private suspend fun SnackbarHostState.showDismissibleSnackBar(message: String) {

        showSnackbar(
            message,
            null,
            true
        )

    }

    private class MutableLoginUIState : LoginUIState {
        override var username: String by mutableStateOf(String())
        override var password: String by mutableStateOf(String())
        override var loading: CancellableLoadingState by mutableStateOf(CancellableLoadingState.READY)
        override var isUsernameError: Boolean by mutableStateOf(false)
        override var isPasswordError: Boolean by mutableStateOf(false)
        override val snackbarHostState: SnackbarHostState = SnackbarHostState()

        override fun updateUsername(value: String) {
            username = value
            isUsernameError = false
        }

        override fun updatePassword(value: String) {
            password = value
            isPasswordError = false
        }
    }
}

@Stable
interface LoginUIState {
    val username: String
    val password: String
    val loading: CancellableLoadingState
    val isUsernameError: Boolean
    val isPasswordError: Boolean
    val snackbarHostState: SnackbarHostState

    fun updateUsername(value: String)
    fun updatePassword(value: String)
}