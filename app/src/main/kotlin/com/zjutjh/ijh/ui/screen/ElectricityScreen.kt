package com.zjutjh.ijh.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zjutjh.ijh.R
import com.zjutjh.ijh.ui.component.BackIconButton
import com.zjutjh.ijh.ui.component.IJhScaffold
import com.zjutjh.ijh.ui.theme.IJhTheme

@Composable
fun ElectricityRoute() {
    ElectricityScreen()
}

@Composable
private fun ElectricityScreen() {
    ElectricityScaffold(onBackClick = { /*TODO*/ }) {
        Card {
            Text(stringResource(id = R.string.dorm_electricity))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ElectricityScaffold(
    onBackClick: () -> Unit,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    IJhScaffold(topBar = {
        ElectricityTopBar(
            scrollBehavior = it,
            onBackClick = onBackClick,
        )
    }, content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ElectricityTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.dorm_electricity))
        },
        navigationIcon = {
            BackIconButton(onBackClick)
        },
        scrollBehavior = scrollBehavior,
    )
}

@Preview
@Composable
private fun ElectricityScreenPreview() {
    IJhTheme {
        ElectricityScreen()
    }
}