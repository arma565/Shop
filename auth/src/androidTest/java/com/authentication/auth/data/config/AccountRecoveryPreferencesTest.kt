package com.authentication.auth.data.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class AccountRecoveryPreferencesTest {

    companion object {
        private lateinit var context: Context
        private lateinit var sp: SharedPreferences
        private lateinit var accountRecoveryPreferences: AccountRecoveryPreferences
        private const val RECOVERY_SP = "RecoverySP"
        private const val EMAIL_KEY = "email"
        private const val TOKEN_KEY = "token"
        private const val EMAIL_TEST = "arma@gmail.com"
        private const val TOKEN_TEST = "fdgdg##$241@13123$#%$%#56346%$#^%$^%$^$#$#$%#$#"

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            sp = context.getSharedPreferences(RECOVERY_SP, Context.MODE_PRIVATE)
            accountRecoveryPreferences = AccountRecoveryPreferences(context)
        }

    }

    /**
     * Set test
     * Input: Email Value: correct & token Value: correct
     * Result: Email must be saved in shared preferences
     */
    @Test
    fun testSet_ValidEmailParameter_ValidTokenParameter_EmailMustSaveInSharedPreferencesSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            //Act
            accountRecoveryPreferences.set(EMAIL_TEST, TOKEN_TEST)
            //Assert
            Assert.assertEquals(sp.getString(EMAIL_KEY, ""), EMAIL_TEST)
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Set test
     * Input: Email Value: correct & token Value: correct
     * Result: Token must be saved in shared preferences
     */
    @Test
    fun testSet_ValidEmailParameter_ValidTokenParameter_TokenMustSaveInSharedPreferencesSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            //Act
            accountRecoveryPreferences.set(EMAIL_TEST, TOKEN_TEST)
            //Assert
            Assert.assertEquals(sp.getString(TOKEN_KEY, ""), TOKEN_TEST)
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Set test
     * Input: Email Value: Empty & token Value: correct
     * Result: Email must be empty
     */
    @Test
    fun testSet_InValidEmailParameter_ValidTokenParameter_EmailFromSharedPreferencesMustBeEmpty() {
        try {
            //Arrange
            sp.edit().clear().commit()
            //Act
            accountRecoveryPreferences.set("", TOKEN_TEST)
            //Assert
            Assert.assertEquals("",sp.getString(EMAIL_KEY, ""))
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Set test
     * Input: Email Value: Correct & token Value: Empty
     * Result: Token must be empty
     */
    @Test
    fun testSet_ValidEmailParameter_InValidTokenParameter_TokenFromSharedPreferencesMustBeEmpty() {
        try {
            //Arrange
            sp.edit().clear().commit()
            //Act
            accountRecoveryPreferences.set(EMAIL_TEST, "")
            //Assert
            Assert.assertEquals("",sp.getString(TOKEN_KEY, ""))
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }


    /**
     * Get Email test
     * Input: NoParameter
     * Result: Get expected email from shared preferences
     */
    @Test
    fun testGetEmail_NoParameter_GetEmailFromSharedPreferencesSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            sp.edit {
                this.putString(EMAIL_KEY, EMAIL_TEST)
                this.commit()
            }

            //Act&&Assert
            Assert.assertEquals(EMAIL_TEST, accountRecoveryPreferences.getEmail())

        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Get Token test
     * Input: NoParameter
     * Result: Get expected token from shared preferences
     */
    @Test
    fun testGetToken_NoParameter_GetTokenFromSharedPreferencesSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            sp.edit {
                this.putString(TOKEN_KEY, TOKEN_TEST)
                this.commit()
            }

            //Act&&Assert
            Assert.assertEquals(TOKEN_TEST, accountRecoveryPreferences.getToken())

        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }


    /**
     * ClearAll test
     * Input: No Value: No
     * Result: clear everything from shared preferences
     */
    @Test
    fun testClearAll_NoParameter_ClearAllSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            sp.edit {
                this.putString(EMAIL_KEY, EMAIL_TEST)
                this.putString(TOKEN_KEY, TOKEN_TEST)
                this.commit()
            }
            //Act
            accountRecoveryPreferences.clearAll()
            //Assert
            Assert.assertTrue(
                accountRecoveryPreferences.getEmail()
                    .isNullOrEmpty() && accountRecoveryPreferences.getToken().isNullOrEmpty()
            )

        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }
}