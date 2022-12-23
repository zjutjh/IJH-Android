package com.zjutjh.ijh.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.R
import com.zjutjh.ijh.data.Course
import com.zjutjh.ijh.data.Section
import com.zjutjh.ijh.ui.theme.IJHTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek

@Composable
fun ScheduleCard(courses: ImmutableList<Course>, modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.schedule),
            modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        if (courses.isEmpty()) {
            Text(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                text = stringResource(id = R.string.nothing_to_do)
            )
        } else {
            CoursesCards(
                courses = courses, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)
            )
        }
    }
}

@Composable
fun CoursesCards(courses: ImmutableList<Course>, modifier: Modifier = Modifier) {
    Column(modifier) {
        courses.forEach {
            Spacer(modifier = Modifier.padding(4.dp))
            CourseCard(course = it, onClick = {})
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
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            IconText(
                icon = Icons.Default.Place,
                contentDescription = stringResource(id = R.string.place),
                text = course.place
            )
            IconText(
                icon = Icons.Default.Schedule,
                contentDescription = stringResource(id = R.string.time),
                text = course.shortTime
            )
            IconText(
                icon = Icons.Default.Person,
                contentDescription = stringResource(id = R.string.teacher),
                text = course.teacher
            )
        }
    }
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
        Spacer(modifier = Modifier.padding(horizontal = 1.dp))
        Text(text, style = style)
    }
}

@Preview
@Composable
fun IconTextPreview() {
    IJHTheme {
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
    IJHTheme {
        Surface {
            CourseCard(
                course = Course(
                    "Design pattern in practice",
                    "Mr. Info",
                    "A3C2",
                    "8:00-9:40",
                    "1-16 Week | Mon (1-2) | 8:00-9:40",
                    "4.0",
                    Section(1, 2),
                    DayOfWeek.MONDAY
                ),
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
fun ScheduleSurfacePreview() {
    IJHTheme {
        Surface {
            ScheduleCard(
                modifier = Modifier.padding(10.dp), courses = persistentListOf(
                    Course(
                        "Design pattern in practice",
                        "Mr. Info",
                        "Software.A.302",
                        "8:00-9:40",
                        "1-16 Week | Mon (1-2) | 8:00-9:40",
                        "4.0",
                        Section(1, 2),
                        DayOfWeek.MONDAY
                    ), Course(
                        "Software Engineering",
                        "Mr. Hex",
                        "Information.B.101",
                        "9:55-11:35",
                        "1-16 Week | Mon (1-2) | 8:00-9:40",
                        "4.0",
                        Section(1, 2),
                        DayOfWeek.MONDAY
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun ScheduleSurfaceEmptyPreview() {
    IJHTheme {
        Surface {
            ScheduleCard(
                modifier = Modifier.padding(10.dp), courses = persistentListOf()
            )
        }
    }
}