package com.example.androidplayground.timer.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidplayground.timer.TimerEngine
import com.example.androidplayground.timer.TimerService
import com.example.androidplayground.timer.TimerState
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SimpleTimerScreen(viewModel: TimerViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TimerContent("Without service", state, viewModel::start, viewModel::pause, viewModel::reset)
}

@Composable
fun ServiceTimerScreen(engine: TimerEngine = koinInject()) {
    val context = LocalContext.current
    val state by engine.state.collectAsStateWithLifecycle()
    // The timer works even if permission is denied; only the notification is hidden.
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        TimerService.start(context)
    }

    TimerContent(
        title = "With foreground service",
        state = state,
        onStart = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                TimerService.start(context)
            }
        },
        onPause = { engine.pause(); TimerService.stop(context) },
        onReset = { engine.reset(); TimerService.stop(context) },
    )
}

@Composable
private fun TimerContent(
    title: String,
    state: TimerState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(title)
        Text(state.text, style = MaterialTheme.typography.displayLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (state.isRunning) {
                Button(onClick = onPause) { Text("Pause") }
            } else {
                Button(onClick = onStart) { Text("Start") }
            }
            OutlinedButton(onClick = onReset) { Text("Reset") }
        }
    }
}
