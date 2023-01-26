package com.zjutjh.ijh.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.WeJhInfoRepository
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.model.TermDayState
import com.zjutjh.ijh.ui.model.toTermDayState
import com.zjutjh.ijh.util.LoadResult
import com.zjutjh.ijh.util.asLoadResultStateFlow
import com.zjutjh.ijh.util.isReady
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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

    val loginState: StateFlow<LoadResult<Boolean>> = weJhUserRepository.userStream
        .map { it != null }
        .distinctUntilChanged()
        .asLoadResultStateFlow(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
        )

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

    private val _coursesState: MutableStateFlow<ImmutableList<Course>> =
        MutableStateFlow(persistentListOf())
    var coursesState: StateFlow<ImmutableList<Course>> = _coursesState.asStateFlow()

    private val _refreshState = MutableStateFlow(false)
    val refreshState: StateFlow<Boolean> = _refreshState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val termState: StateFlow<LoadResult<TermDayState?>> = weJhInfoRepository.infoStream
        .mapLatest {
            it?.toTermDayState()
        }
        .distinctUntilChanged()
        .asLoadResultStateFlow(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            this.launch {
                // Subscribe state.
                termState.collect()
            }
            loginState
                .filter { it.isReady() }
                .collectLatest {
                    refreshAll(this)
                }
        }
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
        val timer = scope.async { delay(animationDuration) }

        // Parallel jobs
        refreshTerm()

        val isLoggedIn = loginState.value
        if (isLoggedIn is LoadResult.Ready && isLoggedIn.data)
            refreshCourse()

        Log.i("HomeSync", "Synchronization complete.")

        timer.await()
        _refreshState.update { false }
    }

    /**
     * Refresh on startup only or manually
     */
    private suspend fun refreshTerm() {
        runCatching { weJhInfoRepository.sync() }.onFailure {
            Log.e("HomeSync", "Sync WeJhInfo failed: $it")
        }
    }

    private suspend fun refreshCourse() {
        val termInfo = termState.value
        if (termInfo is LoadResult.Ready && termInfo.data != null) {
            runCatching {
                courseRepository.sync(termInfo.data.year, termInfo.data.term)
            }.onFailure {
                Log.e("HomeSync", "Sync Courses failed: $it")
            }
        }
    }

    companion object {
        private const val animationDuration: Long = 300
    }
}