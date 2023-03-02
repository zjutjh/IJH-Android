package com.zjutjh.ijh.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.WeJhInfoRepository
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.ui.model.TermDayState
import com.zjutjh.ijh.ui.model.TermWeekState
import com.zjutjh.ijh.ui.model.toTermDayState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ClassScheduleViewModel @Inject constructor(
    weJhInfoRepository: WeJhInfoRepository,
    weJhUserRepository: WeJhUserRepository,
    private val courseRepository: CourseRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val startYear = weJhUserRepository.userStream
        .mapLatest { it?.studentId?.substring(0, 4)?.toIntOrNull() ?: 2019 }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

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

    val termState: StateFlow<Pair<TermDayState?, TermWeekState?>> = localTermDayState
        .distinctUntilChanged()
        .combine(_selectedTermDayState) { localTermDayState, selectedTermDayState ->
            localTermDayState to selectedTermDayState
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Pair(null, null)
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val coursesState: StateFlow<List<Course>?> = termState
        .dropWhile { (l, r) -> l == null && r == null }
        .flatMapLatest { state ->
            val termState = state.second ?: state.first
            if (termState != null) {
                courseRepository.getCourses(termState.year, termState.term)
                    .map { it to termState.week }
            } else flowOf(Pair(emptyList(), 1))
        }
        .combine(_termView) { courses, termView ->
            if (termView) {
                courses.first
            } else {
                courses.first.filter { course -> course.weeks.contains(courses.second) }
            }
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private var currentJob: Job? = null

    fun refresh() {
        currentJob = viewModelScope.launch {
            currentJob?.cancelAndJoin()
            _isRefreshing.value = true
            val state = termState.value.second ?: termState.value.first
            if (state != null)
                refreshCourses(state.year, state.term)
            _isRefreshing.value = false
        }
    }

    private fun refresh(year: Int, term: Term) {
        currentJob = viewModelScope.launch {
            currentJob?.cancelAndJoin()
            _isRefreshing.value = true
            refreshCourses(year, term)
            _isRefreshing.value = false
        }
    }

    private suspend fun refreshCourses(year: Int, term: Term) {
        try {
            courseRepository.sync(year, term)
        } catch (e: Exception) {
            Log.e("Schedule", "Sync Courses failed: $e")
        }
    }

    fun switchTermView() {
        _termView.value = !_termView.value
    }

    fun selectTerm(termWeekState: TermWeekState, refresh: Boolean = false) {
        _selectedTermDayState.value = termWeekState
        if (refresh) refresh(termWeekState.year, termWeekState.term)
    }

    fun unselectTerm() {
        _selectedTermDayState.value = null
    }

    // Activity scoped view model to preload data
    suspend fun preload() {
        coursesState.dropWhile { it == null }.first()
        startYear.dropWhile { it == null }.first()
        Log.i("Schedule", "Preload finished.")
    }
}