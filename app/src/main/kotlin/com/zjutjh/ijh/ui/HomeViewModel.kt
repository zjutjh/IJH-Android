package com.zjutjh.ijh.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zjutjh.ijh.data.Course
import com.zjutjh.ijh.data.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Inject

@Stable
interface HomeUiState {
    val courses: ImmutableList<Course>
}

private class MutableHomeUiState : HomeUiState {
    override var courses: ImmutableList<Course> by mutableStateOf(persistentListOf())
}

@HiltViewModel
class HomeViewModel @Inject constructor(courseRepository: CourseRepository) :
    ViewModel() {
    private val _uiState = MutableHomeUiState()
    val uiState: HomeUiState = _uiState

    init {
        _uiState.courses = courseRepository.fetchCourses()
    }
}
