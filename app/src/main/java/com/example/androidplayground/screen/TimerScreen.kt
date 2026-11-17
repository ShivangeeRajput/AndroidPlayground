package com.example.androidplayground.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidplayground.viewmodel.TimerViewModel

@Composable
fun TimerScreen(modifier: Modifier = Modifier, viewModel: TimerViewModel = viewModel()) {
    val totalSeconds = viewModel.timeLeftMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "%02d:%02d".format(minutes, seconds),
            style = MaterialTheme.typography.displayLarge
        )
        if (viewModel.isFinished) {
            Text("Time's up!", style = MaterialTheme.typography.titleLarge)
        }

        Row(
            modifier = Modifier.padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (viewModel.isRunning) {
                Button(onClick = { viewModel.pause() }) { Text("Pause") }
            } else {
                Button(onClick = { viewModel.start() }) { Text("Start") }
            }
            OutlinedButton(onClick = { viewModel.reset() }) { Text("Reset") }
        }
    }
}
