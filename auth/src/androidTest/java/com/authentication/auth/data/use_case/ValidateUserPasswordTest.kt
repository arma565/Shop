package com.authentication.auth.data.use_case

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.R
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class ValidateUserPasswordTest{
    private lateinit var context: Context
    private lateinit var validateUserPassword: ValidateUserPassword

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        validateUserPassword = ValidateUserPassword(context)
    }

    /**
     * Validate user password test
     * input: password value: empty
     * Result: return false
     */
    @Test
    fun testValidateUserPassword_InValidParameter_ReturnFalse() {
        try {
            assertEquals(false, validateUserPassword.execute("").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user password test
     * input: password value: empty
     * Result: show valid error message
     */
    @Test
    fun testValidateUserPassword_InValidParameter_ShowValidErrorMessage() {
        try {
            assertEquals(
                context.getString(R.string.password_require),
                validateUserPassword.execute("").errorMessage
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user password test
     * input: password value: password with 10 characters
     * Result: return false
     */
    @Test
    fun testValidateUserPassword_InvalidPasswordLength_ReturnFalse() {
        try {
            assertEquals(false, validateUserPassword.execute("asc1").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user password test
     * input: password value: password with 10 characters
     * Result: show valid error message
     */
    @Test
    fun testValidateUserPassword_InvalidPasswordLength_ShowValidErrorMessage() {
        try {
            assertEquals(
                context.getString(R.string.password_length),
                validateUserPassword.execute("aaa123").errorMessage
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user password test
     * input: password value: password without number
     * Result: return false
     */
    @Test
    fun testValidateUserPassword_InvalidPasswordNumberFormat_ReturnFalse() {
        try {
            assertEquals(false, validateUserPassword.execute("dsfdsfsd").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user password test
     * input: password value: password without number
     * Result: show valid error message
     */
    @Test
    fun testValidateUserPassword_InvalidPasswordFormat_ShowValidErrorMessage() {
        try {
            assertEquals(
                context.getString(R.string.password_digit_letter),
                validateUserPassword.execute("sdfdsfds").errorMessage
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user password test
     * input: password value: password without letter
     * Result: return false
     */
    @Test
    fun testValidateUserPassword_InvalidPasswordLetterFormat_ReturnFalse() {
        try {
            assertEquals(false, validateUserPassword.execute("1323432").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user password test
     * input: password value: password without letter
     * Result: show valid error message
     */
    @Test
    fun testValidateUserPassword_InvalidPasswordLetterFormat_ShowValidErrorMessage() {
        try {
            assertEquals(
                context.getString(R.string.password_digit_letter),
                validateUserPassword.execute("324324234324").errorMessage
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user password test
     * input: password value: valid password
     * Result: return true
     */
    @Test
    fun testValidateUserPassword_ValidParameter_ReturnTrue() {
        try {
            assertEquals(true, validateUserPassword.execute("abc1323432").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }
}