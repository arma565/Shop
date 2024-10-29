package com.authentication.auth.data.use_case

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.R
import com.authentication.auth.data.local.IUserDao
import com.authentication.auth.data.local.UserDatabase
import com.authentication.auth.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class ValidateUserRecoveryCodeNotExistTest {

    companion object {
        private lateinit var context: Context
        private lateinit var validateUserRecoveryCodeNotExist: ValidateUserRecoveryCodeNotExist
        private lateinit var database: UserDatabase
        private lateinit var dao: IUserDao

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            database = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
            dao = database.userDao()
            validateUserRecoveryCodeNotExist = ValidateUserRecoveryCodeNotExist(context)
        }
    }

    @Before
    fun setUp() {
        database.clearAllTables()
    }

    /**
     * Validate user recovery code not exist test
     * input: recovery code value: inValid recovery code
     * Result: return false
     */
    @Test
    fun testValidateUserRecoveryCode_InValidParameter_ReturnFalse() = runBlocking {
        try {
            //Arrange
            val userInDb = User(
                email = "reza@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "242335656"
            )
            dao.upsertUser(userInDb)
            delay(3000)
            val user = User(
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "24233123123"
            )
            //Act&Assert
            assertTrue(
                !validateUserRecoveryCodeNotExist.execute(
                    userInDb.recoveryCode,
                    user.recoveryCode
                ).successful
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user recovery code not exist test
     * input: recovery code value: inValid recovery code
     * Result: show valid error message
     */
    @Test
    fun testValidateUserRecoveryCode_InValidParameter_ShowValidErrorMessage() = runBlocking {
        try {
            //Arrange
            val userInDb = User(
                email = "reza@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "242335656"
            )
            dao.upsertUser(userInDb)
            delay(3000)
            val user = User(
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "12345123"
            )
            //Act&Assert
            assertTrue(
                validateUserRecoveryCodeNotExist.execute(
                    userInDb.recoveryCode,
                    user.recoveryCode
                ).errorMessage == context.getString(
                    R.string.recovery_code_is_incorrect
                )
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user recovery code not exist test
     * input: recovery code value: valid recovery code
     * Result: return true
     */
    @Test
    fun testValidateUserRecoveryCode_validParameter_ReturnTrue() = runBlocking {
        try {
            //Arrange
            val userInDb = User(
                email = "reza@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "242335656"
            )
            dao.upsertUser(userInDb)
            val user = User(
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "242335656"
            )
            //Act&Assert
            assertTrue(
                validateUserRecoveryCodeNotExist.execute(
                    userInDb.recoveryCode,
                    user.recoveryCode
                ).successful
            )
        } catch (e: Exception) {
            fail(e.message)
        }

    }
}