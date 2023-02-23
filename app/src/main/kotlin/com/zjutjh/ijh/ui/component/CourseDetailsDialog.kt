package com.zjutjh.ijh.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.R
import com.zjutjh.ijh.model.Course

@Composable
fun CourseDetailsDialog(onConfirm: () -> Unit, chosenCourse: Course) {
    AlertDialog(
        onDismissRequest = onConfirm,
        title = {
            Text(text = chosenCourse.name)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .verticalScroll(scrollState)
                    .verticalScrollbar(scrollState)
            ) {
                TextListItem(
                    title = R.string.place,
                    text = "${chosenCourse.campus}-${chosenCourse.place}"
                )
                TextListItem(title = R.string.time, text = chosenCourse.detailedTime())
                TextListItem(title = R.string.weeks, text = chosenCourse.weeks.toString())
                TextListItem(title = R.string.teacher, text = chosenCourse.teacherName)
                TextListItem(title = R.string.class_str, text = chosenCourse.className)
                TextListItem(title = R.string.credits, text = chosenCourse.credits.toString())
                TextListItem(title = R.string.hours, text = chosenCourse.hours.toString())
            }
        },
    )
}

@Composable
fun CourseDetailsDialog(onConfirm: () -> Unit, chosenCourses: List<Course>) {
    if (chosenCourses.size == 1) {
        CourseDetailsDialog(onConfirm = onConfirm, chosenCourse = chosenCourses[0])
    } else if (chosenCourses.size > 1) {
        AlertDialog(
            onDismissRequest = onConfirm,
            title = {
                Text(text = stringResource(id = R.string.course_details))
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            text = {
                var state by remember { mutableStateOf(0) }
                Column {
                    ScrollableTabRow(selectedTabIndex = state) {
                        chosenCourses.forEachIndexed { index, course ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = {
                                    Text(
                                        text = course.name,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            )
                        }
                    }

                    val scrollState = rememberScrollState()
                    val chosenCourse by remember {
                        derivedStateOf {
                            chosenCourses[state]
                        }
                    }

                    Column(
                        modifier = Modifier
                            .heightIn(max = 400.dp)
                            .verticalScroll(scrollState)
                            .verticalScrollbar(scrollState)
                    ) {
                        TextListItem(
                            title = R.string.place,
                            text = "${chosenCourse.campus}-${chosenCourse.place}"
                        )
                        TextListItem(title = R.string.time, text = chosenCourse.detailedTime())
                        TextListItem(title = R.string.weeks, text = chosenCourse.weeks.toString())
                        TextListItem(title = R.string.teacher, text = chosenCourse.teacherName)
                        TextListItem(title = R.string.class_str, text = chosenCourse.className)
                        TextListItem(
                            title = R.string.credits,
                            text = chosenCourse.credits.toString()
                        )
                        TextListItem(title = R.string.hours, text = chosenCourse.hours.toString())
                    }
                }
            },
        )
    }
}

@Composable
private fun TextListItem(@StringRes title: Int, text: String) {
    ListItem(
        headlineContent = {
            Text(text = stringResource(id = title))
        },
        supportingContent = {
            Text(text = text)
        }
    )
    Divider()
}