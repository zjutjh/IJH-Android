package com.zjutjh.ijh.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.outlined.CalendarViewWeek
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
import kotlinx.coroutines.launch

@Composable
fun ClassScheduleRoute(
    viewModel: ClassScheduleViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val startYear by viewModel.startYear.collectAsStateWithLifecycle()
    val termView by viewModel.termView.collectAsStateWithLifecycle()
    val termState by viewModel.termState.collectAsStateWithLifecycle()
    val courses by viewModel.coursesState.collectAsStateWithLifecycle()

    ClassScheduleScreen(
        startYear = startYear,
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
    startYear: Int,
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
        startYear = startYear,
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
    startYear: Int,
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
                startYear,
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
    startYear: Int,
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    termView: Boolean,
    switchTermView: () -> Unit,
    onSelectTerm: (TermWeekState) -> Unit,
    onBackClick: () -> Unit,
) {
    val termWeek = selectedTermWeek ?: currentTermWeek

    var openPicker by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .clickable { openPicker = true },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val title = if (termWeek == null) {
                    stringResource(id = R.string.unselected)
                } else if (termView) {
                    "${termWeek.year} (${termWeek.term.value})"
                } else if (termWeek.isInTerm) {
                    stringResource(id = R.string.unit_week, termWeek.week)
                } else {
                    stringResource(id = R.string.during_vacation)
                }
                Text(text = title)
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
                    Icon(Icons.Outlined.DateRange, null)
                }
            }
        }
    )

    if (openPicker) {
        if (termView) {
            TermPicker(
                startYear = startYear,
                currentTermWeek = currentTermWeek,
                selectedTermWeek = selectedTermWeek,
                onDismiss = { openPicker = false },
                onConfirm = {
                    onSelectTerm(it)
                    openPicker = false
                },
            )
        } else {
            WeekPicker(
                currentTermWeek = currentTermWeek,
                selectedTermWeek = selectedTermWeek,
                onDismiss = { openPicker = false },
                onConfirm = {
                    onSelectTerm(it)
                    openPicker = false
                },
            )
        }
    }
}

@Composable
fun TermPicker(
    startYear: Int,
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    onDismiss: () -> Unit,
    onConfirm: (TermWeekState) -> Unit,
) {
    val termWeek = selectedTermWeek ?: currentTermWeek

    var selectedYear by remember(currentTermWeek) {
        if (termWeek != null)
            mutableStateOf(termWeek.year)
        else mutableStateOf(java.time.Year.now().value)
    }
    var selectedTerm by remember(currentTermWeek) {
        if (termWeek != null)
            mutableStateOf(termWeek.term)
        else mutableStateOf(Term.FIRST)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onConfirm(
                    TermWeekState(
                        selectedYear,
                        selectedTerm,
                        termWeek?.week ?: 1,
                        true
                    )
                )
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
            // TODO: Optimizing by custom layout
            val dividerColor = MaterialTheme.colorScheme.outlineVariant
            Row(
                Modifier
                    .heightIn(max = 300.dp)
                    .drawBehind {
                        // Divider
                        val x = size.width / 2
                        drawLine(
                            color = dividerColor,
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }) {
                // Year picker
                val currentYear = remember {
                    currentTermWeek?.year ?: java.time.Year.now().value
                }
                val count = currentYear - startYear + 2
                val yearScrollState = rememberLazyListState()
                LazyColumn(
                    Modifier.weight(1f),
                    state = yearScrollState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(count) {
                        val year = currentYear + 1 - it
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

                // Term picker
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
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
            }
        },
    )
}

@Composable
fun WeekPicker(
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    onConfirm: (TermWeekState) -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val termWeek = selectedTermWeek ?: currentTermWeek
    var selectedWeek by remember(currentTermWeek) {
        if (termWeek == null || !termWeek.isInTerm) mutableStateOf(1)
        else mutableStateOf(termWeek.week)
    }
    AlertDialog(
        modifier = Modifier.widthIn(max = 250.dp),
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onConfirm(
                    TermWeekState(
                        termWeek?.year ?: java.time.Year.now().value,
                        termWeek?.term ?: Term.FIRST,
                        selectedWeek,
                        true
                    )
                )
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
            // TODO: Optimizing by custom layout
            Box(
                Modifier
                    .heightIn(max = 200.dp)
            ) {
                // Week picker
                val weekScrollState =
                    rememberLazyListState(initialFirstVisibleItemIndex = selectedWeek - 1)
                LazyColumn(
                    Modifier.fillMaxWidth(),
//                    contentPadding = PaddingValues(vertical = 80.dp),
                    state = weekScrollState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(20) {
                        val week = it + 1
                        Text(
                            text = stringResource(id = R.string.unit_week, week),
                            modifier = Modifier
                                .clickable {
                                    scope.launch {
                                        weekScrollState.animateScrollToItem(week - 1)
                                    }
                                    selectedWeek = week
                                }
                                .padding(16.dp),
                            color = if (week == selectedWeek) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    IJhTheme {
        ClassScheduleTopBar(2019, null, null, false, {}, {}) {}
    }
}

@Preview
@Composable
private fun ClassScheduleScreenPreview() {
    val courses = CourseRepositoryMock.getCourses()
    IJhTheme {
        ClassScheduleScreen(2019, courses, false, null, null, {}, {}) {}
    }
}