/**
 * Compose components for Glance
 */
package com.zjutjh.ijh.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

/**
 * Simple text component wrapper for Glance
 */
@Composable
fun GText(
    text: String,
    color: ColorProvider = GlanceTheme.colors.onSurface,
    size: TextUnit = MaterialTheme.typography.bodySmall.fontSize,
    weight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign? = null,
    modifier: GlanceModifier = GlanceModifier
) = Text(
    text = text,
    modifier = modifier,
    style = TextStyle(color = color, fontSize = size, fontWeight = weight, textAlign = textAlign),
    maxLines = 1,
)

@Composable
fun IconButton(
    provider: ImageProvider,
    onClick: Action
) {
    Box(
        modifier = GlanceModifier
            .background(GlanceTheme.colors.primary)
            .height(30.dp)
            .width(30.dp)
            .cornerRadius(10.dp)
            .clickable(onClick)
    ) {
        Image(
            provider = provider,
            contentDescription = "Image in button",
            modifier = GlanceModifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(GlanceTheme.colors.onPrimary)
        )
    }
}