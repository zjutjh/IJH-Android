package com.zjutjh.ijh.widget

import android.content.Context
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
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
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.WeJhInfoRepository
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.component.shortTime
import com.zjutjh.ijh.ui.model.toTermDayState
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

// TODO: Unfinished yet
class ScheduleWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ScheduleWidgetEntryPoint {
        val courseRepository: CourseRepository
        val weJhInfoRepository: WeJhInfoRepository
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val hiltEntryPoint = EntryPointAccessors.fromApplication<ScheduleWidgetEntryPoint>(context)

        val info = hiltEntryPoint.weJhInfoRepository.infoStream
            .map { it?.toTermDayState() }
            .first()
        val courses = if (info != null) {
            hiltEntryPoint.courseRepository
                .getCourses(info.year, info.term)
                .map { courses ->
                    courses.filter {
                        it.weeks.contains(info.week) && it.dayOfWeek == info.dayOfWeek
                    }
                }
        } else flow { emit(emptyList()) }

        Log.d("ScheduleWidget", "provideGlance: $info")

        provideContent {
            Log.d("ScheduleWidget", "provideGlance: $info")
            Content(courses)
        }
    }

    @Composable
    fun Content(coursesFlow: Flow<List<Course>>) {
        val courses by coursesFlow.collectAsState(null)

        GlanceTheme {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .appWidgetBackground()
            ) {
                if (courses != null) {
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