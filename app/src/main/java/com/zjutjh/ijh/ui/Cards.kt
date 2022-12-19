package com.zjutjh.ijh.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.ui.theme.IJHTheme

@Composable
fun ScheduleCard(courses: List<String>, modifier: Modifier = Modifier) {
    ElevatedCard(modifier.padding(7.dp)) {
        Text(
            text = "Schedule",
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineMedium,
        )

        val lineColor = MaterialTheme.colorScheme.primary

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {

            drawLine(
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = size.width, y = 0f),
                color = lineColor,
                strokeWidth = 3f,
            )
        }
        Column(modifier.padding(10.dp)) {
            courses.forEach { Text(text = it) }
        }
    }
}

@Preview
@Composable
fun ScheduleCardPreview() {
    IJHTheme {
        Surface {
            ScheduleCard(courses = listOf("8:00AM IT", "9:55AM DB"))
        }
    }
}