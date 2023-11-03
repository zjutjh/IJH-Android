package com.zjutjh.ijh.ui.component

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
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
import com.zjutjh.ijh.model.ElectricityBalance
import com.zjutjh.ijh.ui.theme.IJhTheme
import com.zjutjh.ijh.util.LoadResult
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ElectricityStatusCard(
    modifier: Modifier = Modifier,
    balance: LoadResult<ElectricityBalance?>,
) {
    val context = LocalContext.current
    val subtitle = remember(balance) {
        prompt(context, if (balance is LoadResult.Loading) null else LocalDateTime.now())
    }

    GlanceCard(
        modifier = modifier,
        title = stringResource(id = R.string.dorm_electricity),
        subtitle = subtitle,
        icon = Icons.Default.ElectricBolt
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
                        "${it.data.total} KW•h"
                    }

                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        text = text,
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

private fun prompt(context: Context, syncTime: LocalDateTime? = null): String = buildString {
    val divider = " • "
    append(context.getString(R.string.balance))
    append(divider)
    if (syncTime == null) {
        append(context.getString(R.string.unknown))
    } else {
        append(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(syncTime))
    }
}


@Preview
@Composable
private fun ElectricityStatusCardPreview() {
    IJhTheme {
        ElectricityStatusCard(
            balance = LoadResult.Ready(
                ElectricityBalance(
                    total = 200f,
                    totalAmount = 100f,
                    subsidy = 100f,
                    subsidyAmount = 50f,
                    surplus = 100f,
                    surplusAmount = 50f
                )
            )
        )
    }
}