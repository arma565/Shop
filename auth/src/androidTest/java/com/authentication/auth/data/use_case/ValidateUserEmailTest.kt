package com.authentication.auth.data.use_case

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.R
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class ValidateUserEmailTest {

    private lateinit var context: Context
    private lateinit var validateUserEmail: ValidateUserEmail

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        validateUserEmail = ValidateUserEmail(context)
    }

    /**
     * Validate user email test
     * input: email value: empty
     * Result: return false
     */
    @Test
    fun testValidateUserEmail_InValidParameter_ReturnFalse() {
        try {
            assertEquals(false, validateUserEmail.execute("").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user email test
     * input: email value: empty
     * Result: show valid error message
     */
    @Test
    fun testValidateUserEmail_InValidParameter_ShowValidErrorMessage() {
        try {
            assertEquals(
                context.getString(R.string.email_require),
                validateUserEmail.execute("").errorMessage
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user email test
     * input: email value: inValid email format
     * Result: return false
     */
    @Test
    fun testValidateUserEmail_InValidEmailFormat_ReturnFalse() {
        try {
            assertEquals(false, validateUserEmail.execute("32423423423").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user email test
     * input: email value: inValid email format
     * Result: show valid error message
     */
    @Test
    fun testValidateUserEmail_InValidEmailFormat_ShowValidErrorMessage() {
        try {
            assertEquals(
                context.getString(R.string.email_not_valid),
                validateUserEmail.execute("2343244234234").errorMessage
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user email test
     * input: email value: inValid email format
     * Result: return false
     */
    @Test
    fun testValidateUserEmail_InValidInputLocal_ReturnFalse() {
        try {
            assertEquals(false, validateUserEmail.execute("سیبسیبیسبسیب@gmail.com").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user email test
     * input: email value: inValid email format
     * Result: show valid error message
     */
    @Test
    fun testValidateUserEmail_InValidInputLocal_ShowValidErrorMessage() {
        try {
            assertEquals(
                context.getString(R.string.email_not_valid),
                validateUserEmail.execute("سیبسیبیسبسیب@gmail.com").errorMessage
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }


    /**
     * Validate user email test
     * input: email value: valid
     * Result: return true
     */
    @Test
    fun testValidateUserEmail_validParameter_ReturnTrue() {
        try {
            assertEquals(true, validateUserEmail.execute("reza@gmail.com").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }
}