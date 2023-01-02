package com.zjutjh.ijh.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zjutjh.ijh.data.repository.mock.WeJhUserRepositoryMock
import com.zjutjh.ijh.ui.theme.IJhTheme

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), onCloseClick: () -> Unit) {
    LoginScaffold(onCloseClick, onContinueClick = viewModel::login) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(vertical = 18.dp))
            Column(
                Modifier
                    .widthIn(Dp.Unspecified, 450.dp)
                    .fillMaxWidth(0.9f),
            ) {
                LoginFormTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    icon = Icons.Default.AccountCircle,
                    label = "Username",
                    placeholder = "Input username",
                    value = viewModel.uiState.username,
                    onValueChange = viewModel::updateUsername
                )
                LoginFormTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    icon = Icons.Default.Password,
                    label = "Password",
                    placeholder = "Input password",
                    value = viewModel.uiState.password,
                    onValueChange = viewModel::updatePassword
                )
                Row {
                    TextButton(
                        modifier = Modifier.offset(x = (-8).dp),
                        onClick = { /* TODO */ },
                    ) {
                        Text("Forgot?")
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
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        leadingIcon = { Icon(imageVector = icon, contentDescription = iconContentDescriptor) },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        value = value,
        onValueChange = onValueChange,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScaffold(
    onCloseClick: () -> Unit,
    onContinueClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            LoginTopBar(onCloseClick, onContinueClick)
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopBar(onCloseClick: () -> Unit, onContinueClick: () -> Unit) {
    TopAppBar(
        title = {
            Text("Sign in")
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(),
        actions = {
            TextButton(
                onClick = onContinueClick
            ) {
                Text("Continue")
            }
        },
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview() {
    IJhTheme {
        val viewModel = LoginViewModel(WeJhUserRepositoryMock())
        LoginScreen(viewModel) {}
    }
}