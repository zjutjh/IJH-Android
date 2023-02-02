package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
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
                    val modifier1 = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                    for (i in 1..12) {
                        Row(
                            modifier = modifier1,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = i.toString(),
                                textAlign = TextAlign.Center
                            )
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                            )
                        }
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
                    dayOfWeek = DayOfWeek.THURSDAY,
                    locale = locale,
                    courses = courses
                )
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.WEDNESDAY,
                    locale = locale,
                    courses = courses
                )
                ClassScheduleRowItem(
                    dayOfWeek = DayOfWeek.TUESDAY,
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
    val elements = remember(courses) {
        courses.filter { it.dayOfWeek == dayOfWeek }
    }

    Layout(modifier = modifier, content = {
        elements.forEach { course ->
            ClassScheduleColumnItem(course = course)
        }
    }) { measurables, constraints ->
        val cellHeight = constraints.maxHeight / 12

        val placeables = measurables.mapIndexed { index, measurable ->
            val height =
                (elements[index].sectionEnd - elements[index].sectionStart + 1) * cellHeight

            measurable.measure(constraints.copy(minHeight = height, maxHeight = height))
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val yPosition = (elements[index].sectionStart - 1) * cellHeight
                placeable.placeRelative(0, yPosition)
            }
        }
    }
}

@Composable
fun ClassScheduleColumnItem(course: Course) {
    Surface(
        modifier = Modifier.padding(4.dp),
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(4.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = course.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
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