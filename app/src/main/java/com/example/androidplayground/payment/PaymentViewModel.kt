package com.example.androidplayground.payment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PaymentState(
    val payments: Map<String, PaymentStatus> = emptyMap(),
    val message: String = "",
)

class PaymentViewModel(private val processor: PaymentProcessor) : ViewModel() {
    private val _state = MutableStateFlow(PaymentState(processor.allPayments()))
    val state = _state.asStateFlow()

    fun send(paymentId: String, eventId: String, status: PaymentStatus) {
        val applied = processor.processEvent(paymentId, eventId, status)
        val message = if (applied) "$eventId applied" else "$eventId ignored"
        _state.value = PaymentState(processor.allPayments(), message)
    }
}
