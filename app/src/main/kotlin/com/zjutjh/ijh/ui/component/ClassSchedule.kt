package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.os.LocaleListCompat
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.util.stackConflict
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@Composable
fun ClassSchedule(modifier: Modifier = Modifier, courses: List<Course>) {
    val locale = remember { LocaleListCompat.getDefault()[0] }

    Surface(modifier = modifier) {
        Box {
            // Left section bar
            Column(
                Modifier
                    .width(30.dp)
                    .zIndex(1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                ) {
                    Divider()
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            val modifier1 = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                            for (i in 1..12) {
                                Box(
                                    modifier = modifier1,
                                    contentAlignment = Alignment.Center,
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

            // Table
            ClassScheduleRow(
                startPadding = 30.dp,
            ) {
                // Monday
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.MONDAY,
                    locale = locale!!,
                    courses = courses
                )
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.TUESDAY,
                    locale = locale,
                    courses = courses
                )
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.WEDNESDAY,
                    locale = locale,
                    courses = courses
                )
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.THURSDAY,
                    locale = locale,
                    courses = courses
                )
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.FRIDAY,
                    locale = locale,
                    courses = courses
                )
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.SATURDAY,
                    locale = locale,
                    courses = courses
                )
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.SUNDAY,
                    locale = locale,
                    courses = courses
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
        val padding = startPadding.toPx().toInt()
        val width =
            (constraints.minWidth - padding) / 5 // Only 5 items are visible

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
    dayOfWeek: DayOfWeek,
    locale: Locale,
    courses: List<Course>
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, locale),
                textAlign = TextAlign.Center
            )
        }
        Divider()
        ClassScheduleColumn(
            courses = courses,
            dayOfWeek = dayOfWeek
        )
    }
}

@Composable
fun ClassScheduleColumn(
    modifier: Modifier = Modifier,
    courses: List<Course>,
    dayOfWeek: DayOfWeek
) {
    val elements: List<Triple<List<Course>, Int, Int>> = remember {
        courses.stackConflict(dayOfWeek)
    }

    var chosenCourses by remember { mutableStateOf(emptyList<Course>()) }
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
        val cellHeight = constraints.minHeight / 12

        val placeables = measurables.mapIndexed { index, measurable ->
            val element = elements[index]
            val height =
                (element.third - element.second + 1) * cellHeight

            measurable.measure(constraints.copy(minHeight = height, maxHeight = height))
        }

        layout(constraints.maxWidth, constraints.minHeight) {
            placeables.forEachIndexed { index, placeable ->
                val element = elements[index]
                val yPosition = (element.second - 1) * cellHeight
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
        BadgedBox(
            badge = {
                Badge(
                    modifier = Modifier.offset((-12).dp, (12).dp)
                ) {
                    Text(
                        text = courseStack.first.size.toString(),
                    )
                }
            },
        ) {
            ScheduleCardContent(
                courses = courseStack.first,
                onClick = { onClick(courseStack.first) },
                span = span
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleCardContent(courses: List<Course>, onClick: () -> Unit, span: Int) {
    ElevatedCard(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        onClick = onClick,
    ) {
        Column(Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
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
        ClassSchedule(courses = courses)
    }
}