package com.zjutjh.ijh.widget

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.component.shortTime
import com.zjutjh.ijh.ui.model.toTermDayState
import com.zjutjh.ijh.work.ScheduleWidgetUpdateWorker
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime

// TODO: Unfinished yet, in early stages
class ScheduleWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint =
            EntryPointAccessors.fromApplication<ScheduleWidgetReceiver.Repositories>(context)

        val info = entryPoint.weJhInfoRepository.infoStream
            .map { it?.toTermDayState() }
            .first()
        val courses = if (info != null) {
            entryPoint.courseRepository
                .getCourses(info.year, info.term, info.week, info.dayOfWeek)
        } else flow { emit(emptyList()) }

        val syncTime = entryPoint.courseRepository.lastSyncTimeStream

        provideContent {
            Content(courses, syncTime) {
                ScheduleWidgetUpdateWorker.enqueue(context)
            }
        }
    }

    @Composable
    fun Content(
        coursesFlow: Flow<List<Course>>,
        syncTimeFlow: Flow<ZonedDateTime?>,
        onUpdate: () -> Unit
    ) {
        val courses by coursesFlow.collectAsState(null)
        val syncTime by syncTimeFlow.collectAsState(null)

        GlanceTheme {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .appWidgetBackground()
            ) {
                if (courses != null) {
                    Column {
                        if (syncTime != null) {
                            Row {
                                Button(text = "Update", onClick = onUpdate)
                                Text("Last sync: ${syncTime!!.toLocalTime()}")
                            }
                        }
                        if (courses!!.isEmpty()) {
                            Text("No course.")
                        } else {
                            Column(
                                modifier = GlanceModifier
                                    .cornerRadius(10.dp)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                            ) {
                                courses!!.forEach {
                                    CourseItem(course = it)
                                }
                            }
                        }
                    }
                } else {
                    Text("Not login")
                }
            }
        }
    }

    @Composable
    private fun CourseItem(course: Course) {
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text("${course.name}-${course.teacherName}")
            Text("${course.shortTime()} | ${course.place}")
        }
    }
}