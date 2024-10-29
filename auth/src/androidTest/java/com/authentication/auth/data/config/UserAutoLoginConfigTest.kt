package com.authentication.auth.data.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.data.model.User
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserAutoLoginConfigTest {

    companion object {
        private lateinit var context: Context
        private lateinit var sp: SharedPreferences
        private lateinit var userAutoLoginConfig: UserAutoLoginConfig
        private const val SP_USER_AUTO_LOGIN_CONFIG = "userAutoLogin"
        private const val USER_ID_KEY = "user_id"
        private const val REMEMBER_CHECK_USER_LOGIN_KEY = "remember_check"

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            sp = context.getSharedPreferences(SP_USER_AUTO_LOGIN_CONFIG, Context.MODE_PRIVATE)
            userAutoLoginConfig = UserAutoLoginConfig(context)
        }

    }

    /**
     * Save test
     * Input: userId Value: correct
     * Result: userID must be saved in shared preferences
     */
    @Test
    fun testSave_ValidUserIDParameter_UserMustSaveInSharedPreferencesSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            val user = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            //Act
            userAutoLoginConfig.save(user.id, false)
            //Assert
            Assert.assertNotEquals(0, sp.getInt(USER_ID_KEY, 0))
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
    fun testSave_ValidUserIDParameter_InValidRememberCheckParameter_RememberCheckValueMustBeFalseInSharedPreferences() {
        try {
            //Arrange
            sp.edit().clear().commit()
            val user = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            //Act
            userAutoLoginConfig.save(user.id, false)
            //Assert
            Assert.assertNotEquals(true, sp.getBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, false))
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
    fun testSave_ValidUserIDParameter_ValidRememberCheckParameter_RememberCheckValueMustBeTrueInSharedPreferences() {
        try {
            //Arrange
            sp.edit().clear().commit()
            val user = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            //Act
            userAutoLoginConfig.save(user.id, true)
            //Assert
            Assert.assertNotEquals(false, sp.getBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, false))
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * getUserID test
     * Input: No Value: No
     * Result: get valid userID from shared preferences
     */
    @Test
    fun testGetUser_NoParameter_GetValidUserID() {
        try {
            //Arrange
            sp.edit().clear().commit()
            val user = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            sp.edit {
                this.putInt(USER_ID_KEY, user.id)
                this.commit()
            }
            //Act&&Assert
            Assert.assertEquals(100, userAutoLoginConfig.getUserID())

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
            Assert.assertEquals(true, userAutoLoginConfig.getRememberCheck())

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
            val user = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            sp.edit {
                this.putInt(USER_ID_KEY, user.id)
                this.putBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, true)
                this.commit()
            }
            //Act
            userAutoLoginConfig.clearAll()
            //Assert
            Assert.assertTrue(userAutoLoginConfig.getUserID() == 0 && !userAutoLoginConfig.getRememberCheck())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

}