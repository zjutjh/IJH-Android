package com.zjutjh.ijh.util

import android.content.Context
import com.zjutjh.ijh.R
import java.time.Duration
import kotlin.time.toKotlinDuration

fun Duration.toLocalizedString(context: Context): String = buildString {
    val duration = this@toLocalizedString.toKotlinDuration()
    duration.absoluteValue.toComponents { days, hours, minutes, seconds, _ ->
        if (duration.isInfinite() || days > 999L) {
            append(context.getString(R.string.never))
        } else if (days != 0L) {
            append(days).append(context.getString(R.string.day_ago))
        } else if (hours != 0) {
            append(hours).append(context.getString(R.string.hour_ago))
        } else if (minutes != 0) {
            append(minutes).append(context.getString(R.string.minute_ago))
        } else if (seconds != 0) {
            append(seconds).append(context.getString(R.string.second_ago))
        } else {
            append(context.getString(R.string.just))
        }
    }
}