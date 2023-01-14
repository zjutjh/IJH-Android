package com.zjutjh.ijh.ui.screen

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weJhUserRepository: WeJhUserRepository,
    private val courseRepository: CourseRepository
) : ViewModel() {

    val loginState: StateFlow<Boolean> = weJhUserRepository.userStream
        .map { it.uid != 0L }
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
        }
    }

    /**
     * Sync with upstream
     */
    fun refresh() {
        if (refreshState || !loginState.value) {
            return
        }
        viewModelScope.launch {
            refreshState = true
            try {
                weJhUserRepository.sync()
                Log.i("HomeSync", "Synchronization succeeded.")
            } catch (e: Throwable) {
                Log.w("HomeSync", "Error: $e.")
            }
            delay(100)
            refreshState = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            weJhUserRepository.logout()
        }
    }
}