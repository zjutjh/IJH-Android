package com.zjutjh.ijh.ui.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Badge
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.zjutjh.ijh.data.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Section
import com.zjutjh.ijh.ui.theme.Blue0
import com.zjutjh.ijh.ui.theme.Blue5
import com.zjutjh.ijh.ui.theme.Cyan0
import com.zjutjh.ijh.ui.theme.Desert
import com.zjutjh.ijh.ui.theme.Green0
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.ui.theme.Orange0
import com.zjutjh.ijh.ui.theme.Purple3
import com.zjutjh.ijh.ui.theme.RainbowChampagne
import com.zjutjh.ijh.ui.theme.RainbowCoral
import com.zjutjh.ijh.ui.theme.RainbowLavender
import com.zjutjh.ijh.ui.theme.RainbowLightBlue
import com.zjutjh.ijh.ui.theme.RainbowLightGreen
import com.zjutjh.ijh.ui.theme.RainbowLime
import com.zjutjh.ijh.ui.theme.RainbowOrange
import com.zjutjh.ijh.ui.theme.RainbowPeach
import com.zjutjh.ijh.ui.theme.RainbowPink
import com.zjutjh.ijh.ui.theme.RainbowViolet
import com.zjutjh.ijh.ui.theme.Red0
import com.zjutjh.ijh.ui.theme.Red2
import com.zjutjh.ijh.util.CourseStack
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import java.lang.Integer.min
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.TextStyle
import kotlin.math.roundToInt
import kotlin.time.toKotlinDuration

