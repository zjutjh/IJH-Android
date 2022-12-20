package com.zjutjh.ijh.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Section
import com.zjutjh.ijh.ui.theme.IJHTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek

@Composable
fun ScheduleSurface(courses: ImmutableList<Course>, modifier: Modifier = Modifier) {
    HomeSurface(modifier = modifier) {
        Column {
            Text(
                text = "Schedule",
                modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            if (courses.isEmpty()) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Nothing to do!"
                )
            } else {
                CoursesCards(
                    courses = courses,
                    modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)
                )
            }
        }
    }
}

@Composable
fun CoursesCards(courses: ImmutableList<Course>, modifier: Modifier = Modifier) {
    Column(modifier) {
        courses.forEach {
            Spacer(modifier = Modifier.padding(5.dp))
            CourseCard(course = it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCard(course: Course, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        onClick = {},
    ) {
        Column(Modifier.padding(10.dp)) {
            IconText(
                icon = Icons.Default.Book,
                text = course.name,
                style = MaterialTheme.typography.titleMedium
            )
            Divider(modifier = Modifier.padding(vertical = 5.dp))
            IconText(icon = Icons.Default.Place, text = course.place)
            IconText(icon = Icons.Default.Schedule, text = course.shortTime)
            IconText(icon = Icons.Default.Person, text = course.teacher)
        }
    }
}

@Composable
fun IconText(icon: ImageVector, text: String, style: TextStyle = TextStyle.Default) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = "Teacher",
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
            IconText(icon = Icons.Default.Person, text = "Person Orange")
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
                )
            )
        }
    }
}

@Preview
@Composable
fun ScheduleSurfacePreview() {
    IJHTheme {
        Surface {
            ScheduleSurface(
                courses = persistentListOf(
                    Course(
                        "Design pattern in practice",
                        "Mr. Info",
                        "Software.A.302",
                        "8:00-9:40",
                        "1-16 Week | Mon (1-2) | 8:00-9:40",
                        "4.0",
                        Section(1, 2),
                        DayOfWeek.MONDAY
                    ),
                    Course(
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
            ScheduleSurface(courses = persistentListOf())
        }
    }
}