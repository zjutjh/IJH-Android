package com.zjutjh.ijh.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.PullRefreshState
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.data.repository.mock.WeJhUserRepositoryMock
import com.zjutjh.ijh.ui.component.DividerBottomBar
import com.zjutjh.ijh.ui.component.ScheduleCard
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToClassSchedule: () -> Unit = {/* TODO */ },
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.refreshState,
        onRefresh = viewModel::refresh
    )

    HomeScaffold(
        loginState,
        drawerState,
        pullRefreshState,
        onNavigateToProfile,
        onNavigateToLogin,
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState),
            contentAlignment = Alignment.TopCenter
        ) {

            ScheduleCard(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                courses = viewModel.coursesState,
                onCalendarClick = onNavigateToClassSchedule,
            )

            PullRefreshIndicator(
                refreshing = viewModel.refreshState,
                state = pullRefreshState,
                scale = true
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    isLoggedIn: Boolean,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    pullRefreshState: PullRefreshState,
    onAccountButtonClick: () -> Unit,
    onLoginButtonClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    ModalNavigationDrawer(
        drawerContent = {
            HomeDrawerContent(onCloseButtonClick = {
                scope.launch {
                    drawerState.close()
                }
            })
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                HomeTopBar(
                    isLoggedIn,
                    {
                        scope.launch { drawerState.open() }
                    },
                    onAccountButtonClick,
                    onLoginButtonClick,
                    scrollBehavior,
                )
            },
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                DividerBottomBar()
            },
            content = content,
        )
    }
}

@Composable
fun HomeDrawerContent(onCloseButtonClick: () -> Unit) {
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
                IconButton(onClick = onCloseButtonClick) {
                    Icon(
                        imageVector = Icons.Default.MenuOpen,
                        contentDescription = null,
                    )
                }
            }

            NavigationDrawerItem(icon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = null)
            }, label = {
                Text(
                    text = "Home", modifier = Modifier.padding(horizontal = 16.dp)
                )
            }, selected = true, onClick = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isLoggedIn: Boolean,
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
            rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ }),
            {},
            {}) {}
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    IJhTheme {
        val viewModel = HomeViewModel(WeJhUserRepositoryMock(), CourseRepositoryMock())
        HomeScreen(viewModel, {}, {}) {}
    }
}

@Preview(heightDp = 400)
@Composable
private fun HomeScrollPreview() {
    IJhTheme {
        val viewModel = HomeViewModel(WeJhUserRepositoryMock(), CourseRepositoryMock())
        HomeScreen(viewModel, {}, {}) {}
    }
}
