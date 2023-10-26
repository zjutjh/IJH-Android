package com.zjutjh.ijh.work

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.updateAll
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.time.Duration

/**
 * This worker is used to refresh the widget periodically.
 */
class WidgetRefreshWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val INPUT_CLASS_NAME = "className"

        inline fun <reified T : GlanceAppWidget> getUniqueWorkName() =
            WidgetRefreshWorker::class.java.simpleName + "_" + T::class.java.simpleName

        /**
         * Enqueue a [WidgetRefreshWorker] for the given [GlanceAppWidget].
         * Do nothing if the worker is already enqueued.
         * Default refresh interval is 15 minutes.
         */
        inline fun <reified T : GlanceAppWidget> enqueue(context: Context) {
            val manager = WorkManager.getInstance(context)
            val request = PeriodicWorkRequestBuilder<WidgetRefreshWorker>(
                Duration.ofMinutes(15), Duration.ofMinutes(5)
            ).setConstraints(
                Constraints(requiresBatteryNotLow = true)
            ).setInputData(
                workDataOf(INPUT_CLASS_NAME to T::class.java.name)
            )
                .build()

            manager.enqueueUniquePeriodicWork(
                getUniqueWorkName<T>(),
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        inline fun <reified T : GlanceAppWidget> cancel(context: Context) {
            val manager = WorkManager.getInstance(context)
            manager.cancelUniqueWork(getUniqueWorkName<T>())
        }
    }

    /**
     * Using reflection to get the class name of the widget, then update it.
     */
    override suspend fun doWork(): Result {
        return try {
            val className = inputData.getString(INPUT_CLASS_NAME) ?: return Result.failure()
            val clazz = Class.forName(className)

            // check if clazz is a subclass of GlanceAppWidget
            if (!GlanceAppWidget::class.java.isAssignableFrom(clazz)) return Result.failure()

            val obj = clazz.getConstructor().newInstance() as GlanceAppWidget

            obj.updateAll(context)

            Log.i("WidgetRefreshWorker", "Widget refreshed: $className")
            Result.success()
        } catch (e: Exception) {
            Log.e("WidgetRefreshWorker", "Error when refreshing widget.", e)
            Result.failure()
        }
    }

}