@Composable
fun CourseCalendar(
    modifier: Modifier = Modifier,
    courses: ImmutableList<Course>,
    highlight: Boolean
) {
    val locale = remember { LocaleListCompat.getDefault()[0]!! }
    val context = LocalContext.current
    val toast = remember {
        Toast.makeText(
            context,
            String(),
            Toast.LENGTH_SHORT
        )
    }
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
            CourseCalendarRow(
                startPadding = 30.dp,
            ) {
                val today = dateTime.dayOfWeek
                DayOfWeek.entries.forEachIndexed { index, dayOfWeek ->
                    val dayCourses = remember(courses) {
                        courses.filter { it.dayOfWeek == dayOfWeek }.toImmutableList()
                    }
                    CourseCalendarRowItem(
                        title = dayOfWeek.getDisplayName(TextStyle.SHORT, locale),
                        courses = dayCourses,
                        leftDivider = index == 0,
                        highlight = highlight && dayOfWeek == today,
                    )
                }
            }


            // Left section bar
            Surface(
                modifier = Modifier
                    .width(30.dp)
                    .padding(top = 30.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ) {
                Row {
                    Column(Modifier.weight(1f)) {
                        val commonModifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()

                        for (i in 0..11) {
                            val modifier1 =
                                if (highlight && section.second >= 0 && section.first == i) {
                                    commonModifier.background(
                                        MaterialTheme.colorScheme.secondaryContainer
                                    )
                                } else {
                                    commonModifier
                                }.clickable {
                                    // TODO: Replace Toast by local SnackBar
                                    val timePair = Section.PAIRS[i]
                                    toast.setText("${timePair.first} - ${timePair.second}")
                                    toast.show()
                                }

                            Column(
                                modifier = modifier1,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = (i + 1).toString(),
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

            Divider(Modifier.offset(y = 30.dp))

            // Current section proportion indicator
            if (highlight) {
                if (section.first > 0 || (section.first == 0 && section.second > 0))
                    Divider(modifier = Modifier.layout { measurable, constraints ->
                        val paddingTop = 30.dp.toPx()
                        val paddingStart = 30.dp.roundToPx()
                        val width = 1.dp.toPx()

                        val cellHeight: Float = (constraints.maxHeight - paddingTop) / 12
                        val y: Int = if (section.second < 0) {
                            paddingTop + cellHeight * section.first
                        } else {
                            paddingTop + cellHeight * section.first + (cellHeight - width) * section.second
                        }.roundToInt()
                        val placeable =
                            measurable.measure(constraints.copy(maxWidth = constraints.maxWidth - paddingStart))

                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(paddingStart, y)
                        }
                    }, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun CourseCalendarRow(
    modifier: Modifier = Modifier,
    startPadding: Dp,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()

    Layout(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(scrollState)
            .horizontalScrollbar(scrollState, startPadding),
        content = content
    ) { measurables, constraints ->
        val padding = startPadding.roundToPx()
        val width = (constraints.minWidth - padding) / 5 // Only 5 items are visible

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
        }
        layout((width * 7) + padding, constraints.minHeight) {
            var xPosition = padding

            placeables.forEach { placeable ->
                placeable.placeRelative(xPosition, 0)
                xPosition += placeable.width
            }
        }
    }
}

@Composable
private fun CourseCalendarRowItem(
    modifier: Modifier = Modifier,
    title: String,
    courses: ImmutableList<Course>,
    leftDivider: Boolean = false,
    highlight: Boolean = false,
) {
    val elements: ImmutableList<CourseStack> = remember(courses) {
        CourseStack.stackConflict(courses)
    }

    var chosenCourses: ImmutableList<Course> by remember(courses) { mutableStateOf(persistentListOf()) }
    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        CourseDetailsDialog(
            onConfirm = { openDialog = false },
            chosenCourses = chosenCourses
        )
    }

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
                .height(30.dp),
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

        // Enforce group recompose
        key(elements) {
            CourseCalendarColumn(courses = elements) {
                elements.forEachIndexed { index, courseStack ->
                    CourseCalendarColumnItem(
                        Modifier.layoutId(index),
                        courseStack,
                    ) {
                        chosenCourses = it
                        openDialog = true
                    }
                }
            }
        }
    }
}

@Composable
private fun CourseCalendarColumn(
    modifier: Modifier = Modifier,
    courses: ImmutableList<CourseStack>,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier.fillMaxHeight(),
        content = content,
    ) { measurables, constraints ->
        val cellHeight = constraints.minHeight / 12f

        val placeables = measurables.map { measurable ->
            val element = courses[measurable.layoutId as Int]
            val height =
                ((element.end - element.start + 1) * cellHeight).roundToInt()
            val yPosition = ((element.start - 1) * cellHeight).roundToInt()
            measurable.measure(
                constraints.copy(
                    minHeight = height,
                    maxHeight = height
                )
            ) to yPosition
        }

        layout(constraints.maxWidth, constraints.minHeight) {
            placeables.forEach { placeable ->
                placeable.first.placeRelative(0, placeable.second)
            }
        }
    }
}

/**
 * The lowest level item in the schedule table.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseCalendarColumnItem(
    modifier: Modifier = Modifier,
    courseStack: CourseStack,
    onClick: (ImmutableList<Course>) -> Unit
) {
    val span = courseStack.end - courseStack.start
    val courses = courseStack.courses

    if (courses.size == 1) {
        CalendarElement(
            modifier = modifier,
            courses = courses,
            onClick = { onClick(courses) },
            span = span
        )
    } else if (courseStack.courses.size > 1) {
        // Conflict
        Box(modifier = modifier) {
            CalendarElement(
                courses = courses,
                onClick = { onClick(courses) },
                span = span
            )

            Badge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 1.dp),
            ) {
                Text(
                    text = courses.size.toString(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarElement(
    modifier: Modifier = Modifier,
    courses: ImmutableList<Course>,
    onClick: () -> Unit,
    span: Int
) {
    ElevatedCard(
        modifier = modifier.padding(horizontal = 4.dp, vertical = 3.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall
    ) {
        val course = courses.last()
        val colorNumber = remember(course) {
            course.coloringHashCode().rem(courseColors.size.toUInt()).toInt()
        }

        Row {
            Box(
                Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(courseColors[colorNumber])
            )
            Column(
                Modifier.padding(1.dp),
                verticalArrangement = Arrangement.Center
            ) {

                CourseAndTeacherNameTextColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f + (span * 0.5f)),
                    courseName = course.name,
                    teacherName = course.teacherName,
                )

                Divider()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = course.place,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * Adaptive text column.
 */
@Composable
private fun CourseAndTeacherNameTextColumn(
    modifier: Modifier = Modifier,
    courseName: String,
    teacherName: String,
) {
    val textStyle = MaterialTheme.typography.labelSmall
    val density = LocalDensity.current
    val fontFamily = LocalFontFamilyResolver.current

    Layout(
        modifier = modifier,
        content =
        {
            Text(
                modifier = Modifier.layoutId(0),
                text = courseName,
                style = textStyle,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.layoutId(1),
                text = teacherName,
                style = textStyle,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
        },
        measurePolicy = { measurables, constraints ->
            val paragraphConstraint = Constraints(maxWidth = constraints.maxWidth)
            var p1 = Paragraph(courseName, textStyle, paragraphConstraint, density, fontFamily)
            var p2: Paragraph? =
                Paragraph(teacherName, textStyle, paragraphConstraint, density, fontFamily)

            while (p1.height + (p2?.height ?: 0f) > constraints.maxHeight) {
                if (p1.lineCount > 1 && p1.lineCount > (p2?.lineCount ?: 0)) {
                    p1 = Paragraph(
                        courseName,
                        textStyle,
                        paragraphConstraint,
                        density,
                        fontFamily,
                        maxLines = p1.lineCount - 1,
                        ellipsis = true
                    )
                } else if (p2 != null) {
                    p2 = if (p2.lineCount > 1)
                        Paragraph(
                            courseName,
                            textStyle,
                            paragraphConstraint,
                            density,
                            fontFamily,
                            maxLines = p1.lineCount - 1,
                            ellipsis = true
                        ) else null
                } else {
                    // Only one line remains.
                    break
                }
            }

            val h1 = min(p1.height.roundToInt(), constraints.maxHeight)
            val abovePlaceable = measurables.find { it.layoutId == 0 }!!.measure(
                constraints.copy(minHeight = h1, maxHeight = h1)
            )
            val belowPlaceable = if (p2 == null) null else {
                val h2 = p2.height.roundToInt()
                measurables.find { it.layoutId == 1 }?.measure(
                    constraints.copy(minHeight = h2, maxHeight = h2)
                )
            }

            layout(constraints.maxWidth, constraints.maxHeight) {
                if (belowPlaceable == null) {
                    abovePlaceable.placeRelative(
                        0,
                        (constraints.maxHeight - abovePlaceable.height) / 2
                    )
                } else {
                    val y =
                        (constraints.maxHeight - abovePlaceable.height - belowPlaceable.height) / 2
                    abovePlaceable.placeRelative(0, y)
                    belowPlaceable.placeRelative(0, y + abovePlaceable.height)
                }
            }
        }
    )
}

/**
 * For better hashing, the total number of colors should be a prime number.
 */
private val courseColors = listOf(
    RainbowPink,
    RainbowPeach,
    RainbowLightGreen,
    RainbowLightBlue,
    RainbowLavender,
    RainbowViolet,
    RainbowCoral,
    RainbowOrange,
    RainbowChampagne,
    RainbowLime,
    Green0,
    Blue0,
    Cyan0,
    Orange0,
    Desert,
    Red0,
    Blue5,
    Purple3,
    Red2,
)

@Preview
@Composable
private fun CourseCalendarPreview() {
    IJhTheme {
        val courses = CourseRepositoryMock.getCourses()
        CourseCalendar(courses = courses.toImmutableList(), highlight = true)
    }
}