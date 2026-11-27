package com.example.androidplayground.livedata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.koin.androidx.compose.koinViewModel

class CounterViewModel : ViewModel() {
    private val _count = MyMutableLiveData<Int>().apply { setValue(0) }
    val count: MyLiveData<Int> = _count

    fun increment() = _count.setValue((_count.value ?: 0) + 1)
}

/** Bridge to Compose: observe, and remove the observer when leaving composition. */
@Composable
fun <T> MyLiveData<T>.observeAsState(initial: T): State<T> {
    val owner = LocalLifecycleOwner.current
    val state = remember(this) { mutableStateOf(value ?: initial) }
    DisposableEffect(this, owner) {
        val observer = Observer<T> { state.value = it }
        observe(owner, observer)
        onDispose { removeObserver(observer) }
    }
    return state
}

@Composable
fun CounterScreen(viewModel: CounterViewModel = koinViewModel()) {
    val count by viewModel.count.observeAsState(0)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Count: $count")
        Button(onClick = viewModel::increment) { Text("Increment") }
    }
}
