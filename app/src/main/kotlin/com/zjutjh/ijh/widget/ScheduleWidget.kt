package com.zjutjh.ijh.widget

import android.content.Context
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
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
import androidx.glance.text.FontWeight
import androidx.glance.text.TextAlign
import com.zjutjh.ijh.R
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.ui.component.shortTime
import com.zjutjh.ijh.ui.model.TermDayState
import com.zjutjh.ijh.ui.model.toTermDayState
import com.zjutjh.ijh.ui.theme.DarkColorScheme
import com.zjutjh.ijh.ui.theme.LightColorScheme
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ScheduleWidget : GlanceAppWidget() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Log.d("ScheduleWidget", "provideGlance called.")

        val entryPoint =
            EntryPointAccessors.fromApplication<ScheduleWidgetReceiver.Repositories>(context)

        val dayStateFlow = entryPoint.weJhInfoRepository.infoStream
            .map { it?.toTermDayState() }
        val coursesFlow = dayStateFlow.flatMapLatest {
            it ?: return@flatMapLatest flowOf(null)

            entryPoint.courseRepository
                .getCourses(it.year, it.term, it.week, it.dayOfWeek)
        }

        val syncTimeFlow = entryPoint.courseRepository.lastSyncTimeStream
            .map {
                it ?: return@map null
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(it.withZoneSameInstant(ZoneId.systemDefault()))
            }

        // Force update, or the widget will not show data when it is first created
        updateAll(context)

        provideContent {
            val termDay by dayStateFlow.collectAsState(initial = null)
            val courses by coursesFlow.collectAsState(initial = null)
            val syncTime by syncTimeFlow.collectAsState(initial = null)

            Content(termDay, courses, syncTime)
        }
    }

    @Composable
    fun Content(
        termDay: TermDayState?,
        courses: List<Course>?,
        syncTime: String?,
    ) {
        val context = LocalContext.current

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
                        Column {
                            // Title
                            val title = buildString {
                                if (termDay != null) {
                                    append(context.getString(R.string.schedule))
                                    append(" | ")
                                    if (termDay.isInTerm) {
                                        append(
                                            termDay.dayOfWeek.getDisplayName(
                                                java.time.format.TextStyle.SHORT,
                                                java.util.Locale.getDefault()
                                            )
                                        )
                                        append("(")
                                        append(termDay.week)
                                        append(")")
                                    } else {
                                        append(context.getString(R.string.during_vacation))
                                    }
                                }
                            }
                            GText(
                                text = title,
                                weight = FontWeight.Medium,
                                size = MaterialTheme.typography.labelLarge.fontSize
                            )

                            // Subtitle
                            GText(
                                text = "${context.getString(R.string.last_sync)} ${
                                    syncTime ?: context.getString(R.string.unknown)
                                }",
                                size = TextUnit(8f, TextUnitType.Sp)
                            )
                        }

                        Spacer(GlanceModifier.defaultWeight())

                        IconButton(
                            provider = ImageProvider(R.drawable.baseline_autorenew_24),
                            actionRunCallback<UpdateAction>()
                        )
                    }

                    // Divider
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

                    // Content
                    if (courses != null) {
                        if (courses.isEmpty()) {
                            GText(
                                context.getString(R.string.nothing_to_do),
                                modifier = GlanceModifier.fillMaxSize(),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn {
                                items(courses) {
                                    CourseItem(course = it)
                                }
                            }
                        }
                    } else {
                        GText(context.getString(R.string.loading))
                    }
                }
            }
        }
    }


    @Composable
    private fun CourseItem(course: Course) {
        Column {
            Column(
                modifier = GlanceModifier
                    .padding(8.dp)
                    .cornerRadius(5.dp)
                    .fillMaxWidth()
                    .background(GlanceTheme.colors.secondary)
            ) {
                val color = GlanceTheme.colors.onSecondary
                GText("${course.name}-${course.teacherName}", color)
                GText("${course.shortTime()} | ${course.place}", color)
            }
            // Padding
            Spacer(GlanceModifier.height(5.dp))
        }
    }

}