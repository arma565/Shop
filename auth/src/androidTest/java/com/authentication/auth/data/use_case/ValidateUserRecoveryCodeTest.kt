package com.authentication.auth.data.use_case

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.R
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class ValidateUserRecoveryCodeTest{
    private lateinit var context: Context
    private lateinit var validateUserRecoveryCode: ValidateUserRecoveryCode

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        validateUserRecoveryCode = ValidateUserRecoveryCode(context)
    }

    /**
     * Validate user recovery code test
     * input: recovery code value: empty
     * Result: return false
     */
    @Test
    fun testValidateUserRecoveryCode_InValidParameter_ReturnFalse() {
        try {
            assertEquals(false, validateUserRecoveryCode.execute("").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user recovery code test
     * input: recovery code value: empty
     * Result: show valid error message
     */
    @Test
    fun testValidateUserRecoveryCode_InValidParameter_ShowValidErrorMessage() {
        try {
            assertEquals(
                context.getString(R.string.recovery_code_is_require),
                validateUserRecoveryCode.execute("").errorMessage
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user recovery code test
     * input: recovery code value: valid
     * Result: return false
     */
    @Test
    fun testValidateUserRecoveryCode_validParameter_ReturnTrue() {
        try {
            assertEquals(true, validateUserRecoveryCode.execute("123213123").successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

}