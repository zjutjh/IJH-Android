package com.zjutjh.ijh.ui.component

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.R
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.util.LoadResult
import com.zjutjh.ijh.util.toLocalizedString
import java.time.Duration

@Composable
fun CampusCardInfoCard(
    modifier: Modifier = Modifier,
    balance: LoadResult<String?>,
    lastSync: LoadResult<Duration?>,
) {
    val context = LocalContext.current
    val subtitle = remember(lastSync) {
        prompt(context, lastSync)
    }

    GlanceCard(
        modifier = modifier,
        title = stringResource(id = R.string.campus_card),
        subtitle = subtitle,
        icon = Icons.Default.CreditCard
    ) {
        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            targetState = balance,
            contentAlignment = Alignment.Center,
            label = "Loading",
        ) {
            when (it) {
                is LoadResult.Loading -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(id = R.string.loading),
                        textAlign = TextAlign.Center
                    )
                }

                is LoadResult.Ready -> {
                    val text = if (it.data == null) {
                        "N/A"
                    } else {
                        "¥ ${it.data}"
                    }

                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        text = text,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

private fun prompt(context: Context, lastSyncDuration: LoadResult<Duration?>) =
    buildString {
        val separator = " • "
        append(context.getString(R.string.balance))
        append(separator)
        when (lastSyncDuration) {
            is LoadResult.Loading -> append(context.getString(R.string.unknown))
            is LoadResult.Ready -> {
                val duration = lastSyncDuration.data
                if (duration != null) {
                    append(duration.toLocalizedString(context))
                } else {
                    append(context.getString(R.string.never))
                }
            }
        }
    }


@Preview
@Composable
private fun CampusCardInfoCardPreview() {
    IJhTheme {
        CampusCardInfoCard(
            balance = LoadResult.Ready("123"),
            lastSync = LoadResult.Ready(Duration.ofSeconds(10))
        )
    }
}

@Preview
@Composable
private fun CampusCardInfoCardPreviewEmpty() {
    IJhTheme {
        CampusCardInfoCard(balance = LoadResult.Loading, lastSync = LoadResult.Loading)
    }
}