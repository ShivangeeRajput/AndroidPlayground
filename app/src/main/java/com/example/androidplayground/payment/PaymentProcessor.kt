package com.example.androidplayground.payment

enum class PaymentStatus {
    INITIATED, PENDING, SUCCESS, FAILED;

    // Saare rules ek jagah
    fun canMoveTo(next: PaymentStatus) = when (this) {
        INITIATED -> next == PENDING || next == FAILED
        PENDING -> next == SUCCESS || next == FAILED
        SUCCESS, FAILED -> false
    }
}

class PaymentProcessor {
    private val payments = mutableMapOf<String, PaymentStatus>()
    private val usedEventIds = mutableSetOf<String>()

    fun statusOf(paymentId: String) = payments[paymentId] ?: PaymentStatus.INITIATED

    fun allPayments(): Map<String, PaymentStatus> = payments.toMap()

    // true = apply hua, false = ignore hua
    @Synchronized
    fun processEvent(paymentId: String, eventId: String, newStatus: PaymentStatus): Boolean {
        if (eventId in usedEventIds) return false // duplicate (assumption)
        if (!statusOf(paymentId).canMoveTo(newStatus)) return false

        payments[paymentId] = newStatus
        usedEventIds.add(eventId)
        return true
    }
}
