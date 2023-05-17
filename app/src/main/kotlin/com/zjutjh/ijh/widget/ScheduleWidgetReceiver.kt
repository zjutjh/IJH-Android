package com.zjutjh.ijh.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * Implementation of App Widget functionality.
 */
class ScheduleWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = ScheduleWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        super.onEnabled(context)
        // Enter relevant functionality for when the last widget is disabled
    }
}
