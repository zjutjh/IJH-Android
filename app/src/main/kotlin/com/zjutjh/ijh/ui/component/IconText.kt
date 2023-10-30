package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun IconText(
    icon: ImageVector,
    text: String,
    contentDescription: String? = null,
    fontWeight: FontWeight? = null,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val id = "icon"
    val annotatedString = buildAnnotatedString {
        appendInlineContent(id, "[icon]")
        append(text)
    }
    val inlineContent = mapOf(
        id to InlineTextContent(
            Placeholder(
                width = style.fontSize,
                height = style.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            )
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = icon,
                contentDescription = contentDescription,
            )
        }
    )

    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        style = style,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Preview
@Composable
fun IconTextPreview() {
    Surface {
        IconText(
            icon = Icons.Default.Image,
            text = "Hello World",
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 30.sp)
        )
    }
}