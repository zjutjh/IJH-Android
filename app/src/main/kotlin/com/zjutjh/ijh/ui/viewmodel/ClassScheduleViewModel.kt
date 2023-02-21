package com.zjutjh.ijh.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.WeJhInfoRepository
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.model.TermWeekState
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
    private val localTermDayState = weJhInfoRepository.infoStream
        .mapLatest {
            it?.toTermDayState()
        }
        .flowOn(Dispatchers.Default)

    private val _selectedTermDayState = MutableStateFlow<TermWeekState?>(null)

    // Switch between term view and week view
    private val _termView = MutableStateFlow(false)
    val termView = _termView.asStateFlow()

    val termState = localTermDayState
        .combine(_selectedTermDayState) { localTermDayState, selectedTermDayState ->
            localTermDayState to selectedTermDayState
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Pair(null, null)
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val coursesState: StateFlow<LoadResult<List<Course>>> = termState
        .drop(1)
        .combine(_termView) { termState, termView ->
            Pair(termState, termView)
        }
        .flatMapLatest { (state, view) ->
            val termState = state.second ?: state.first
            if (termState != null) {
                if (view) courseRepository.getCourses(termState.year, termState.term)
                    .map { it.filter { course -> course.weeks.contains(termState.week) } }
                else courseRepository.getCourses(termState.year, termState.term)
            } else flowOf(emptyList())
        }
        .flowOn(Dispatchers.Default)
        .asLoadResultStateFlow(
            scope = viewModelScope,
            started = SharingStarted.Eagerly
        )
}