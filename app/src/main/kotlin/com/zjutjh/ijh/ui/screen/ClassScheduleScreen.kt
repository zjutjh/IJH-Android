package com.zjutjh.ijh.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.zjutjh.ijh.ui.model.TermWeekState
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.ui.viewmodel.ClassScheduleViewModel

@Composable
fun ClassScheduleRoute(
    viewModel: ClassScheduleViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val termView by viewModel.termView.collectAsStateWithLifecycle()
    val termState by viewModel.termState.collectAsStateWithLifecycle()
    val courses by viewModel.coursesState.collectAsStateWithLifecycle()

    ClassScheduleScreen(
        courses = courses,
        termView = termView,
        currentTermWeek = termState.first,
        selectedTermWeek = termState.second,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun ClassScheduleScreen(
    courses: List<Course>?,
    termView: Boolean,
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    onNavigateBack: () -> Unit
) {
    // Highlight if it is in week view and the current week is selected
    val highlight = !termView && (selectedTermWeek == null || currentTermWeek == selectedTermWeek)

    ClassScheduleScaffold(
        termWeek = selectedTermWeek ?: currentTermWeek,
        onBackClick = onNavigateBack
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (courses != null)
                ClassSchedule(
                    courses = courses,
                    highlight = highlight,
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassScheduleScaffold(
    termWeek: TermWeekState?,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { ClassScheduleTopBar(termWeek, onBackClick) },
        contentWindowInsets = WindowInsets.safeDrawing,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassScheduleTopBar(
    termWeek: TermWeekState?,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .clickable { /*TODO*/ },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (termWeek == null) {
                    Text(stringResource(id = R.string.unselected))
                } else {
                    Text(stringResource(id = R.string.unit_week, termWeek.week))
                }
                Icon(imageVector = Icons.Default.UnfoldMore, contentDescription = null)
            }
        },
        navigationIcon = {
            BackIconButton(onBackClick)
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.FilterAlt, null)
            }
        }
    )
}

@Preview
@Composable
private fun ClassScheduleScreenPreview() {
    val courses = CourseRepositoryMock.getCourses()
    IJhTheme {
        ClassScheduleScreen(courses, false, null, null) {}
    }
}