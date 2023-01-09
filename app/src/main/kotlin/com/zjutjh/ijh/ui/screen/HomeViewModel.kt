package com.zjutjh.ijh.ui.screen

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.model.Course
import com.zjutjh.ijh.data.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val courseRepository: CourseRepository) :
    ViewModel() {

    private val _uiState = MutableHomeUiState()
    val uiState: HomeUiState = _uiState

    init {
        Log.i("ViewModel", "HomeViewModel: $this")
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.isRefreshing = true
            delay(500)
            _uiState.courses = courseRepository.getCourses()
            _uiState.isRefreshing = false
        }
    }

    private class MutableHomeUiState : HomeUiState {
        override var courses: ImmutableList<Course> by mutableStateOf(persistentListOf())
        override var isRefreshing: Boolean by mutableStateOf(false)
    }
}

@Stable
interface HomeUiState {
    val courses: ImmutableList<Course>
    val isRefreshing: Boolean
}