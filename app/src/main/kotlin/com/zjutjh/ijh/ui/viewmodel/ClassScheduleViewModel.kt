package com.zjutjh.ijh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.WeJhInfoRepository
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.model.toTermDayState
import com.zjutjh.ijh.util.LoadResult
import com.zjutjh.ijh.util.asLoadResultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ClassScheduleViewModel @Inject constructor(
    weJhInfoRepository: WeJhInfoRepository,
    private val courseRepository: CourseRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val coursesState: StateFlow<LoadResult<List<Course>>> = weJhInfoRepository.infoStream
        .distinctUntilChanged()
        .mapLatest {
            it?.toTermDayState()
        }
        .distinctUntilChanged()
        .flatMapLatest { state ->
            if (state != null) {
                courseRepository.getCourses(state.year, state.term)
            } else flowOf(emptyList())
        }
        .flowOn(Dispatchers.Default)
        .asLoadResultStateFlow(
            scope = viewModelScope,
            started = SharingStarted.Eagerly
        )
}