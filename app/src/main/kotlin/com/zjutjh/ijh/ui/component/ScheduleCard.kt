package com.zjutjh.ijh.ui.component

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.ui.model.TermDayState
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.util.shortTime
import com.zjutjh.ijh.util.toLocalizedString
import java.time.DayOfWeek
import java.time.Duration
import java.util.Locale

@Composable
fun ScheduleCard(
    courses: List<Course>?,
    termDay: TermDayState?,
    lastSyncDuration: Duration?,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val subtitle = remember(termDay, lastSyncDuration) {
        prompt(context, termDay, lastSyncDuration)
    }

    Card(
        modifier = modifier,
    ) {
        Row(
            Modifier
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                // Title
                Text(
                    text = stringResource(id = R.string.schedule),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                // Subtitle
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                )
            }

            FilledIconButton(
                onClick = onCalendarClick,
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarViewWeek,
                    contentDescription = stringResource(
                        id = R.string.calendar
                    )
                )
            }
        }

        AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = courses,
            contentAlignment = Alignment.Center,
            label = "Loading",
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
                    modifier = Modifier.padding(vertical = 24.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.nothing_to_do)
                )
            } else {
                CoursesCard(
                    modifier = Modifier.padding(16.dp),
                    courses = it,
                )
            }
        }
    }
}

private fun prompt(context: Context, termDay: TermDayState?, lastSyncDuration: Duration?) =
    buildString {
        val separator = " | "
        if (termDay != null) {
            if (termDay.isInTerm) {
                append(
                    termDay.dayOfWeek.getDisplayName(
                        java.time.format.TextStyle.SHORT,
                        Locale.getDefault()
                    )
                )
                append(separator)
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
        append(separator)
        if (lastSyncDuration != null) {
            append(lastSyncDuration.toLocalizedString(context))
        } else {
            append(context.getString(R.string.never))
        }
    }

@Composable
fun CoursesCard(
    modifier: Modifier = Modifier,
    courses: List<Course>,
) {
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
                courses = emptyList(),
                onCalendarClick = {},
                termDay = null,
                lastSyncDuration = Duration.ofDays(1),
            )
        }
    }
}

@Preview
@Composable
private fun ScheduleSurfaceLoadingPreview() {
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