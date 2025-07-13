package com.authentication.auth.data.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserAutoLoginPreferencesRepositoryTest {

    companion object {
        private lateinit var context: Context
        private lateinit var sp: SharedPreferences
        private lateinit var userAutoLoginPreferencesRepository: UserAutoLoginPreferencesRepository
        private const val SP_USER_AUTO_LOGIN_CONFIG = "userAutoLogin"
        private const val USER_NAME_KEY = "user_name"
        private const val REMEMBER_CHECK_USER_LOGIN_KEY = "remember_check"

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            sp = context.getSharedPreferences(SP_USER_AUTO_LOGIN_CONFIG, Context.MODE_PRIVATE)
            userAutoLoginPreferencesRepository = UserAutoLoginPreferencesRepository(context)

        }

    }

    /**
     * Save test
     * Input: userId Value: correct
     * Result: userName must be saved in shared preferences
     */
    @Test
    fun testSave_ValidUserNameParameter_UserNameMustSaveInSharedPreferencesSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            //Act
            userAutoLoginPreferencesRepository.save("arma_258", false)
            //Assert
            Assert.assertNotNull(userAutoLoginPreferencesRepository.getUserName())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Save test
     * Input: false Value: inCorrect
     * Result: Remember check value must be false in shared preferences
     */
    @Test
    fun testSave_ValidUserNameParameter_SetRememberCheckParameterToFalse_RememberCheckValueMustBeFalseInSharedPreferences() {
        try {
            //Arrange
            sp.edit().clear().commit()
            //Act
            userAutoLoginPreferencesRepository.save("arma_258", false)
            //Assert
            Assert.assertTrue(!sp.getBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, false))
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Save test
     * Input: true Value: Correct
     * Result: Remember check value must be true in shared preferences
     */
    @Test
    fun testSave_ValidUserNameParameter_SetRememberCheckParameterToTrue_RememberCheckValueMustBeTrueInSharedPreferences() {
        try {
            //Arrange
            sp.edit().clear().commit()
            //Act
            userAutoLoginPreferencesRepository.save("arma_258", true)
            //Assert
            Assert.assertTrue(sp.getBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, false))
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * getUserName test
     * Input: No Value: No
     * Result: get valid userName from shared preferences
     */
    @Test
    fun testGetUserName_NoParameter_GetUserNameSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()

            sp.edit {
                this.putString(USER_NAME_KEY, "arma_258")
                this.commit()
            }
            //Act&&Assert
            Assert.assertEquals("arma_258", userAutoLoginPreferencesRepository.getUserName())

        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * getRememberCheck test
     * Input: No Value: No
     * Result: get valid remember check value from shared preferences
     *
     */
    @Test
    fun testGetRememberCheck_NoParameter_GetValidRememberCheckValueFromSharedPreferences() {
        try {
            //Arrange
            sp.edit().clear().commit()
            sp.edit {
                this.putBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, true)
                this.commit()
            }
            //Act&&Assert
            Assert.assertEquals(true, userAutoLoginPreferencesRepository.getRememberCheck())

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
                this.putString(USER_NAME_KEY, "arma_258")
                this.putBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, true)
                this.commit()
            }
            //Act
            userAutoLoginPreferencesRepository.clearAll()
            //Assert
            Assert.assertTrue(
                userAutoLoginPreferencesRepository.getUserName()
                    .isNullOrEmpty() && !userAutoLoginPreferencesRepository.getRememberCheck()
            )
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

}