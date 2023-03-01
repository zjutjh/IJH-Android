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
import com.zjutjh.ijh.ui.model.toTermDayState
import com.zjutjh.ijh.util.LoadResult
import com.zjutjh.ijh.util.asLoadResultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.time.toKotlinDuration

@HiltViewModel
class HomeViewModel @Inject constructor(
    weJhUserRepository: WeJhUserRepository,
    private val courseRepository: CourseRepository,
    private val weJhInfoRepository: WeJhInfoRepository,
) : ViewModel() {

    private val timerFlow: Flow<Unit> = flow {
        val duration = Duration.ofSeconds(20).toKotlinDuration()
        while (true) {
            emit(Unit)
            delay(duration)
        }
    }
    val courseLastSyncState: StateFlow<Duration?> = courseRepository.lastSyncTimeStream
        .distinctUntilChanged()
        .combine(timerFlow) { t1, _ -> t1 }
        .map {
            if (it == null) null else {
                Duration.between(it, ZonedDateTime.now())
            }
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _refreshState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val refreshState: StateFlow<Boolean> = _refreshState.asStateFlow()

    val loginState: StateFlow<LoadResult<Boolean>> = weJhUserRepository.userStream
        .map { it != null }
        .distinctUntilChanged()
        .flowOn(Dispatchers.Default)
        .asLoadResultStateFlow(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        )

    private val termLocalRefreshChannel: MutableStateFlow<Unit> = MutableStateFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    val termDayState: StateFlow<LoadResult<TermDayState?>> = weJhInfoRepository.infoStream
        .combine(termLocalRefreshChannel) { t1, _ -> t1 }
        .mapLatest {
            it?.toTermDayState()
        }
        .flowOn(Dispatchers.Default)
        .asLoadResultStateFlow(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val coursesState: StateFlow<LoadResult<List<Course>>> = termDayState
        .drop(1)
        .distinctUntilChanged(LoadResult<*>::isEqual)
        .flatMapLatest { state ->
            if (state is LoadResult.Ready && state.data != null) {
                val day = state.data
                if (day.isInTerm) {
                    courseRepository.getCourses(day.year, day.term)
                        .map { it.filterToday(day) }
                } else flowOf(emptyList())
            } else flowOf(emptyList())
        }
        .flowOn(Dispatchers.Default)
        .asLoadResultStateFlow(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            // Subscribe latest login state, and trigger refresh.
            loginState
                .drop(1) // Wait until available
                .collectLatest {
                    refreshAll(this)
                }
        }
    }

    private fun List<Course>.filterToday(day: TermDayState): List<Course> =
        this.filter {
            (day.dayOfWeek == it.dayOfWeek) && (day.week in it.weeks)
        }

    /**
     * Sync with upstream
     */
    fun refreshAll() {
        if (_refreshState.value) {
            // Already in refreshing, abort.
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            refreshAll(this)
        }
    }

    private suspend fun refreshAll(scope: CoroutineScope) {
        _refreshState.update { true }
        val timer = scope.async { delay(300L) }

        val term = refreshTerm()

        val isLoggedIn = loginState.value
        if (isLoggedIn is LoadResult.Ready && isLoggedIn.data) {
            if (term != null) {
                refreshCourse(term.first, term.second)
            }
        }

        Log.i("Home", "Synchronization complete.")

        timer.await()
        _refreshState.update { false }
    }

    private suspend fun refreshTerm(): Pair<Int, Term>? {
        runCatching { weJhInfoRepository.sync() }
            .fold({
                Log.i("Home", "Sync WeJhInfo succeed.")
                return it
            }) {
                Log.e("Home", "Sync WeJhInfo failed: $it")
                // Run local refresh when failed
                termLocalRefreshChannel.emit(Unit)
                if (termDayState.value is LoadResult.Ready) {
                    val termDay = (termDayState.value as LoadResult.Ready).data
                    return if (termDay != null) {
                        termDay.year to termDay.term
                    } else null
                }
            }
        return null
    }

    private suspend fun refreshCourse(year: Int, term: Term) {
        runCatching {
            courseRepository.sync(year, term)
        }.fold({
            Log.i("Home", "Sync Courses succeed.")
        }) {
            Log.e("Home", "Sync Courses failed: $it")
        }
    }

}