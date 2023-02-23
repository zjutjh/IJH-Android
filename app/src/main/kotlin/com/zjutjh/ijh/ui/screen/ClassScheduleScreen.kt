package com.zjutjh.ijh.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.outlined.CalendarViewWeek
import androidx.compose.material.icons.outlined.FilterNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
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
        switchTermView = viewModel::switchTermView,
        onSelectTerm = viewModel::selectTerm,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun ClassScheduleScreen(
    courses: List<Course>?,
    termView: Boolean,
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    switchTermView: () -> Unit,
    onSelectTerm: (TermWeekState) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Highlight if it is in week view and the current week is selected
    val highlight =
        remember(
            termView,
            selectedTermWeek,
            currentTermWeek
        ) { !termView && (selectedTermWeek == null || selectedTermWeek == currentTermWeek) }

    ClassScheduleScaffold(
        currentTermWeek = currentTermWeek,
        selectedTermWeek = selectedTermWeek,
        termView = termView,
        switchTermView = switchTermView,
        onSelectTerm = onSelectTerm,
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

@Composable
private fun ClassScheduleScaffold(
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    termView: Boolean,
    switchTermView: () -> Unit,
    onSelectTerm: (TermWeekState) -> Unit,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            ClassScheduleTopBar(
                currentTermWeek,
                selectedTermWeek,
                termView,
                switchTermView,
                onSelectTerm,
                onBackClick
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassScheduleTopBar(
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    termView: Boolean,
    switchTermView: () -> Unit,
    onSelectTerm: (TermWeekState) -> Unit,
    onBackClick: () -> Unit,
) {
    val termWeek = selectedTermWeek ?: currentTermWeek

    var openTermPicker by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .clickable { openTermPicker = true },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (termWeek == null) {
                    Text(stringResource(id = R.string.unselected))
                } else if (termView) {
                    Text(text = "${termWeek.year} (${termWeek.term.value})")
                } else if (termWeek.isInTerm) {
                    Text(stringResource(id = R.string.unit_week, termWeek.week))
                } else {
                    Text(stringResource(id = R.string.during_vacation))
                }

                Icon(imageVector = Icons.Default.UnfoldMore, contentDescription = null)
            }
        },
        navigationIcon = {
            BackIconButton(onBackClick)
        },
        actions = {
            IconButton(onClick = switchTermView) {
                if (termView) {
                    Icon(Icons.Outlined.CalendarViewWeek, null)
                } else {
                    Icon(Icons.Outlined.FilterNone, null)
                }
            }
        }
    )

    if (openTermPicker) {
        TermPicker(
            currentTermWeek = currentTermWeek,
            selectedTermWeek = selectedTermWeek,
            onDismiss = { openTermPicker = false },
            onConfirm = {
                onSelectTerm(it)
                openTermPicker = false
            },
        )
    }
}

@Composable
fun TermPicker(
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    onDismiss: () -> Unit,
    onConfirm: (TermWeekState) -> Unit,
) {
    val termWeek = selectedTermWeek ?: currentTermWeek

    var selectedYear by remember {
        if (termWeek != null)
            mutableStateOf(termWeek.year)
        else mutableStateOf(java.time.Year.now().value)
    }
    var selectedTerm by remember {
        if (termWeek != null)
            mutableStateOf(termWeek.term)
        else mutableStateOf(Term.FIRST)
    }
    var selectedWeek by remember {
        if (termWeek == null || !termWeek.isInTerm) mutableStateOf(1)
        else mutableStateOf(termWeek.week)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(TermWeekState(selectedYear, selectedTerm, selectedWeek, true))
            }) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }
        },
        text = {
            Row(Modifier.heightIn(max = 300.dp)) {
                // Year picker
                LazyColumn(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentTermWeek != null) {
                        items(6) {
                            val year = currentTermWeek.year - 4 + it
                            Text(
                                text = year.toString(),
                                modifier = Modifier
                                    .clickable {
                                        selectedYear = year
                                    }
                                    .padding(16.dp),
                                color = if (year == selectedYear) MaterialTheme.colorScheme.primary else Color.Unspecified
                            )
                        }
                    }
                }
                // Term picker
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Term.values().forEach { term ->
                        Text(
                            text = term.value,
                            modifier = Modifier
                                .clickable {
                                    selectedTerm = term
                                }
                                .padding(16.dp),
                            color = if (term == selectedTerm) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }
                }
                // Week picker
                LazyColumn(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(20) {
                        val week = it + 1
                        Text(
                            text = week.toString(),
                            modifier = Modifier
                                .clickable {
                                    selectedWeek = week
                                }
                                .padding(16.dp),
                            color = if (week == selectedWeek) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    IJhTheme {
        ClassScheduleTopBar(null, null, false, {}, {}) {}
    }
}

@Preview
@Composable
private fun ClassScheduleScreenPreview() {
    val courses = CourseRepositoryMock.getCourses()
    IJhTheme {
        ClassScheduleScreen(courses, false, null, null, {}, {}) {}
    }
}