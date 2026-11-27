package com.example.androidplayground.livedata

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.koin.androidx.compose.koinViewModel

class CounterViewModel : ViewModel() {
    val count = MyLiveData<Int>()

    fun increment() = count.setValue((count.value ?: 0) + 1)
}

@Composable
fun CounterScreen(modifier: Modifier = Modifier, viewModel: CounterViewModel = koinViewModel()) {
    val owner = LocalLifecycleOwner.current
    var count by remember { mutableStateOf(0) }

    // Screen aaye to observe, jaye to hatao
    DisposableEffect(viewModel, owner) {
        val observer: (Int) -> Unit = { count = it }
        viewModel.count.observe(owner, observer)
        onDispose { viewModel.count.removeObserver(observer) }
    }

    Column(modifier) {
        Text("Count: $count")
        Button(onClick = { viewModel.increment() }) { Text("Increment") }
    }
}
