package com.zjutjh.ijh.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.outlined.CalendarViewWeek
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.ui.component.BackIconButton
import com.zjutjh.ijh.ui.component.CourseCalendar
import com.zjutjh.ijh.ui.model.TermWeekState
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.ui.viewmodel.CourseCalendarViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun CourseCalendarRoute(
    viewModel: CourseCalendarViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val startYear by viewModel.startYear.collectAsStateWithLifecycle()
    val termView by viewModel.termView.collectAsStateWithLifecycle()
    val termState by viewModel.termState.collectAsStateWithLifecycle()
    val courses by viewModel.coursesState.collectAsStateWithLifecycle()
    val refreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    CourseCalendarScreen(
        startYear = startYear ?: 2020,
        courses = courses ?: persistentListOf(),
        termView = termView,
        currentTermWeek = termState.first,
        selectedTermWeek = termState.second,
        refreshing = refreshing,
        switchTermView = viewModel::switchTermView,
        onUnselect = viewModel::clearSelection,
        onSelectTerm = viewModel::selectTerm,
        onRefresh = viewModel::refresh,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun CourseCalendarScreen(
    startYear: Int,
    courses: ImmutableList<Course>,
    termView: Boolean,
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    refreshing: Boolean,
    switchTermView: () -> Unit,
    onUnselect: () -> Unit,
    onRefresh: () -> Unit,
    onSelectTerm: (TermWeekState, Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Highlight if it is in week view and the current week is selected
    val highlight =
        remember(
            termView,
            selectedTermWeek,
            currentTermWeek
        ) { !termView && (selectedTermWeek == null || selectedTermWeek == currentTermWeek) }

    val revocable = remember(
        termView,
        selectedTermWeek,
        currentTermWeek
    ) {
        if (selectedTermWeek == null) false
        else if (termView) !selectedTermWeek.equalsIgnoreWeek(currentTermWeek)
        else selectedTermWeek != currentTermWeek
    }

    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = onRefresh)

    CourseCalendarScaffold(
        modifier = Modifier.pullRefresh(pullRefreshState),
        startYear = startYear,
        currentTermWeek = currentTermWeek,
        selectedTermWeek = selectedTermWeek,
        termView = termView,
        revocable = revocable,
        switchTermView = switchTermView,
        onUnselect = onUnselect,
        onSelectTerm = onSelectTerm,
        onBackClick = onNavigateBack
    ) { paddingValues ->
        MainContainer(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
        ) {
            Box(contentAlignment = Alignment.TopCenter) {
                // Force recompose when courses changed
                key(courses) {
                    CourseCalendar(
                        modifier = Modifier.padding(10.dp),
                        courses = courses,
                        highlight = highlight,
                    )
                }
                key(refreshing) {
                    PullRefreshIndicator(
                        refreshing = refreshing,
                        state = pullRefreshState,
                        scale = true
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(maxHeight = constraints.minHeight))
        }
        layout(constraints.maxWidth, constraints.minHeight) {
            placeables.forEach {
                it.placeRelative(0, 0)
            }
        }
    }
}

@Composable
private fun CourseCalendarScaffold(
    modifier: Modifier = Modifier,
    startYear: Int,
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    termView: Boolean,
    revocable: Boolean,
    switchTermView: () -> Unit,
    onUnselect: () -> Unit,
    onSelectTerm: (TermWeekState, Boolean) -> Unit,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CourseCalendarTopBar(
                startYear,
                currentTermWeek,
                selectedTermWeek,
                termView,
                revocable,
                switchTermView,
                onUnselect,
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
private fun CourseCalendarTopBar(
    startYear: Int,
    currentTermWeek: TermWeekState?,
    selectedTermWeek: TermWeekState?,
    termView: Boolean,
    revocable: Boolean,
    switchTermView: () -> Unit,
    onUnselect: () -> Unit,
    onSelectTerm: (TermWeekState, Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    val termWeek = selectedTermWeek ?: currentTermWeek

    var pickerOpened by remember { mutableStateOf(false) }
    fun openPicker() {
        pickerOpened = true
    }

    fun closePicker() {
        pickerOpened = false
    }

    CenterAlignedTopAppBar(
        title = {
            if (termWeek == null) {
                MoreClickableText(
                    text = stringResource(id = R.string.unselected),
                    onClick = ::openPicker
                )
            } else if (termView) {
                MoreClickableText(
                    text = "${termWeek.year} (${termWeek.term.value})",
                    onClick = ::openPicker
                )
            } else if (termWeek.isInTerm) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous week button
                    IconButton(
                        onClick = { onSelectTerm(termWeek.previousWeek(), false) },
                        enabled = termWeek.hasPreviousWeek()
                    ) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
                    }
                    Text(
                        modifier = Modifier
                            .widthIn(min = 68.dp)
                            .clickable(onClick = ::openPicker),
                        text = stringResource(id = R.string.unit_week, termWeek.week),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                    // Next week button
                    IconButton(
                        onClick = { onSelectTerm(termWeek.nextWeek(), false) },
                        enabled = termWeek.hasNextWeek()
                    ) {
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            } else {
                MoreClickableText(
                    text = stringResource(id = R.string.during_vacation),
                    onClick = ::openPicker
                )
            }
        },
        navigationIcon = {
            BackIconButton(onBackClick)
        },
        actions = {
            if (revocable)
                IconButton(onClick = onUnselect) {
                    Icon(Icons.Default.Undo, null)
                }

            IconButton(onClick = switchTermView) {
                if (termView) {
                    Icon(Icons.Outlined.CalendarViewWeek, null)
                } else {
                    Icon(Icons.Outlined.DateRange, null)
                }
            }
        }
    )

    if (pickerOpened) {
        if (termView) {
            TermPicker(
                startYear = startYear,
                currentTermWeek = currentTermWeek,
                selectedTermWeek = selectedTermWeek,
                onDismiss = ::closePicker,
                onConfirm = {
                    onSelectTerm(it, true)
                    pickerOpened = false
                },
            )
        } else {
            WeekPicker(
                currentTermWeek = currentTermWeek,
                selectedTermWeek = selectedTermWeek,
                onDismiss = ::closePicker,
                onConfirm = {
                    onSelectTerm(it, false)
                    pickerOpened = false
                },
            )
        }
    }
}

@Composable
private fun MoreClickableText(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text)
        Icon(imageVector = Icons.Default.UnfoldMore, contentDescription = null)
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
            mutableIntStateOf(termWeek.year)
        else mutableIntStateOf(java.time.Year.now().value)
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
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Year picker
                val currentYear = remember {
                    currentTermWeek?.year ?: java.time.Year.now().value
                }
                val count = currentYear - startYear + 1
                val yearScrollState = rememberLazyListState()
                LazyColumn(
                    Modifier.weight(1f),
                    state = yearScrollState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(count) {
                        val year = currentYear - it
                        Text(
                            text = year.toString(),
                            modifier = Modifier
                                .clickable {
                                    selectedYear = year
                                }
                                .padding(16.dp),
                            fontWeight = FontWeight.Bold,
                            color = if (year == selectedYear) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }
                }

                // Term picker
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Term.entries.forEach { term ->
                        Text(
                            text = term.value,
                            modifier = Modifier
                                .clickable {
                                    selectedTerm = term
                                }
                                .padding(16.dp),
                            fontWeight = FontWeight.Bold,
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
        if (termWeek == null || !termWeek.isInTerm) mutableIntStateOf(1)
        else mutableIntStateOf(termWeek.week)
    }
    AlertDialog(
        modifier = Modifier.widthIn(max = 275.dp),
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
            // Week picker
            val weekScrollState =
                rememberLazyListState(initialFirstVisibleItemIndex = selectedWeek - 1)
            LazyColumn(
                Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 100.dp),
                state = weekScrollState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(TermWeekState.WEEK_END) {
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
                        fontWeight = FontWeight.Bold,
                        color = if (week == selectedWeek) MaterialTheme.colorScheme.primary else Color.Unspecified
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    IJhTheme {
        CourseCalendarTopBar(
            2019, null, null,
            termView = false,
            revocable = true,
            switchTermView = {},
            onSelectTerm = { _, _ -> },
            onUnselect = {},
        ) {}
    }
}

@Preview
@Composable
private fun CourseCalendarScreenPreview() {
    val courses = CourseRepositoryMock.getCourses().toImmutableList()
    IJhTheme {
        CourseCalendarScreen(2019, courses, false, null,
            null, false, {}, {}, {}, { _, _ -> }) {}
    }
}