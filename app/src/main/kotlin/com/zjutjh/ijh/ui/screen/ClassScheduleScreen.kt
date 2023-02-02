package com.zjutjh.ijh.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.component.BackIconButton
import com.zjutjh.ijh.ui.component.ClassSchedule
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.ui.viewmodel.ClassScheduleViewModel
import com.zjutjh.ijh.util.LoadResult

@Composable
fun ClassScheduleRoute(
    viewModel: ClassScheduleViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val coursesState by viewModel.coursesState.collectAsStateWithLifecycle()
    val courses = when (val state = coursesState) {
        is LoadResult.Loading -> null
        is LoadResult.Ready -> state.data
    }

    ClassScheduleScreen(courses = courses, onNavigateBack = onNavigateBack)
}

@Composable
private fun ClassScheduleScreen(
    courses: List<Course>?,
    onNavigateBack: () -> Unit
) {
    ClassScheduleScaffold(onBackClick = onNavigateBack) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (courses == null) {
                CircularProgressIndicator()
            } else {
                ClassSchedule(courses = courses)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassScheduleScaffold(
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { ClassScheduleTopBar(onBackClick) },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassScheduleTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.class_schedule))
        },
        navigationIcon = {
            BackIconButton(onBackClick)
        },
    )
}

@Preview
@Composable
private fun ClassScheduleScreenPreview() {
    val courses = CourseRepositoryMock.getCourses()
    IJhTheme {
        ClassScheduleScreen(courses) {}
    }
}