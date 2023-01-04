package com.zjutjh.ijh.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.WeJhUserRepositoryMock
import com.zjutjh.ijh.ui.model.CancellableLoadingState
import com.zjutjh.ijh.ui.theme.IJhTheme

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), onCloseClick: () -> Unit) {
    LoginScaffold(
        snackbarHostState = viewModel.uiState.snackbarHostState,
        loadingState = viewModel.uiState.loading,
        onCloseClick = onCloseClick,
        onContinueClick = viewModel::loginOrCancel,
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            contentAlignment = Alignment.TopCenter
        ) {
            AnimatedVisibility(
                visible = viewModel.uiState.loading.isLoading(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            Column(
                Modifier
                    .widthIn(max = 450.dp)
                    .padding(top = 24.dp)
                    .fillMaxWidth(0.9f),
            ) {
                Icon(
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth(),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                LoginFormTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    icon = Icons.Default.Person,
                    label = stringResource(id = R.string.username),
                    placeholder = stringResource(id = R.string.input_username),
                    value = viewModel.uiState.username,
                    onValueChange = viewModel.uiState::updateUsername,
                    isError = viewModel.uiState.isUsernameError
                )
                LoginFormTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    icon = Icons.Default.Password,
                    label = stringResource(id = R.string.password),
                    placeholder = stringResource(id = R.string.input_password),
                    value = viewModel.uiState.password,
                    onValueChange = viewModel.uiState::updatePassword,
                    isError = viewModel.uiState.isPasswordError
                )
                Row {
                    TextButton(
                        modifier = Modifier.offset(x = (-8).dp),
                        onClick = { /* TODO */ },
                    ) {
                        Text(stringResource(id = R.string.login_forgot))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFormTextField(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconContentDescriptor: String? = null,
    label: String,
    placeholder: String,
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        leadingIcon = { Icon(imageVector = icon, contentDescription = iconContentDescriptor) },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        value = value,
        isError = isError,
        onValueChange = onValueChange,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScaffold(
    snackbarHostState: SnackbarHostState,
    loadingState: CancellableLoadingState,
    onCloseClick: () -> Unit,
    onContinueClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LoginTopBar(loadingState, scrollBehavior, onCloseClick, onContinueClick)
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopBar(
    loadingState: CancellableLoadingState,
    scrollBehavior: TopAppBarScrollBehavior?,
    onCloseClick: () -> Unit,
    onContinueClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.sign_in))
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close),
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(),
        actions = {
            val enabled =
                loadingState == CancellableLoadingState.READY || loadingState == CancellableLoadingState.CANCELLABLE
            TextButton(
                modifier = Modifier.animateContentSize(),
                onClick = onContinueClick,
                enabled = enabled,
            ) {
                val text = when (loadingState) {
                    CancellableLoadingState.CANCELLABLE -> stringResource(id = R.string.cancel)
                    else -> stringResource(R.string.continue_str)
                }
                Text(text)
            }
            Spacer(Modifier.width(16.dp))
        },
        scrollBehavior = scrollBehavior,
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenPreview() {
    IJhTheme {
        val viewModel = LoginViewModel(WeJhUserRepositoryMock())
        LoginScreen(viewModel) {}
    }
}

@Preview(heightDp = 300)
@Composable
private fun LoginScrollPreview() {
    IJhTheme {
        val viewModel = LoginViewModel(WeJhUserRepositoryMock())
        LoginScreen(viewModel) {}
    }
}