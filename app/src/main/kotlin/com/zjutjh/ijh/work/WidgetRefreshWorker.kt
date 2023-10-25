package com.zjutjh.ijh.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WidgetRefreshWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result =
        try {
            // TODO
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }

}