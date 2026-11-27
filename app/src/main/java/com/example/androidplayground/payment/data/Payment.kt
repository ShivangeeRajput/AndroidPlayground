package com.example.androidplayground.payment.data

enum class PaymentStatus {
    INITIATED, PENDING, SUCCESS, FAILED;

    /** The whole state machine lives here. Terminal states allow nothing. */
    fun canMoveTo(next: PaymentStatus): Boolean = when (this) {
        INITIATED -> next == PENDING || next == FAILED
        PENDING -> next == SUCCESS || next == FAILED
        SUCCESS, FAILED -> false
    }
}

enum class EventResult { APPLIED, DUPLICATE_EVENT, INVALID_TRANSITION }
