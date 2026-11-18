package com.example.androidplayground.screen

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidplayground.notification.NotificationHelper
import com.example.androidplayground.timer.TimerRepository
import com.example.androidplayground.timer.TimerService

@Composable
fun TimerScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // The service owns the timer; the screen just observes it.
    val state by TimerRepository.state.collectAsStateWithLifecycle()

    val totalSeconds = state.timeLeftMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    LaunchedEffect(Unit) { NotificationHelper.createChannels(context) }

    // Android 13+: ask for notification permission, then start either way.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { TimerService.start(context) }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "%02d:%02d".format(minutes, seconds),
            style = MaterialTheme.typography.displayLarge
        )
        if (state.isFinished) {
            Text("Time's up!", style = MaterialTheme.typography.titleLarge)
        }

        Row(
            modifier = Modifier.padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isRunning) {
                Button(onClick = { TimerService.pause(context) }) { Text("Pause") }
            } else {
                Button(onClick = {
                    if (NotificationHelper.hasPermission(context)) {
                        TimerService.start(context)
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }) { Text("Start") }
            }
            OutlinedButton(onClick = { TimerService.reset(context) }) { Text("Reset") }
        }
    }
}
