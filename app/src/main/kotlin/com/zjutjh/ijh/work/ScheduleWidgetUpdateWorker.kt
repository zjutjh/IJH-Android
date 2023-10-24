package com.zjutjh.ijh.work

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkerParameters
import com.zjutjh.ijh.exception.UnauthorizedException
import com.zjutjh.ijh.widget.ScheduleWidget
import com.zjutjh.ijh.widget.ScheduleWidgetReceiver
import dagger.hilt.android.EntryPointAccessors

class ScheduleWidgetUpdateWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) :
    CoroutineWorker(context, workerParameters) {
    companion object {
        // Periodic work and one time work should have different unique names.
        const val UNIQUE_NAME = "ScheduleWidgetUpdater"
        const val PERIODIC_UNIQUE_NAME = "ScheduleWidgetPeriodicUpdater"

        fun enqueue(context: Context, force: Boolean = false) {
            val manager = androidx.work.WorkManager.getInstance(context)
            val request = androidx.work.OneTimeWorkRequestBuilder<ScheduleWidgetUpdateWorker>()
                .build()

            var policy = ExistingWorkPolicy.KEEP

            if (force) {
                policy = ExistingWorkPolicy.REPLACE
            }

            manager.enqueueUniqueWork(
                UNIQUE_NAME,
                policy,
                request
            )
        }
    }

    private val entryPoint =
        EntryPointAccessors.fromApplication<ScheduleWidgetReceiver.Repositories>(context)

    override suspend fun doWork(): Result {
        return try {
            val info = entryPoint.weJhInfoRepository.sync()
            entryPoint.courseRepository.sync(info.first, info.second)

            // Notify all existing widgets to update
            ScheduleWidget().updateAll(context)

            Result.success()
        } catch (e: UnauthorizedException) {
            // Not logged in, stop the worker
            Result.failure()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}