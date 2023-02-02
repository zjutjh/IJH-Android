package com.zjutjh.ijh.ui.component

import android.content.res.Configuration.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
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
                    fontWeight = FontWeight.Black
                )
                IconButton(
                    onClick = onCalendarClick
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
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
                        append(
                            context.getString(
                                R.string.unit_week,
                                termDay.week
                            )
                        )
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
            icon = Icons.Default.Book,
            contentDescription = stringResource(id = R.string.course),
            text = course.name,
            fontWeight = FontWeight.Black,
            style = MaterialTheme.typography.titleMedium
        )
        IconText(
            icon = Icons.Default.Place,
            contentDescription = stringResource(id = R.string.place),
            text = course.place
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
    val (start, _) = Course.SECTIONS[sectionStart - 1]
    val (_, end) = Course.SECTIONS[sectionEnd - 1]

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return "${start.format(formatter)} - ${end.format(formatter)} | $sectionStart-$sectionEnd"
}

fun Course.detailedTime(): String {
    val (start, _) = Course.SECTIONS[sectionStart - 1]
    val (_, end) = Course.SECTIONS[sectionEnd - 1]

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val startTime = start.format(formatter)
    val endTime = end.format(formatter)

    val locale = LocaleListCompat.getDefault()[0]

    val dayOfWeek = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, locale)
    return "${dayOfWeek}($sectionStart-$sectionEnd) | $startTime-$endTime"
}

@Composable
fun IconText(
    icon: ImageVector,
    contentDescription: String?,
    text: String,
    fontWeight: FontWeight? = null,
    style: TextStyle = TextStyle.Default
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 1.5.dp))
        Text(
            text,
            style = style,
            fontWeight = fontWeight,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
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
    val termDay = TermDayState(2023, Term.FIRST, 1, DayOfWeek.MONDAY, true)
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