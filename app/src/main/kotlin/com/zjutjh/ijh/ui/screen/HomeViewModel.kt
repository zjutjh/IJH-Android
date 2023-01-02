package com.zjutjh.ijh.ui.screen

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(courseRepository: CourseRepository) :
    ViewModel() {

    private val _uiState = MutableHomeUIState()
    val uiState: HomeUIState = _uiState

    init {
        viewModelScope.launch {
            _uiState.courses = courseRepository.getCourses()
        }
    }
}

@Stable
interface HomeUIState {
    val courses: ImmutableList<Course>
}

private class MutableHomeUIState : HomeUIState {
    override var courses: ImmutableList<Course> by mutableStateOf(persistentListOf())
}
