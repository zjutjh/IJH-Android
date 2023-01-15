package com.zjutjh.ijh.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.WeJhUserRepositoryMock
import com.zjutjh.ijh.ui.component.BackIconButton
import com.zjutjh.ijh.ui.component.DividerBottomBar
import com.zjutjh.ijh.ui.model.CancellableLoadingState
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.ui.viewmodel.LoginViewModel
import com.zjutjh.ijh.ui.viewmodel.PasswordUiState
import com.zjutjh.ijh.ui.viewmodel.UsernameUiState
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateContinue: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    LoginScaffold(
        snackbarHostState = viewModel.uiState.snackbarHostState,
        loadingState = viewModel.uiState.loading,
        onBackClick = onNavigateBack,
        onClick = focusManager::clearFocus,
        onActionClick = {
            focusManager.clearFocus()
            if (viewModel.checkUsername() && viewModel.checkPassword()) {
                viewModel.loginOrCancel(context, onNavigateContinue)
            }
        },
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
                        .height(100.dp)
                        .fillMaxWidth(),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                LoginFormTextField(modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Person,
                    label = stringResource(id = R.string.username),
                    placeholder = stringResource(id = R.string.input_username),
                    value = viewModel.uiState.username,
                    onValueChange = viewModel::updateUsername,
                    isError = viewModel.uiState.usernameUiState != UsernameUiState.OK,
                    supportingText = {
                        val text: String = when (viewModel.uiState.usernameUiState) {
                            UsernameUiState.OK -> String()
                            UsernameUiState.UNKNOWN -> stringResource(id = R.string.unknown_user)
                            UsernameUiState.INVALID -> stringResource(id = R.string.invalid_username)
                        }
                        Text(text)
                    })
                LoginFormTextField(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Password,
                    label = stringResource(id = R.string.password),
                    placeholder = stringResource(id = R.string.input_password),
                    value = viewModel.uiState.password,
                    onValueChange = viewModel::updatePassword,
                    isError = viewModel.uiState.passwordUiState != PasswordUiState.OK,
                    supportingText = {
                        val text: String = when (viewModel.uiState.passwordUiState) {
                            PasswordUiState.OK -> String()
                            PasswordUiState.WRONG -> stringResource(id = R.string.wrong_password)
                            PasswordUiState.INVALID -> stringResource(id = R.string.invalid_password)
                        }
                        Text(text)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Row {
                    var enabled by remember { mutableStateOf(true) }

                    TextButton(
                        modifier = Modifier.offset(x = (-8).dp),
                        enabled = enabled,
                        onClick = {
                            // TODO
                            scope.launch {
                                enabled = false
                                viewModel.showDismissibleSnackbar(
                                    context.getString(R.string.not_supported)
                                )
                                enabled = true
                            }
                        },
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
private fun LoginFormTextField(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconContentDescriptor: String? = null,
    label: String,
    placeholder: String,
    value: String,
    isError: Boolean,
    supportingText: @Composable () -> Unit,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        modifier = modifier,
        leadingIcon = { Icon(imageVector = icon, contentDescription = iconContentDescriptor) },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        value = value,
        isError = isError,
        supportingText = supportingText,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScaffold(
    snackbarHostState: SnackbarHostState,
    loadingState: CancellableLoadingState,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .pointerInput(onClick) { detectTapGestures { onClick() } }
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LoginTopBar(loadingState, scrollBehavior, onBackClick, onActionClick)
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            DividerBottomBar()
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginTopBar(
    loadingState: CancellableLoadingState,
    scrollBehavior: TopAppBarScrollBehavior?,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.sign_in))
        },
        navigationIcon = {
            BackIconButton(onBackClick)
        },
        actions = {
            val enabled =
                loadingState == CancellableLoadingState.READY || loadingState == CancellableLoadingState.CANCELLABLE

            TextButton(
                modifier = Modifier.animateContentSize(),
                onClick = onActionClick,
                enabled = enabled,
            ) {
                val text = when (loadingState) {
                    CancellableLoadingState.CANCELLABLE -> stringResource(id = R.string.cancel)
                    else -> stringResource(R.string.continue_str)
                }
                Text(text)
            }
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
        LoginScreen(viewModel, {}) {}
    }
}

@Preview(heightDp = 300)
@Composable
private fun LoginScrollPreview() {
    IJhTheme {
        val viewModel = LoginViewModel(WeJhUserRepositoryMock())
        LoginScreen(viewModel, {}) {}
    }
}