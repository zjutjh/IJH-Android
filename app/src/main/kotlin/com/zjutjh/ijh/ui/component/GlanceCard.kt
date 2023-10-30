package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.R
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.util.emptyFun

@Composable
internal fun GlanceCard(
    modifier: Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector? = null,
    onButtonClick: (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Card(
        modifier = modifier,
    ) {
        Row(
            Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                // Title
                if (icon != null)
                    IconText(
                        icon = icon,
                        text = " | $title",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                else Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )

                // Subtitle
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                )
            }
            if (onButtonClick != null)
                FilledTonalIconButton(
                    onClick = onButtonClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = stringResource(id = R.string.more)
                    )
                }
        }

        content()
    }
}

@Preview
@Composable
private fun GlanceCardPreview() {
    IJhTheme {
        GlanceCard(
            modifier = Modifier,
            title = "Title",
            subtitle = "Subtitle",
            icon = Icons.Default.Image,
            onButtonClick = ::emptyFun,
        ) {
            Text(modifier = Modifier.padding(24.dp), text = "Content")
        }
    }
}