package com.example.androidplayground.payment.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.androidplayground.payment.data.PaymentProcessor
import com.example.androidplayground.payment.data.PaymentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentViewModel(private val processor: PaymentProcessor) : ViewModel() {
    val payments = processor.statuses

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    fun send(paymentId: String, eventId: String, status: PaymentStatus) {
        if (paymentId.isBlank() || eventId.isBlank()) {
            _message.value = "paymentId and eventId are required"
            return
        }
        val result = processor.processEvent(paymentId.trim(), eventId.trim(), status)
        Log.d("Payment", "paymentId=$paymentId eventId=$eventId new=$status result=$result")
        _message.value = "$eventId -> $status: $result"
    }
}
