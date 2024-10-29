package com.authentication.auth.data.use_case

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.R
import org.junit.Assert
import org.junit.Assert.fail

import org.junit.Before
import org.junit.Test

class ValidateTermsTest {

    private lateinit var context: Context
    private lateinit var validateTerms : ValidateTerms

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        validateTerms = ValidateTerms(context)
    }

    /**
     * Validate terms test
     * input: acceptedTerms value: inValid
     * Result: return false
     */
    @Test
    fun testValidateTerms_InValidParameter_ReturnFalse() {
        try {
            Assert.assertEquals(false,validateTerms.execute(false).successful)
        }catch (e : Exception){
            fail(e.message)
        }
    }

    /**
     * Validate terms test
     * input: acceptedTerms value: inValid
     * Result: show valid error message
     */
    @Test
    fun testValidateTerms_InValidParameter_ShowValidErrorMessage() {
        try {
            Assert.assertEquals(context.getString(R.string.terms_require),validateTerms.execute(false).errorMessage)
        }catch (e : Exception){
            fail(e.message)
        }
    }

    /**
     * Validate terms test
     * input: acceptedTerms value: valid
     * Result: return true
     */
    @Test
    fun testValidateTerms_ValidParameter_ReturnTrue() {
        try {
            Assert.assertEquals(true,validateTerms.execute(true).successful)
        }catch (e : Exception){
            fail(e.message)
        }
    }
}