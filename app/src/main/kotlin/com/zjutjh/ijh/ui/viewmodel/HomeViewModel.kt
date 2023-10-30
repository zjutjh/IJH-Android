package com.zjutjh.ijh.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjutjh.ijh.data.CampusInfoRepository
import com.zjutjh.ijh.data.CardInfoRepository
import com.zjutjh.ijh.data.CourseRepository
import com.zjutjh.ijh.data.WeJhUserRepository
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Session
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.ui.model.TermDayState
import com.zjutjh.ijh.ui.model.toTermDayState
import com.zjutjh.ijh.util.LoadResult
import com.zjutjh.ijh.util.asLoadResultStateFlow
import com.zjutjh.ijh.util.isLoading
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
    private val campusInfoRepository: CampusInfoRepository,
    private val cardInfoRepository: CardInfoRepository,
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

    val loginState: StateFlow<Session?> = weJhUserRepository.sessionStream
        .distinctUntilChanged()
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val termLocalRefreshChannel: MutableStateFlow<Unit> = MutableStateFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    val termDayState: StateFlow<LoadResult<TermDayState?>> = campusInfoRepository.infoStream
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
        .dropWhile(LoadResult<*>::isLoading)
        .distinctUntilChanged(LoadResult<*>::isEqual)
        .flatMapLatest { state ->
            if (state is LoadResult.Ready && state.data != null) {
                val day = state.data
                if (day.isInTerm) {
                    courseRepository.getCourses(day.year, day.term, day.week, day.dayOfWeek)
                } else flowOf(emptyList())
            } else flowOf(emptyList())
        }
        .flowOn(Dispatchers.Default)
        .asLoadResultStateFlow(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    val cardBalanceState: StateFlow<Pair<String, Duration>?> = cardInfoRepository.balanceStream
        .distinctUntilChanged()
        .combine(timerFlow) { t, _ -> t }
        .map {
            if (it == null) null else {
                Pair(it.first, Duration.between(it.second, ZonedDateTime.now()))
            }
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            // Check session state, renew if needed. TODO: move to application scope
            val session = loginState.first()
            if (session != null) {
                val duration = Duration.between(ZonedDateTime.now(), session.expiresAt)
                Log.v("Home", "WeJH Session state: $duration; Negative: ${duration.isNegative}.")
                if (duration.isNegative) {
                    weJhUserRepository.logout()
                    Log.i("Home", "WeJH session expired, logged out.")
                } else if (duration.toDays() in 0..2) {
                    try {
                        weJhUserRepository.renewSession()
                        Log.i("Home", "WeJH Session renewed.")
                    } catch (e: Exception) {
                        Log.e("Home", "Failed to renew WeJH Session.", e)
                    }
                }
            }
            // Subscribe latest login state, and trigger refresh.
            loginState.collectLatest {
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
        val timer = scope.async { delay(300L) }

        val term = refreshTerm()

        val isLoggedIn = loginState.value
        if (isLoggedIn != null) {
            if (term != null) {
                refreshCourse(term.first, term.second)
            }
            refreshCard()
        }

        Log.i("Home", "Synchronization complete.")

        timer.await()
        _refreshState.update { false }
    }

    private suspend fun refreshTerm(): Pair<Int, Term>? {
        runCatching { campusInfoRepository.sync() }
            .fold({
                Log.i("Home", "Sync WeJhInfo succeed.")
                return it
            }) {
                Log.e("Home", "Sync WeJhInfo failed.", it)
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
            Log.e("Home", "Sync Courses failed.", it)
        }
    }

    private suspend fun refreshCard() {
        runCatching {
            cardInfoRepository.sync()
        }.fold({
            Log.i("Home", "Sync Card succeed.")
        }) {
            Log.e("Home", "Sync Card failed.", it)
        }
    }

}