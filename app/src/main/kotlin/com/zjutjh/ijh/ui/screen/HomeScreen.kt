package com.zjutjh.ijh.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.ui.component.DividerBottomBar
import com.zjutjh.ijh.ui.component.ScheduleCard
import com.zjutjh.ijh.ui.material.pullrefresh.PullRefreshIndicator
import com.zjutjh.ijh.ui.material.pullrefresh.pullRefresh
import com.zjutjh.ijh.ui.material.pullrefresh.rememberPullRefreshState
import com.zjutjh.ijh.ui.theme.IJhTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
) {
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.uiState.isRefreshing,
        onRefresh = viewModel::refresh
    )

    HomeScaffold(
        drawerState,
        onNavigateToLogin
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.TopCenter
        ) {

            ScheduleCard(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                courses = viewModel.uiState.courses,
                onCalendarClick = { /* TODO */ },
            )

            PullRefreshIndicator(
                refreshing = viewModel.uiState.isRefreshing,
                state = pullRefreshState,
                scale = true
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    onAccountButtonClick: () -> Unit,
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
//        gesturesEnabled = false,
        drawerState = drawerState,
    ) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                HomeTopBar(
                    {
                        scope.launch { drawerState.open() }
                    },
                    onAccountButtonClick,
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

@OptIn(ExperimentalMaterial3Api::class)
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
    onMenuButtonClick: () -> Unit,
    onAccountButtonClick: () -> Unit,
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
            IconButton(onClick = onAccountButtonClick) {
                Icon(
                    imageVector = Icons.Default.Login,
                    contentDescription = stringResource(id = R.string.account)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationDrawerPreview() {
    IJhTheme {
        HomeScaffold(
            drawerState = DrawerState(initialValue = DrawerValue.Open),
            {}) {}
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    IJhTheme {
        val viewModel = HomeViewModel(CourseRepositoryMock())
        HomeScreen(viewModel) {}
    }
}

@Preview(heightDp = 400)
@Composable
private fun HomeScrollPreview() {
    IJhTheme {
        val viewModel = HomeViewModel(CourseRepositoryMock())
        HomeScreen(viewModel) {}
    }
}
