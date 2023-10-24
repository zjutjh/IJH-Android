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
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.material3.ColorProviders
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.component.shortTime
import com.zjutjh.ijh.ui.model.toTermDayState
import com.zjutjh.ijh.ui.theme.DarkColorScheme
import com.zjutjh.ijh.ui.theme.LightColorScheme
import com.zjutjh.ijh.util.toLocalizedString
import com.zjutjh.ijh.work.ScheduleWidgetUpdateWorker
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.Duration
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
            .map {
                if (it == null) null else {
                    Duration.between(it, ZonedDateTime.now()).toLocalizedString(context)
                }
            }

        provideContent {
            Content(courses, syncTime) {
                ScheduleWidgetUpdateWorker.enqueue(context)
            }
        }
    }

    @Composable
    fun Content(
        coursesFlow: Flow<List<Course>>,
        syncTimeFlow: Flow<String?>,
        onUpdate: () -> Unit
    ) {
        val courses by coursesFlow.collectAsState(null)
        val syncTime by syncTimeFlow.collectAsState(null)

        GlanceTheme(ColorProviders(LightColorScheme, DarkColorScheme)) {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(GlanceTheme.colors.background)
                    .appWidgetBackground()
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Vertical.CenterVertically
                    ) {
                        if (syncTime != null)
                            GText(
                                text = syncTime!!,
                            )
                        else
                            GText(text = "Never")

                        Spacer(GlanceModifier.defaultWeight())
                        Button(text = "Update", onClick = onUpdate, maxLines = 1)
                    }
                    Box(
                        GlanceModifier.padding(vertical = 10.dp)
                    ) {
                        Spacer(
                            GlanceModifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(GlanceTheme.colors.outline)
                        )
                    }
                    if (courses != null) {
                        if (courses!!.isEmpty()) {
                            GText("No course.")
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
                        GText("Loading...")
                    }
                }
            }
        }
    }
}

// TODO: Impl icon button
@Composable
private fun IconButton(
    provider: ImageProvider,
    onClick: () -> Unit
) {
    Box(
        modifier = GlanceModifier
            .background(GlanceTheme.colors.primaryContainer)
            .height(50.dp)
            .cornerRadius(10.dp)
            .clickable(onClick)
    ) {
        Image(
            provider = provider,
            contentDescription = "Image in button",
            modifier = GlanceModifier.fillMaxSize()
        )
    }
}

@Composable
private fun CourseItem(course: Course) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        GText("${course.name}-${course.teacherName}")
        GText("${course.shortTime()} | ${course.place}")
    }
}

