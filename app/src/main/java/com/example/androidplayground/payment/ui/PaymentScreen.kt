package com.example.androidplayground.payment.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
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
import com.example.androidplayground.payment.data.PaymentStatus
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PaymentScreen(viewModel: PaymentViewModel = koinViewModel()) {
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    var paymentId by rememberSaveable { mutableStateOf("p1") }
    var eventId by rememberSaveable { mutableStateOf("e1") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(paymentId, { paymentId = it }, Modifier.fillMaxWidth(), label = { Text("paymentId") })
        OutlinedTextField(eventId, { eventId = it }, Modifier.fillMaxWidth(), label = { Text("eventId") })
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PaymentStatus.entries.forEach { status ->
                Button(onClick = { viewModel.send(paymentId, eventId, status) }) { Text(status.name) }
            }
        }
        Text(message)
        LazyColumn {
            items(payments.entries.toList(), key = { it.key }) { (id, status) ->
                ListItem(headlineContent = { Text(id) }, trailingContent = { Text(status.name) })
            }
        }
    }
}
