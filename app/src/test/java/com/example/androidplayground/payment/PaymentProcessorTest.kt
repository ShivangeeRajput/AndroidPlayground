package com.example.androidplayground.payment

import com.example.androidplayground.payment.data.EventResult.APPLIED
import com.example.androidplayground.payment.data.EventResult.DUPLICATE_EVENT
import com.example.androidplayground.payment.data.EventResult.INVALID_TRANSITION
import com.example.androidplayground.payment.data.PaymentProcessor
import com.example.androidplayground.payment.data.PaymentStatus
import com.example.androidplayground.payment.data.PaymentStatus.FAILED
import com.example.androidplayground.payment.data.PaymentStatus.INITIATED
import com.example.androidplayground.payment.data.PaymentStatus.PENDING
import com.example.androidplayground.payment.data.PaymentStatus.SUCCESS
import org.junit.Assert.assertEquals
import org.junit.Test

class PaymentProcessorTest {
    private val processor = PaymentProcessor()

    @Test
    fun `unknown payment starts as INITIATED`() {
        assertEquals(INITIATED, processor.statusOf("p1"))
    }

    @Test
    fun `happy path reaches SUCCESS`() {
        assertEquals(APPLIED, processor.processEvent("p1", "e1", PENDING))
        assertEquals(APPLIED, processor.processEvent("p1", "e2", SUCCESS))
        assertEquals(SUCCESS, processor.statusOf("p1"))
    }

    @Test
    fun `terminal state cannot change`() {
        processor.processEvent("p1", "e1", FAILED)
        assertEquals(INVALID_TRANSITION, processor.processEvent("p1", "e2", PENDING))
        assertEquals(FAILED, processor.statusOf("p1"))
    }

    @Test
    fun `same eventId is ignored`() {
        processor.processEvent("p1", "e1", PENDING)
        assertEquals(DUPLICATE_EVENT, processor.processEvent("p1", "e1", SUCCESS))
        assertEquals(PENDING, processor.statusOf("p1"))
    }

    @Test
    fun `early event can be redelivered`() {
        assertEquals(INVALID_TRANSITION, processor.processEvent("p1", "e2", SUCCESS))
        processor.processEvent("p1", "e1", PENDING)
        assertEquals(APPLIED, processor.processEvent("p1", "e2", SUCCESS))
    }

    @Test
    fun `only the four listed transitions are allowed`() {
        val allowed = setOf(INITIATED to PENDING, INITIATED to FAILED, PENDING to SUCCESS, PENDING to FAILED)
        for (from in PaymentStatus.entries) {
            for (to in PaymentStatus.entries) {
                assertEquals("$from -> $to", (from to to) in allowed, from.canMoveTo(to))
            }
        }
    }
}
