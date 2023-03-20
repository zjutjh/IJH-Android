package com.zjutjh.ijh.ui.component

import android.content.res.Configuration.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Section
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.ui.model.TermDayState
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.util.toLocalizedString
import java.time.DayOfWeek
import java.time.Duration
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScheduleCard(
    courses: List<Course>?,
    termDay: TermDayState?,
    lastSyncDuration: Duration?,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    OutlinedCard(
        modifier = modifier,
    ) {
        Column(Modifier.padding(top = 12.dp, start = 24.dp, end = 24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.schedule),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onCalendarClick
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarViewWeek,
                        contentDescription = stringResource(
                            id = R.string.calendar
                        )
                    )
                }
            }

            Divider()
            Spacer(modifier = Modifier.height(4.dp))

            val prompt = remember(termDay, lastSyncDuration) {
                buildString {
                    if (termDay != null) {
                        if (termDay.isInTerm) {
                            append(
                                context.getString(
                                    R.string.unit_week,
                                    termDay.week
                                )
                            )
                        } else {
                            append(
                                context.getString(R.string.during_vacation)
                            )
                        }
                    } else {
                        append(
                            context.getString(R.string.unknown)
                        )
                    }
                    append(" | ")
                    if (lastSyncDuration != null) {
                        append(lastSyncDuration.toLocalizedString(context))
                    } else {
                        append(context.getString(R.string.never))
                    }
                }
            }

            Text(
                text = prompt,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = courses,
            contentAlignment = Alignment.Center
        ) {
            if (it == null) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(id = R.string.loading),
                        textAlign = TextAlign.Center
                    )
                }
            } else if (it.isEmpty()) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.nothing_to_do)
                )
            } else {
                CoursesCard(
                    courses = it,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun CoursesCard(courses: List<Course>, modifier: Modifier = Modifier) {
    var openDialog by remember { mutableStateOf(false) }
    var chosenCourse: Course by remember {
        mutableStateOf(Course.default())
    }

    if (openDialog) {
        CourseDetailsDialog(onConfirm = { openDialog = false }, chosenCourse = chosenCourse)
    }

    ElevatedCard(modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            courses.forEachIndexed { index, it ->
                if (index != 0)
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                CourseListItem(
                    modifier = Modifier.fillMaxWidth(),
                    course = it,
                    onClick = { chosenCourse = it; openDialog = true })
            }
        }
    }
}

@Composable
private fun CourseListItem(course: Course, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        IconText(
            icon = Icons.Default.Place,
            contentDescription = stringResource(id = R.string.place),
            text = course.place,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        IconText(
            icon = Icons.Default.Book,
            contentDescription = stringResource(id = R.string.course),
            text = course.name
        )
        IconText(
            icon = Icons.Default.Schedule,
            contentDescription = stringResource(id = R.string.time),
            text = course.shortTime()
        )
        IconText(
            icon = Icons.Default.Person,
            contentDescription = stringResource(id = R.string.teacher),
            text = course.teacherName
        )
    }

}

fun Course.shortTime(): String {
    val (start, _) = Section.PAIRS[sectionStart - 1]
    val (_, end) = Section.PAIRS[sectionEnd - 1]

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return "${start.format(formatter)} - ${end.format(formatter)} | $sectionStart-$sectionEnd"
}

fun Course.detailedTime(): String {
    val (start, _) = Section.PAIRS[sectionStart - 1]
    val (_, end) = Section.PAIRS[sectionEnd - 1]

    val startTime = start.format(Section.TIME_FORMATTER)
    val endTime = end.format(Section.TIME_FORMATTER)

    val locale = LocaleListCompat.getDefault()[0]

    val dayOfWeek = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, locale)
    return "${dayOfWeek}($sectionStart-$sectionEnd) | $startTime-$endTime"
}

@Composable
fun IconText(
    icon: ImageVector,
    text: String,
    contentDescription: String? = null,
    fontWeight: FontWeight? = null,
    style: TextStyle = TextStyle.Default
) {
    val id = "icon"
    val annotatedString = buildAnnotatedString {
        appendInlineContent(id, "[icon]")
        append(text)
    }
    val inlineContent = mapOf(
        id to InlineTextContent(
            Placeholder(
                width = 18.sp,
                height = 1.em,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
            )
        }
    )

    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        style = style,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Preview
@Composable
private fun CourseCardPreview() {
    IJhTheme {
        Surface {
            CourseListItem(
                course = CourseRepositoryMock.getCourse(),
                onClick = {}
            )
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ScheduleSurfacePreview() {
    val termDay = TermDayState(2023, Term.FIRST, 1, true, DayOfWeek.MONDAY)
    IJhTheme {
        Surface {
            ScheduleCard(
                modifier = Modifier.padding(10.dp),
                courses = CourseRepositoryMock.getCourses(),
                onCalendarClick = {},
                termDay = termDay,
                lastSyncDuration = Duration.ofDays(2),
            )
        }
    }
}

@Preview
@Composable
private fun ScheduleSurfaceEmptyPreview() {
    IJhTheme {
        Surface {
            ScheduleCard(
                modifier = Modifier.padding(10.dp),
                courses = null,
                onCalendarClick = {},
                termDay = null,
                lastSyncDuration = Duration.ofDays(1),
            )
        }
    }
}