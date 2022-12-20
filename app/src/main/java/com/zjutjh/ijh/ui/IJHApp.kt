package com.zjutjh.ijh.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IJHApp() {
    val viewModel: HomeViewModel = viewModel()
    Scaffold { contentPadding ->
        Surface(
            Modifier.padding(contentPadding),
        ) {
            Column {
                ScheduleSurface(courses = viewModel.courses)
            }
        }
    }
}
