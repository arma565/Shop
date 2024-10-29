package com.authentication.auth.data.use_case

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.R
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class ValidateUserRepeatedPasswordTest{
    private lateinit var context: Context
    private lateinit var validateUserRepeatedPassword: ValidateUserRepeatedPassword

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        validateUserRepeatedPassword = ValidateUserRepeatedPassword(context)
    }

    /**
     * Validate user repeated password test
     * input: repeated password value: invalid passwords
     * Result: return false
     */
    @Test
    fun testValidateUserPassword_InvalidParameter_ReturnFalse() {
        try {
            assertEquals(false, validateUserRepeatedPassword.execute("1234" ,"4321" ).successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user repeated password test
     * input: repeated password value: invalid passwords
     * Result: show valid error message
     */
    @Test
    fun testValidateUserPassword_InvalidParameter_ShowValidErrorMessage() {
        try {
            assertEquals(context.getString(R.string.passwords_not_match), validateUserRepeatedPassword.execute("1234" ,"4321" ).errorMessage)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user repeated password test
     * input: repeated password value: valid passwords
     * Result: return true
     */
    @Test
    fun testValidateUserPassword_validParameter_ReturnTrue() {
        try {
            assertEquals(true, validateUserRepeatedPassword.execute("1234" ,"1234" ).successful)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

}