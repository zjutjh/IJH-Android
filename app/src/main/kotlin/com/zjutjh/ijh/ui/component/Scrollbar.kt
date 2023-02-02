package com.zjutjh.ijh.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.verticalScrollbar(
    state: ScrollState,
    width: Dp = 4.dp,
): Modifier = composed {
    val targetAlpha = if (state.isScrollInProgress) 0.2f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    val color = MaterialTheme.colorScheme.outline

    drawWithContent {
        drawContent()

        // When maxValue is equal to 0, it means no scrolling.
        // Draw scrollbar if scrolling or if the animation is still running.
        val needDrawScrollbar = state.maxValue != 0 && (state.isScrollInProgress || alpha > 0.0f)

        if (needDrawScrollbar) {
            val scrollbarLength = (state.maxValue.toFloat() / this.size.height) * this.size.height
            val scrollbarOffsetY =
                (state.value.toFloat() / state.maxValue.toFloat()) * (this.size.height - scrollbarLength)

            drawRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarLength),
                alpha = alpha
            )
        }
    }
}

fun Modifier.horizontalScrollbar(
    state: ScrollState,
    startPadding: Dp = 0.dp,
    barWidth: Dp = 4.dp,
): Modifier = composed {
    val targetAlpha = if (state.isScrollInProgress) 0.2f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    val color = MaterialTheme.colorScheme.outline

    drawWithContent {
        drawContent()

        // When maxValue is equal to 0, it means no scrolling.
        // Draw scrollbar if scrolling or if the animation is still running.
        val needDrawScrollbar = state.maxValue != 0 && (state.isScrollInProgress || alpha > 0.0f)

        if (needDrawScrollbar) {
            val padding = startPadding.toPx()
            val width = this.size.width - padding
            val scrollbarLength = (state.maxValue.toFloat() / width) * width
            val scrollbarOffsetX =
                (state.value.toFloat() / state.maxValue.toFloat()) * (width - scrollbarLength)

            drawRect(
                color = color,
                topLeft = Offset(
                    padding + scrollbarOffsetX,
                    this.size.height - barWidth.toPx()
                ),
                size = Size(scrollbarLength, barWidth.toPx()),
                alpha = alpha
            )
        }
    }
}

fun Modifier.verticalPinedScrollbar(
    state: ScrollState,
    width: Dp = 4.dp,
): Modifier = composed {
    val color = MaterialTheme.colorScheme.outline

    drawWithContent {
        drawContent()

        this.drawContext

        // When maxValue is equal to 0, it means no scrolling.
        if (state.maxValue != 0) {
            val scrollbarHeight = (state.maxValue.toFloat() / this.size.height) * this.size.height
            val scrollbarOffsetY =
                (state.value.toFloat() / state.maxValue.toFloat()) * (this.size.height - scrollbarHeight)

            drawRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = 0.2f,
            )
        }
    }
}

