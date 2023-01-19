package com.zjutjh.ijh.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.WeJhUserRepositoryMock
import com.zjutjh.ijh.ui.component.BackIconButton
import com.zjutjh.ijh.ui.component.IJhScaffold
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    ProfileScreen(
        userState = userState,
        onNavigateBack = onNavigateBack,
        onLogout = {
            viewModel.logout(onNavigateBack)
        }
    )
}

@Composable
private fun ProfileScreen(
    userState: com.zjutjh.ijh.model.WeJhUser?,
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    ProfileScaffold(onBackClick = onNavigateBack) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            AnimatedVisibility(visible = userState != null, enter = expandVertically()) {
                ElevatedCard(
                    modifier = Modifier
                        .widthIn(max = 450.dp)
                        .padding(16.dp)
                        .fillMaxWidth(0.9f),
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = userState!!.username,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            TextButton(onClick = onLogout) {
                Text(stringResource(id = R.string.sign_out))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScaffold(
    onBackClick: () -> Unit,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    IJhScaffold(
        topBar = {
            ProfileTopBar(it, onBackClick)
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.profile))
        },
        navigationIcon = {
            BackIconButton(onBackClick)
        },
        scrollBehavior = scrollBehavior,
    )
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    IJhTheme {
        val repo = WeJhUserRepositoryMock()
        ProfileScreen(repo.mockWeJhUser())
    }
}