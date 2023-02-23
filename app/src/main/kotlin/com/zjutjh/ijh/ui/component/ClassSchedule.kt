package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.util.stackConflict
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt
import kotlin.time.toKotlinDuration

@Composable
fun ClassSchedule(modifier: Modifier = Modifier, courses: List<Course>, highlight: Boolean) {
    val locale = remember { LocaleListCompat.getDefault()[0]!! }

    // Indicator refresh clock
    var dateTime by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(highlight) {
        if (highlight) {
            val duration = Duration.ofSeconds(5).toKotlinDuration()
            while (true) {
                delay(duration)
                dateTime = LocalDateTime.now()
            }
        }
    }

    val section = Course.currentSection(dateTime.toLocalTime())

    Surface(modifier = modifier) {
        Box {
            ClassScheduleRow(
                startPadding = 30.dp,
            ) {
                val today = dateTime.dayOfWeek

                DayOfWeek.values().forEachIndexed { index, dayOfWeek ->
                    ClassScheduleRowItem(
                        title = dayOfWeek.getDisplayName(TextStyle.SHORT, locale),
                        courses = courses.filter { it.dayOfWeek == dayOfWeek },
                        leftDivider = index == 0,
                        highlight = highlight && dayOfWeek == today,
                    )
                }
            }

            Divider(Modifier.offset(y = 30.dp))

            // Current section proportion indicator
            if (highlight) {
                if (section.first > 0 || (section.first == 0 && section.second > 0))
                    Divider(modifier = Modifier.layout { measurable, constraints ->
                        val paddingTop = 30.dp.toPx()
                        val paddingStart = 30.dp.toPx()
                        val thickness = 1.dp.toPx()

                        val cellHeight = (constraints.maxHeight - paddingTop) / 12
                        val y = if (section.second < 0) {
                            paddingTop + cellHeight * section.first
                        } else {
                            paddingTop + cellHeight * section.first + (cellHeight - thickness) * section.second
                        }
                        val placeable =
                            measurable.measure(constraints.copy(maxWidth = constraints.maxWidth - paddingStart.roundToInt()))
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(paddingStart.roundToInt(), y.roundToInt())
                        }
                    }, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
            }

            // Left section bar
            Row(
                modifier = Modifier
                    .width(30.dp)
                    .padding(top = 31.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
            ) {
                Column(Modifier.weight(1f)) {
                    val modifier1 = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                    for (i in 1..12) {
                        Column(
                            modifier = if (section.second >= 0 && section.first == i - 1)
                                modifier1.background(
                                    MaterialTheme.colorScheme.secondaryContainer
                                ) else modifier1,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = i.toString(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun ClassScheduleRow(
    modifier: Modifier = Modifier,
    startPadding: Dp,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()

    Layout(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .horizontalScrollbar(scrollState, startPadding),
        content = content
    ) { measurables, constraints ->
        val padding = startPadding.roundToPx()
        val width = (constraints.minWidth - padding) / 5 // Only 5 items are visible

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
        }

        layout((width * 7) + padding, constraints.maxHeight) {
            var xPosition = padding

            placeables.forEach { placeable ->
                placeable.placeRelative(xPosition, 0)
                xPosition += placeable.width
            }
        }
    }
}

@Composable
fun ClassScheduleRowItem(
    modifier: Modifier = Modifier,
    title: String,
    courses: List<Course>,
    leftDivider: Boolean = false,
    highlight: Boolean = false,
) {
    Column(
        modifier = if (highlight) {
            val colorFrom = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
            val colorTo = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)

            modifier.background(
                Brush.verticalGradient(
                    Pair(0f, colorFrom),
                    Pair(1f, colorTo),
                ),
            )
        } else modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(31.dp),
            contentAlignment = Alignment.Center
        ) {
            if (leftDivider) {
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .offset(x = (-1).dp)
                        .align(Alignment.TopStart)
                )
            }

            Text(
                text = title,
                textAlign = TextAlign.Center
            )
        }
        ClassScheduleColumn(
            courses = courses,
        )
    }
}

@Composable
fun ClassScheduleColumn(
    modifier: Modifier = Modifier,
    courses: List<Course>,
) {
    val elements: List<Triple<List<Course>, Int, Int>> = remember(courses) {
        courses.stackConflict()
    }

    var chosenCourses by remember(courses) { mutableStateOf(emptyList<Course>()) }
    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        CourseDetailsDialog(
            onConfirm = { openDialog = false },
            chosenCourses = chosenCourses
        )
    }

    Layout(
        modifier = modifier.fillMaxHeight(),
        content = {
            elements.forEach { courses ->
                ClassScheduleColumnItem(courses) {
                    chosenCourses = it
                    openDialog = true
                }
            }
        },
    ) { measurables, constraints ->
        val cellHeight = constraints.minHeight / 12f

        val placeables = measurables.mapIndexed { index, measurable ->
            val element = elements[index]
            val height =
                ((element.third - element.second + 1) * cellHeight).roundToInt()

            measurable.measure(constraints.copy(minHeight = height, maxHeight = height))
        }

        layout(constraints.maxWidth, constraints.minHeight) {
            placeables.forEachIndexed { index, placeable ->
                val element = elements[index]
                val yPosition = ((element.second - 1) * cellHeight).roundToInt()
                placeable.placeRelative(0, yPosition)
            }
        }
    }
}

/**
 * The lowest level item in the schedule table.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassScheduleColumnItem(
    courseStack: Triple<List<Course>, Int, Int>,
    onClick: (List<Course>) -> Unit
) {
    val span = courseStack.third - courseStack.second

    if (courseStack.first.size == 1) {
        ScheduleCardContent(
            courses = courseStack.first,
            onClick = { onClick(courseStack.first) },
            span = span
        )
    } else if (courseStack.first.size > 1) {
        // Conflict
        Box {
            ScheduleCardContent(
                courses = courseStack.first,
                onClick = { onClick(courseStack.first) },
                span = span
            )

            Badge(
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Text(
                    text = courseStack.first.size.toString(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleCardContent(courses: List<Course>, onClick: () -> Unit, span: Int) {
    ElevatedCard(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(
            Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            val course = remember { courses.last() }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f + (span * 0.5f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = course.place,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun ClassSchedulePreview() {
    IJhTheme {
        val courses = CourseRepositoryMock.getCourses()
        ClassSchedule(courses = courses, highlight = true)
    }
}