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
import com.zjutjh.ijh.ui.model.CancellableLoadingState
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
                    200501 -> {
                        _uiState.isUsernameError = true
                        _uiState.isPasswordError = true
                        _uiState.showDismissibleSnackbar(context.getString(R.string.invalid_inputs))
                    }
                    200503 -> {
                        _uiState.isUsernameError = true
                        _uiState.showDismissibleSnackbar(context.getString(R.string.unknown_user))
                    }
                    200504 -> {
                        _uiState.isPasswordError = true
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
    val isUsernameError: Boolean
    val isPasswordError: Boolean
    val snackbarHostState: SnackbarHostState

    fun updateUsername(value: String)
    fun updatePassword(value: String)

    suspend fun showDismissibleSnackbar(message: String): SnackbarResult
}

class DismissibleSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
) : SnackbarVisuals {
    override val withDismissAction: Boolean = true
}
