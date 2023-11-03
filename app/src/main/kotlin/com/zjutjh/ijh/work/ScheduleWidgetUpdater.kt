package com.zjutjh.ijh.work

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkerParameters
import com.zjutjh.ijh.exception.ApiResponseException
import com.zjutjh.ijh.exception.WeJhApiExceptions
import com.zjutjh.ijh.widget.ScheduleWidget
import com.zjutjh.ijh.widget.ScheduleWidgetReceiver
import dagger.hilt.android.EntryPointAccessors

/**
 * Worker that syncs data and updates widgets.
 *
 * **Note** On some devices, periodic update will work only when auto launch is enabled.
 */
class ScheduleWidgetUpdater(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    companion object {
        // Periodic work and one time work should have different unique names.
        const val UNIQUE_NAME = "ScheduleWidgetUpdater"
        const val PERIODIC_UNIQUE_NAME = "ScheduleWidgetPeriodicUpdater"

        fun enqueue(context: Context, force: Boolean = false) {
            val manager = androidx.work.WorkManager.getInstance(context)
            val request = androidx.work.OneTimeWorkRequestBuilder<ScheduleWidgetUpdater>()
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
        val result = try {
            val info = entryPoint.campusRepository.sync()
            entryPoint.courseRepository.sync(info.first, info.second)

            Log.i("ScheduleWidget", "Synced.")
            Result.success()
        } catch (e: ApiResponseException) {
            // Not logged in, stop the worker

            when (e.code) {
                WeJhApiExceptions.NOT_LOGGED_IN -> {
                    Log.e("ScheduleWidget", "Not logged in.")

                }

                else -> {
                    Log.e("ScheduleWidget", "Failed to sync.", e)
                }
            }

            Result.failure()
        } catch (e: Exception) {
            Log.e("ScheduleWidget", "Failed to sync", e)

            Result.failure()
        }

        // Notify widgets to update
        ScheduleWidget().updateAll(context)

        return result
    }

}