package com.zjutjh.ijh.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.model.Course
import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    var coursesState: ImmutableList<Course> by mutableStateOf(persistentListOf())

    var refreshState by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            coursesState = courseRepository.getCourses()
            loginState.collectLatest {
                if (it) suspendRefresh(this)
            }
        }
    }

    /**
     * Sync with upstream
     */
    fun refresh() {
        if (refreshState) {
            // Already in refreshing, abort.
            return
        }
        if (!loginState.value) {
            // Not logged in, no refresh required.
            viewModelScope.launch {
                // Add animation duration to make the animation smoother
                refreshState = true
                delay(animationDuration)
                refreshState = false
            }
        } else {
            viewModelScope.launch {
                suspendRefresh(this)
            }
        }
    }

    private suspend fun suspendRefresh(scope: CoroutineScope) {
        refreshState = true
        val timer = scope.async { delay(animationDuration) }
        try {
            weJhUserRepository.sync()
            Log.i("HomeSync", "Synchronization succeeded.")
        } catch (e: Throwable) {
            Log.w("HomeSync", "Error: $e.")
        }
        timer.await()
        refreshState = false
    }

    companion object {
        private const val animationDuration: Long = 300
    }
}