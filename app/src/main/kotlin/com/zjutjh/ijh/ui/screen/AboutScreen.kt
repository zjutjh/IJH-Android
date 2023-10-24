package com.zjutjh.ijh.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.zjutjh.ijh.BuildConfig
import com.zjutjh.ijh.R
import com.zjutjh.ijh.ui.component.BackIconButton
import com.zjutjh.ijh.ui.component.IJhScaffold

@Composable
fun AboutRoute(
    onNavigateBack: () -> Unit,
) {
    AboutScreen(
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun AboutScreen(
    onNavigateBack: () -> Unit = {},
) {
    AboutScaffold(onBackClick = onNavigateBack) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val uriHandler = LocalUriHandler.current
            val projectPageUrl = stringResource(id = R.string.project_url)

            Text(
                modifier = Modifier.clickable { uriHandler.openUri(projectPageUrl) },
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${BuildConfig.BUILD_TYPE}|v${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )

            val text = buildAnnotatedString {
                val linkStyle = SpanStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
                val str = stringResource(id = R.string.about_text)
                val dev = stringResource(id = R.string.app_developer)
                val serv = stringResource(id = R.string.service_provider)

                val l = str.split("[dev]")
                val p1 = l[0]
                val r = l[1].split("[serv]")
                val p2 = r[0]
                val p3 = r[1]

                pushStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface))
                append(p1)
                pushStyle(style = linkStyle)
                pushUrlAnnotation(
                    UrlAnnotation(stringResource(id = R.string.developer_url))
                )
                append(dev)
                pop()
                pop()
                append(p2)
                pushStyle(style = linkStyle)
                pushUrlAnnotation(
                    UrlAnnotation(stringResource(id = R.string.service_url))
                )
                append(serv)
                pop()
                pop()
                append(p3)
            }
            ClickableText(
                modifier = Modifier.padding(top = 20.dp),
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                onClick = { offset ->
                    text.getUrlAnnotations(offset, offset)
                        .firstOrNull()?.let { annotation ->
                            uriHandler.openUri(annotation.item.url)
                        }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScaffold(
    onBackClick: () -> Unit,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    IJhScaffold(
        topBar = {
            AboutTopBar(it, onBackClick)
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.about))
        },
        navigationIcon = {
            BackIconButton(onBackClick)
        },
        scrollBehavior = scrollBehavior,
    )
}