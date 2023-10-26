package com.zjutjh.ijh.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.WeJhInfoRepository
import com.zjutjh.ijh.work.ScheduleWidgetUpdater
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Duration

/**
 * Implementation of App Widget functionality.
 */
class ScheduleWidgetReceiver : GlanceAppWidgetReceiver() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface Repositories {
        val courseRepository: CourseRepository
        val weJhInfoRepository: WeJhInfoRepository
    }

    override val glanceAppWidget = ScheduleWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        // Enter relevant functionality for when the first widget is created
        val manager = WorkManager.getInstance(context)
        val request = PeriodicWorkRequestBuilder<ScheduleWidgetUpdater>(
            Duration.ofHours(6), Duration.ofMinutes(30)
        ).setConstraints(
            Constraints(requiresBatteryNotLow = true)
        ).build()

        manager.enqueueUniquePeriodicWork(
            ScheduleWidgetUpdater.PERIODIC_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

        Log.i("ScheduleWidget", "Updater enqueued.")
    }

    override fun onDisabled(context: Context) {
        super.onEnabled(context)
        // Enter relevant functionality for when the last widget is disabled
        val manager = WorkManager.getInstance(context)
        manager.cancelUniqueWork(ScheduleWidgetUpdater.PERIODIC_UNIQUE_NAME)

        Log.i("ScheduleWidget", "Updater canceled.")
    }
}
