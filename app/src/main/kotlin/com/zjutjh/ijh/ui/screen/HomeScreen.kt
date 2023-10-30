package com.zjutjh.ijh.ui.screen

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.PullRefreshState
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.ui.component.CampusCardInfoCard
import com.zjutjh.ijh.ui.component.IJhScaffold
import com.zjutjh.ijh.ui.component.ScheduleCard
import com.zjutjh.ijh.ui.model.TermDayState
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.ui.viewmodel.HomeViewModel
import com.zjutjh.ijh.util.LoadResult
import com.zjutjh.ijh.util.emptyFun
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Duration

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val refreshState by viewModel.refreshState.collectAsState()
    val coursesState by viewModel.coursesState.collectAsStateWithLifecycle()
    val termDayState by viewModel.termDayState.collectAsStateWithLifecycle()
    val coursesLastSyncState by viewModel.courseLastSyncState.collectAsStateWithLifecycle()
    val cardBalanceState by viewModel.cardBalanceState.collectAsStateWithLifecycle()
    val cardBalanceLastSyncState by viewModel.cardBalanceLastSyncState.collectAsStateWithLifecycle()

    val isLoggedIn = when (loginState) {
        null -> false
        else -> true
    }

    val courses = when (val state = coursesState) {
        is LoadResult.Loading -> null
        is LoadResult.Ready -> state.data
    }

    val termDay = when (val state = termDayState) {
        is LoadResult.Loading -> null
        is LoadResult.Ready -> state.data
    }

    HomeScreen(
        refreshing = refreshState,
        isLoggedIn = isLoggedIn,
        courses = courses,
        termDay = termDay,
        coursesLastSync = coursesLastSyncState,
        cardBalance = cardBalanceState,
        cardBalanceLastSync = cardBalanceLastSyncState,
        onRefresh = viewModel::refreshAll,
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToCalendar = onNavigateToCalendar,
        onNavigateToAbout = onNavigateToAbout,
    )
}

@Composable
private fun HomeScreen(
    refreshing: Boolean,
    isLoggedIn: Boolean?,
    courses: List<Course>?,
    termDay: TermDayState?,
    coursesLastSync: Duration?,
    cardBalance: LoadResult<String?>,
    cardBalanceLastSync: LoadResult<Duration?>,
    onRefresh: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    // To discard drawer state on recompose.
    // Should use `remember` instead of `rememberDrawerState`
    val drawerState = remember { DrawerState(initialValue = DrawerValue.Closed) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh,
    )

    HomeScaffold(
        isLoggedIn,
        drawerState,
        pullRefreshState,
        onNavigateToProfile,
        onNavigateToLogin,
        onNavigateToCalendar,
        onNavigateToAbout,
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            val modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
            ScheduleCard(
                modifier = modifier,
                courses = courses,
                termDay = termDay,
                onButtonClick = onNavigateToCalendar,
                lastSync = coursesLastSync,
            )
            CampusCardInfoCard(
                modifier = modifier,
                balance = cardBalance,
                lastSync = cardBalanceLastSync,
            )
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            scale = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScaffold(
    isLoggedIn: Boolean?,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    pullRefreshState: PullRefreshState,
    onAccountButtonClick: () -> Unit,
    onLoginButtonClick: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToAbout: () -> Unit,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            HomeDrawerContent(
                onNavigateToCalendar = onNavigateToCalendar,
                onNavigateToAbout = onNavigateToAbout,
                onClose = {
                    scope.launch { drawerState.close() }
                },
            )
        },
        drawerState = drawerState,
    ) {
        IJhScaffold(
            modifier = Modifier
                .pullRefresh(pullRefreshState),
            topBar = {
                HomeTopBar(
                    isLoggedIn,
                    {
                        scope.launch { drawerState.open() }
                    },
                    onAccountButtonClick,
                    onLoginButtonClick,
                    it,
                )
            },
            content = content
        )
    }
}

@Composable
private fun HomeDrawerContent(
    onNavigateToCalendar: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onClose: () -> Unit,
) {
    ModalDrawerSheet(modifier = Modifier.widthIn(max = 300.dp)) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.MenuOpen,
                        contentDescription = stringResource(id = R.string.collapse),
                    )
                }
            }

            HomeNavigationDrawerItem(
                icon = Icons.Default.ViewWeek,
                label = R.string.calendar,
                onClick = onNavigateToCalendar,
            )
            Divider()
            HomeNavigationDrawerItem(
                icon = Icons.Default.Info,
                label = R.string.about,
                onClick = onNavigateToAbout
            )
        }
    }
}

@Composable
private fun HomeNavigationDrawerItem(
    icon: ImageVector,
    @StringRes label: Int,
    onClick: () -> Unit,
    contentDescription: String? = null,
) {
    NavigationDrawerItem(
        icon = {
            Icon(imageVector = icon, contentDescription = contentDescription)
        },
        label = {
            Text(
                text = stringResource(id = label),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        selected = false,
        onClick = onClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    isLoggedIn: Boolean?,
    onMenuButtonClick: () -> Unit,
    onAccountButtonClick: () -> Unit,
    onLoginButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = onMenuButtonClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.navigation)
                )
            }
        },
        actions = {
            if (isLoggedIn != null) {
                if (isLoggedIn) {
                    IconButton(onClick = onAccountButtonClick) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = stringResource(id = R.string.account),
                        )
                    }
                } else {
                    IconButton(onClick = onLoginButtonClick) {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = stringResource(id = R.string.login),
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationDrawerPreview() {
    IJhTheme {
        HomeScaffold(
            true,
            drawerState = DrawerState(initialValue = DrawerValue.Open),
            rememberPullRefreshState(refreshing = false, onRefresh = ::emptyFun),
            {}, {}, {}, {}
        ) {}
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    val courses = CourseRepositoryMock.getCourses()
    val termDay = TermDayState(2023, Term.FIRST, 1, true, DayOfWeek.MONDAY)
    IJhTheme {
        HomeScreen(
            refreshing = false,
            isLoggedIn = true,
            courses = courses,
            termDay = termDay,
            coursesLastSync = Duration.ofDays(1),
            cardBalance = LoadResult.Ready("123"),
            cardBalanceLastSync = LoadResult.Ready(Duration.ofDays(2)),
            onRefresh = ::emptyFun,
            onNavigateToAbout = ::emptyFun,
            onNavigateToCalendar = ::emptyFun,
            onNavigateToLogin = ::emptyFun,
            onNavigateToProfile = ::emptyFun,
        )
    }
}
