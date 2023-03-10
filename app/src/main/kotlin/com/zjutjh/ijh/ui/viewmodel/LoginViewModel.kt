package com.zjutjh.ijh.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonDataException
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.exception.ApiResponseException
import com.zjutjh.ijh.exception.HttpStatusException
import com.zjutjh.ijh.exception.UnauthorizedException
import com.zjutjh.ijh.exception.WeJhApiExceptions
import com.zjutjh.ijh.ui.model.CancellableLoadingState
import com.zjutjh.ijh.ui.util.DismissibleSnackbarVisuals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

private const val CANCELLABLE_INTERVAL = 1000L

@HiltViewModel
class LoginViewModel @Inject constructor(private val weJhUserRepository: WeJhUserRepository) :
    ViewModel() {

    private val _uiState = MutableLoginUiState()
    val uiState: LoginUiState = _uiState

    private var currentJob: Job? = null

    fun loginOrCancel(context: Context, onSuccess: () -> Unit) {
        when (_uiState.loading) {
            CancellableLoadingState.READY -> {
                currentJob = viewModelScope.launch {
                    _uiState.loading = CancellableLoadingState.LOADING

                    val task = async(SupervisorJob(coroutineContext.job)) {
                        weJhUserRepository.login(_uiState.username, _uiState.password)
                    }

                    val timeoutCancel = launch {
                        delay(CANCELLABLE_INTERVAL)
                        _uiState.loading = CancellableLoadingState.CANCELLABLE
                    }

                    val result = try {
                        Result.success(task.await())
                    } catch (t: Throwable) {
                        Result.failure(t)
                    }

                    timeoutCancel.cancelAndJoin()
                    _uiState.loading = CancellableLoadingState.PENDING

                    result.fold(
                        onSuccess = {
                            Log.i("Login", "Success.")
                            onSuccess()
                        },
                        onFailure = {
                            loginErrorHandle(it, context)
                        },
                    )

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

    private suspend fun loginErrorHandle(t: Throwable, context: Context) {
        when (t) {
            is ApiResponseException -> {
                when (t.code) {
                    WeJhApiExceptions.PARAM_ERROR -> {
                        _uiState.usernameUiState = UsernameUiState.INVALID
                        _uiState.passwordUiState = PasswordUiState.INVALID
                        showDismissibleSnackbar(context.getString(R.string.invalid_inputs))
                    }
                    WeJhApiExceptions.USER_NOT_FOUND -> {
                        _uiState.usernameUiState = UsernameUiState.UNKNOWN
                        showDismissibleSnackbar(context.getString(R.string.unknown_user))
                    }
                    WeJhApiExceptions.WRONG_PASSWORD -> {
                        _uiState.passwordUiState = PasswordUiState.WRONG
                        showDismissibleSnackbar(context.getString(R.string.wrong_password))
                    }
                    else -> {
                        Log.w("Login", "code: ${t.code}, msg: ${t.message}")
                        showDismissibleSnackbar("${t.message} (${t.code})")
                    }
                }
            }
            is JsonDataException -> {
                Log.e("JsonParsing", t.localizedMessage ?: t.toString())
                showDismissibleSnackbar(context.getString(R.string.error_response))
            }
            is HttpStatusException -> {
                showDismissibleSnackbar(context.getString(R.string.unexpected_http_status, t.code))
            }
            is UnauthorizedException -> {
                showDismissibleSnackbar(context.getString(R.string.authentication_exception))
            }
            is SocketTimeoutException -> {
                showDismissibleSnackbar(context.getString(R.string.request_timeout))
            }
            is UnknownHostException -> {
                showDismissibleSnackbar(context.getString(R.string.network_error))
            }
            else -> {
                Log.e("Login", "Error: $t")
                showDismissibleSnackbar(context.getString(R.string.unknown_error))
            }
        }
    }

    fun updateUsername(value: String) {
        _uiState.username = value
        checkUsername()
    }

    fun updatePassword(value: String) {
        _uiState.password = value
        checkPassword()
    }

    fun checkUsername(): Boolean {
        return if (_uiState.username.isBlank()) {
            _uiState.usernameUiState = UsernameUiState.INVALID
            false
        } else {
            _uiState.usernameUiState = UsernameUiState.OK
            true
        }
    }

    fun checkPassword(): Boolean {
        return if (_uiState.password.isBlank()) {
            _uiState.passwordUiState = PasswordUiState.INVALID
            false
        } else {
            _uiState.passwordUiState = PasswordUiState.OK
            true
        }
    }

    suspend fun showDismissibleSnackbar(message: String): SnackbarResult =
        _uiState.snackbarHostState.showSnackbar(
            DismissibleSnackbarVisuals(message),
        )

    private class MutableLoginUiState : LoginUiState {
        override var username: String by mutableStateOf(String())
        override var password: String by mutableStateOf(String())
        override var usernameUiState: UsernameUiState by mutableStateOf(UsernameUiState.OK)
        override var passwordUiState: PasswordUiState by mutableStateOf(PasswordUiState.OK)
        override var loading: CancellableLoadingState by mutableStateOf(CancellableLoadingState.READY)
        override val snackbarHostState: SnackbarHostState = SnackbarHostState()
    }
}

@Stable
interface LoginUiState {
    val username: String
    val password: String
    val loading: CancellableLoadingState
    val usernameUiState: UsernameUiState
    val passwordUiState: PasswordUiState
    val snackbarHostState: SnackbarHostState
}

enum class UsernameUiState {
    OK, INVALID, UNKNOWN,
}

enum class PasswordUiState {
    OK, INVALID, WRONG,
}

