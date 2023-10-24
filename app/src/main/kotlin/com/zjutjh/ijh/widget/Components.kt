/**
 * Compose components for Glance
 */
package com.zjutjh.ijh.widget

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

/**
 * Simple text component wrapper for Glance
 */
@Composable
fun GText(
    text: String,
    color: ColorProvider = GlanceTheme.colors.onSurface,
    modifier: GlanceModifier = GlanceModifier
) = Text(
    text = text,
    modifier = modifier,
    style = TextStyle(color),
    maxLines = 1,
)
