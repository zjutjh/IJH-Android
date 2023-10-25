package com.zjutjh.ijh.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.zjutjh.ijh.work.ScheduleWidgetUpdater

class UpdateAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        ScheduleWidgetUpdater.enqueue(context)
    }
}