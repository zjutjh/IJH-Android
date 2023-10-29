package com.zjutjh.ijh.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.mock.WeJhUserRepositoryMock
import com.zjutjh.ijh.model.WeJhUser
import com.zjutjh.ijh.ui.component.BackIconButton
import com.zjutjh.ijh.ui.component.IJhScaffold
import com.zjutjh.ijh.ui.component.TextListItem
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ProfileScreen(
    userState: WeJhUser?,
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    ProfileScaffold(onBackClick = onNavigateBack) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (userState != null)
                ElevatedCard(
                    modifier = Modifier
                        .widthIn(max = 450.dp)
                        .padding(16.dp)
                        .fillMaxWidth(0.9f),
                ) {
                    TextListItem(title = R.string.student_id, text = userState.studentId)
                    ListItem(
                        headlineContent = {
                            Text(text = stringResource(id = R.string.bind))
                        },
                        supportingContent = {
                            FlowRow {
                                FilterChip(
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    selected = userState.bind.zf,
                                    onClick = { /*TODO*/ },
                                    label = {
                                        Text(text = stringResource(id = R.string.zf_account))
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = null,
                                        )
                                    }
                                )
                                FilterChip(
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    selected = userState.bind.lib,
                                    onClick = { /*TODO*/ },
                                    label = {
                                        Text(text = stringResource(id = R.string.lib_account))
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = null,
                                        )
                                    }
                                )
                                FilterChip(
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    selected = userState.bind.yxy,
                                    onClick = { /*TODO*/ },
                                    label = {
                                        Text(text = stringResource(id = R.string.yxy_account))
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = null,
                                        )
                                    }
                                )
                            }
                        }
                    )
                }

            TextButton(
                onClick = onLogout,
            ) {
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