package com.example.androidplayground.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentScreen(viewModel: PaymentViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var paymentId by rememberSaveable { mutableStateOf("p1") }
    var eventId by rememberSaveable { mutableStateOf("e1") }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(paymentId, { paymentId = it }, label = { Text("paymentId") })
        OutlinedTextField(eventId, { eventId = it }, label = { Text("eventId") })

        PaymentStatus.entries.forEach { status ->
            Button(onClick = { viewModel.send(paymentId, eventId, status) }) { Text(status.name) }
        }

        Text(state.message)
        state.payments.forEach { (id, status) -> Text("$id : $status") }
    }
}
