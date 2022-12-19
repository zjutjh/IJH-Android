package com.zjutjh.ijh.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IJHApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("IJH")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Share, contentDescription = "share")
                    }
                },
            )
        },
    ) { contentPadding ->
        Surface(
            Modifier.padding(contentPadding),
        ) {
            Cards()
        }
    }
}

@Composable
fun Cards(modifier: Modifier = Modifier, names: List<String> = List(100) { "$it" }) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            ElevatedCard(modifier = Modifier.padding(4.dp)) {
                Column(Modifier.padding(4.dp)) {
                    Text("Hello,")
                    Text(name)
                }
            }
        }
    }
}

