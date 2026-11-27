package com.example.androidplayground.payment.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Keeps the current status of every payment in memory.
 *
 * Assumptions (confirm with the interviewer):
 * - eventId is globally unique. A repeated eventId is ignored.
 * - Only APPLIED events are remembered, so an event rejected because it came too early
 *   (SUCCESS before PENDING) can be delivered again later.
 *
 * Time and space: O(1) per event, O(payments + applied events) memory.
 */
class PaymentProcessor {
    private val processedEventIds = mutableSetOf<String>()

    private val _statuses = MutableStateFlow<Map<String, PaymentStatus>>(emptyMap())
    val statuses: StateFlow<Map<String, PaymentStatus>> = _statuses.asStateFlow()

    fun statusOf(paymentId: String): PaymentStatus =
        _statuses.value[paymentId] ?: PaymentStatus.INITIATED

    /** synchronized: "check then update" must be atomic when events arrive from many threads. */
    @Synchronized
    fun processEvent(paymentId: String, eventId: String, newStatus: PaymentStatus): EventResult {
        if (eventId in processedEventIds) return EventResult.DUPLICATE_EVENT

        val current = statusOf(paymentId)
        if (!current.canMoveTo(newStatus)) return EventResult.INVALID_TRANSITION

        _statuses.value = _statuses.value + (paymentId to newStatus)
        processedEventIds += eventId
        return EventResult.APPLIED
    }
}
