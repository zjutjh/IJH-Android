package com.zjutjh.ijh.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.R
import com.zjutjh.ijh.ui.theme.IJHTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)) {
    HomeScaffold(drawerState = drawerState) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ScheduleCard(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                courses = persistentListOf()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            HomeDrawerContent(onCloseButtonClick = {
                scope.launch {
                    drawerState.close()
                }
            })
        }, drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                HomeTopBar({
                    scope.launch { drawerState.open() }
                }, {})
            },
            content = content,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDrawerContent(onCloseButtonClick: () -> Unit) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier.padding(16.dp)
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
fun HomeTopBar(onMenuButtonClick: () -> Unit, onAccountButtonClick: () -> Unit) {
    TopAppBar(
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
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(id = R.string.account)
                )
            }
        },
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerPreview() {
    IJHTheme {
        HomeScreen(rememberDrawerState(initialValue = DrawerValue.Open))
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPreview() {
    IJHTheme {
        HomeScreen()
    }
}
