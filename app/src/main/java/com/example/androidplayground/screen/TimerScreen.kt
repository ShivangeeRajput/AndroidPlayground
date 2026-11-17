package com.example.androidplayground.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.androidplayground.notification.NotificationHelper
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
    val context = LocalContext.current
    val totalSeconds = viewModel.timeLeftMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    // Create the channel once.
    LaunchedEffect(Unit) { NotificationHelper.createChannel(context) }

    // Ask for notification permission (Android 13+), then start the timer.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { viewModel.start() }   // start either way; notification just won't show if denied

    // Fire the notification when the timer finishes.
    LaunchedEffect(viewModel.isFinished) {
        if (viewModel.isFinished) NotificationHelper.showTimerFinished(context)
    }

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
                Button(onClick = {
                    if (NotificationHelper.hasPermission(context)) {
                        viewModel.start()
                    } else {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }) { Text("Start") }
            }
            OutlinedButton(onClick = { viewModel.reset() }) { Text("Reset") }
        }
    }
}
