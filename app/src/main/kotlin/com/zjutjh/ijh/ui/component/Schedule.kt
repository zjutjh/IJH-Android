package com.zjutjh.ijh.ui.component

import android.content.res.Configuration.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.model.Course
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.ui.theme.IJhTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScheduleCard(
    courses: ImmutableList<Course>,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.schedule),
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = stringResource(id = R.string.today_class_schedule),
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(
                modifier = Modifier.padding(top = 16.dp, end = 10.dp),
                onClick = onCalendarClick
            ) {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null)
            }
        }

        AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = courses,
            contentAlignment = Alignment.Center
        ) {
            if (it.isEmpty()) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    text = stringResource(id = R.string.nothing_to_do)
                )
            } else {
                CoursesCards(
                    courses = it, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)
                )
            }
        }
    }
}

@Composable
fun CoursesCards(courses: ImmutableList<Course>, modifier: Modifier = Modifier) {
    Column(modifier) {
        courses.forEach {
            Spacer(modifier = Modifier.padding(4.dp))
            CourseCard(course = it, onClick = { /* TODO */ })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCard(course: Course, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(Modifier.padding(16.dp)) {
            IconText(
                icon = Icons.Default.Book,
                contentDescription = stringResource(id = R.string.course),
                text = course.name,
                style = MaterialTheme.typography.titleMedium
            )
            Divider(modifier = Modifier.padding(vertical = 6.dp))
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
}

fun Course.shortTime(): String {
    val (start, _) = Course.SECTIONS[sectionStart - 1]
    val (_, end) = Course.SECTIONS[sectionEnd - 1]

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return "${start.format(formatter)} - ${end.format(formatter)} | $sectionStart-$sectionEnd"
}

@Composable
fun IconText(
    icon: ImageVector,
    contentDescription: String?,
    text: String,
    style: TextStyle = TextStyle.Default
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 1.5.dp))
        Text(text, style = style, overflow = TextOverflow.Ellipsis, maxLines = 1)
    }
}

@Preview
@Composable
fun IconTextPreview() {
    IJhTheme {
        Surface {
            IconText(
                icon = Icons.Default.Person,
                contentDescription = "Person",
                text = "Person Orange"
            )
        }
    }
}

@Preview
@Composable
fun CourseCardPreview() {
    IJhTheme {
        Surface {
            CourseCard(
                course = CourseRepositoryMock.getCourse(),
                onClick = {}
            )
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ScheduleSurfacePreview() {
    IJhTheme {
        Surface {
            ScheduleCard(
                modifier = Modifier.padding(10.dp),
                courses = CourseRepositoryMock.getCourses(),
                onCalendarClick = {}
            )
        }
    }
}

@Preview
@Composable
fun ScheduleSurfaceEmptyPreview() {
    IJhTheme {
        Surface {
            ScheduleCard(
                modifier = Modifier.padding(10.dp),
                courses = persistentListOf(),
                onCalendarClick = {}
            )
        }
    }
}