package com.zjutjh.ijh.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weJhUserRepository: WeJhUserRepository,
    private val courseRepository: CourseRepository
) : ViewModel() {

    val loginState: StateFlow<Boolean> = weJhUserRepository.userStream
        .map { !it.isEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val _coursesState: MutableStateFlow<ImmutableList<Course>> =
        MutableStateFlow(persistentListOf())
    var coursesState: StateFlow<ImmutableList<Course>> = _coursesState.asStateFlow()

    private val _refreshState = MutableStateFlow(false)
    val refreshState: StateFlow<Boolean> = _refreshState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            loginState.collectLatest {
                if (it) refresh(this)
            }
        }
    }

    /**
     * Sync with upstream
     */
    fun refresh() {
        if (_refreshState.value) {
            // Already in refreshing, abort.
            return
        }
        if (!loginState.value) {
            // Not logged in, no refresh required.
            viewModelScope.launch {
                // Add animation duration to make the animation smoother
                _refreshState.update { true }
                delay(animationDuration)
                _refreshState.update { false }
            }
        } else {
            viewModelScope.launch {
                refresh(this)
            }
        }
    }

    private suspend fun refresh(scope: CoroutineScope) {
        _refreshState.update { true }
        val timer = scope.async { delay(animationDuration) }
        try {
            weJhUserRepository.sync()
            courseRepository.sync()
            Log.i("HomeSync", "Synchronization succeeded.")
        } catch (e: Throwable) {
            Log.w("HomeSync", "Error: $e.")
        }
        timer.await()
        _refreshState.update { false }
    }

    companion object {
        private const val animationDuration: Long = 300
    }
}