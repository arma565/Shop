package com.auth.login.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.auth.login.R
import com.auth.login.data.local.config.UserAutoLoginConfig
import com.auth.login.data.model.User
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

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            sp = context.getSharedPreferences(
                context.getString(R.string.user_auto_login),
                Context.MODE_PRIVATE
            )
            userAutoLoginConfig = UserAutoLoginConfig(context)
        }

    }

    /**
     * Save test
     * Input: user Value: correct
     * Result: save user in shared preferences
     */
    @Test
    fun testSave_UserParameter_UserSaveToSharedPreference() {
        try {
            //Arrange
            sp.edit().clear().commit()
            val user = User(email = "123@gmail.com", password = "Arma5651522", confirm =  "Arma5651522", recoveryCode = "2421587")
            //Act
            userAutoLoginConfig.save(user)
            //Assert
            Assert.assertNotNull(sp.getString(context.getString(R.string.email), ""))
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * getEmail test
     * Input: No Value: No
     * Result: get user email from shared preferences
     */
    @Test
    fun testGetEmail_NoParameter_GetUserEmailSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            val user = User(email = "123@gmail.com", password = "Arma5651522", confirm =  "Arma5651522", recoveryCode = "2421587")
            sp.edit {
                this.putString(context.getString(R.string.email),user.email)
                this.commit()
            }
            //Act&Assert
            Assert.assertNotNull(userAutoLoginConfig.getEmail())

        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * ClearAll test
     * Input: user Value: correct
     * Result: save user in shared preferences
     */
    @Test
    fun testClearAll_NoParameter_ClearSharedPreferencesSuccessfully() {
        try {
            //Arrange
            sp.edit().clear().commit()
            val user = User(email = "123@gmail.com", password = "Arma5651522", confirm =  "Arma5651522", recoveryCode = "2421587")
            sp.edit {
                this.putString(context.getString(R.string.email),user.email)
                this.commit()
            }
            //Act
            userAutoLoginConfig.clearAll()
            //Assert
            Assert.assertEquals("",userAutoLoginConfig.getEmail())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

}