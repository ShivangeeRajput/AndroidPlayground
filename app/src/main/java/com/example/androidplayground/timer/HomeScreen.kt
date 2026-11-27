package com.example.androidplayground.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(onOpen: (String) -> Unit) {
    Column {
        Button(onClick = { onOpen("simple") }) { Text("Timer without service") }
        Button(onClick = { onOpen("service") }) { Text("Timer with foreground service") }
    }
}
