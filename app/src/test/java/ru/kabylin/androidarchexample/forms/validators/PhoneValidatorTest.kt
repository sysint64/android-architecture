package ru.kabylin.androidarchexample.forms.validators

import org.junit.Test
import org.junit.Assert.*

class PhoneValidatorTest {
    @Test
    @Throws(Exception::class)
    fun `issue 49 only numbers and + symbol should allows`() {
        assertEquals(PhoneValidator().isValid("+79137698344"), true)
        assertEquals(PhoneValidator().isValid("+79137698344/()"), false)
    }
}
