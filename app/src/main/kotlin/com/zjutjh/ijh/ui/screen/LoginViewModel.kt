package com.zjutjh.ijh.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.model.WeJhUser
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.network.exception.ApiResponseException
import com.zjutjh.ijh.network.exception.HttpStatusException
import com.zjutjh.ijh.network.exception.WeJhApiExceptions
import com.zjutjh.ijh.ui.model.CancellableLoadingState
import com.zjutjh.ijh.ui.model.DismissibleSnackbarVisuals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val CANCELLABLE_INTERVAL = 1000L

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: WeJhUserRepository) :
    ViewModel() {

    private val _uiState = MutableLoginUIState()
    val uiState: LoginUIState = _uiState

    private var currentJob: Job? = null

    fun loginOrCancel(context: Context, onSuccess: (WeJhUser) -> Unit) {
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
                        onSuccess(it)
                    }.onFailure {
                        loginErrorHandle(it, context)
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
            else -> Unit
        }
    }

    private suspend fun loginErrorHandle(it: Throwable, context: Context) {
        when (it) {
            is ApiResponseException -> {
                when (it.code) {
                    WeJhApiExceptions.PARAM_ERROR -> {
                        _uiState.usernameFieldState = UsernameFieldState.INVALID
                        _uiState.passwordFieldState = PasswordFieldState.INVALID
                        _uiState.showDismissibleSnackbar(context.getString(R.string.invalid_inputs))
                    }
                    WeJhApiExceptions.USER_NOT_FOUND -> {
                        _uiState.usernameFieldState = UsernameFieldState.UNKNOWN
                        _uiState.showDismissibleSnackbar(context.getString(R.string.unknown_user))
                    }
                    WeJhApiExceptions.WRONG_PASSWORD -> {
                        _uiState.passwordFieldState = PasswordFieldState.WRONG
                        _uiState.showDismissibleSnackbar(context.getString(R.string.wrong_password))
                    }
                    else -> {
                        Log.w("Login", "code: ${it.code}, msg: ${it.message}")
                        _uiState.showDismissibleSnackbar("${it.message} (${it.code})")
                    }
                }
            }
            is HttpStatusException -> {
                _uiState.showDismissibleSnackbar(context.getString(R.string.network_error))
            }
            is SocketTimeoutException -> {
                _uiState.showDismissibleSnackbar(context.getString(R.string.request_timeout))
            }
            else -> {
                Log.e("Login", it.toString())
                _uiState.showDismissibleSnackbar(context.getString(R.string.unknown_error))
            }
        }
    }

    private class MutableLoginUIState : LoginUIState {
        override var username: String by mutableStateOf(String())
        override var password: String by mutableStateOf(String())
        override var usernameFieldState: UsernameFieldState by mutableStateOf(UsernameFieldState.OK)
        override var passwordFieldState: PasswordFieldState by mutableStateOf(PasswordFieldState.OK)

        override var loading: CancellableLoadingState by mutableStateOf(CancellableLoadingState.READY)
        override val snackbarHostState: SnackbarHostState = SnackbarHostState()

        override fun updateUsername(value: String) {
            username = value
            usernameFieldState = if (value.isBlank()) {
                UsernameFieldState.INVALID
            } else {
                UsernameFieldState.OK
            }
        }

        override fun updatePassword(value: String) {
            password = value
            passwordFieldState = if (value.isBlank()) {
                PasswordFieldState.INVALID
            } else {
                PasswordFieldState.OK
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        override suspend fun showDismissibleSnackbar(message: String): SnackbarResult =
            snackbarHostState.showSnackbar(
                DismissibleSnackbarVisuals(message),
            )
    }
}

@Stable
interface LoginUIState {
    val username: String
    val password: String
    val loading: CancellableLoadingState
    val usernameFieldState: UsernameFieldState
    val passwordFieldState: PasswordFieldState
    val snackbarHostState: SnackbarHostState

    fun updateUsername(value: String)
    fun updatePassword(value: String)

    suspend fun showDismissibleSnackbar(message: String): SnackbarResult
}

enum class UsernameFieldState {
    OK, INVALID, UNKNOWN,
}

enum class PasswordFieldState {
    OK, INVALID, WRONG,
}

