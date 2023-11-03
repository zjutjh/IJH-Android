package com.zjutjh.ijh.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.CampusRepository
import com.zjutjh.ijh.data.CourseRepository
import com.zjutjh.ijh.data.WeJhUserRepository
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.ui.model.TermDayState
import com.zjutjh.ijh.ui.model.TermWeekState
import com.zjutjh.ijh.ui.model.toTermDayState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseCalendarViewModel @Inject constructor(
    campusRepository: CampusRepository,
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
    private val localTermDayState = campusRepository.infoStream
        .mapLatest {
            it?.toTermDayState()
        }
        .flowOn(Dispatchers.Default)

    private val _selectedTermDayState = MutableStateFlow<TermWeekState?>(null)

    /**
     * State of term-week view switcher
     */
    private val _termView = MutableStateFlow(false)
    val termView = _termView.asStateFlow()

    /**
     * Term state pair, first is current term day, second is selected term week
     */
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
    val coursesState: StateFlow<ImmutableList<Course>?> = termState
        .dropWhile { (l, r) -> l == null && r == null } // Drop initial null value
        .flatMapLatest { state ->
            val termState = state.second ?: state.first // Selected state have higher priority
            if (termState != null) {
                courseRepository.getCourses(termState.year, termState.term)
                    .map { it to termState.week } // Save week number for further filtering
            } else flowOf(Pair(emptyList(), 1))
        }
        .combine(_termView) { pair, termView ->
            // Switch between term view and week view
            if (termView) {
                pair.first
            } else {
                // Week view
                pair.first.filter { pair.second in it.weeks }
            }.toImmutableList()
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

    fun clearSelection() {
        _selectedTermDayState.value = null
    }

    // Activity scoped view model to preload data
    suspend fun preload() {
        coursesState.dropWhile { it == null }.first()
        startYear.dropWhile { it == null }.first()
        Log.i("Schedule", "Preload finished.")
    }
}