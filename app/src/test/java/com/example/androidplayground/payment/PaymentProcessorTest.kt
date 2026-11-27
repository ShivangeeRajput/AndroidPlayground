package com.example.androidplayground.payment

import com.example.androidplayground.payment.PaymentStatus.FAILED
import com.example.androidplayground.payment.PaymentStatus.INITIATED
import com.example.androidplayground.payment.PaymentStatus.PENDING
import com.example.androidplayground.payment.PaymentStatus.SUCCESS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PaymentProcessorTest {
    private val processor = PaymentProcessor()

    @Test
    fun `happy path`() {
        assertEquals(INITIATED, processor.statusOf("p1"))
        assertTrue(processor.processEvent("p1", "e1", PENDING))
        assertTrue(processor.processEvent("p1", "e2", SUCCESS))
        assertEquals(SUCCESS, processor.statusOf("p1"))
    }

    @Test
    fun `terminal state and duplicate event do not change status`() {
        processor.processEvent("p1", "e1", FAILED)
        assertFalse(processor.processEvent("p1", "e2", PENDING))
        assertFalse(processor.processEvent("p2", "e1", PENDING))
        assertEquals(FAILED, processor.statusOf("p1"))
        assertEquals(INITIATED, processor.statusOf("p2"))
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
