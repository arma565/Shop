package com.authentication.auth.data.use_case

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.R
import com.authentication.auth.data.local.IUserDao
import com.authentication.auth.data.local.UserDatabase
import com.authentication.auth.data.model.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class ValidateUserAlreadyExistTest {


    companion object {
        private lateinit var context: Context
        private lateinit var validateUserAlreadyExist: ValidateUserAlreadyExist
        private lateinit var database: UserDatabase
        private lateinit var dao: IUserDao

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            database = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
            dao = database.userDao()
            validateUserAlreadyExist = ValidateUserAlreadyExist(context)
        }
    }

    @Before
    fun setUp() {
        database.clearAllTables()
    }

    /**
     * Validate user already exist test
     * input: userInDb,user value: user
     * Result: return false
     */
    @Test
    fun testValidateUserAlreadyExist_inValidParameter_ReturnFalse() = runBlocking {
        val completableDeferred = CompletableDeferred<Boolean>()
        CoroutineScope(IO).launch {
            try {
                //Arrange
                val userInDb = User(
                    email = "reza@gmail.com",
                    password = "Arma5651522",
                    repeatedPassword = "Arma5651522",
                    recoveryCode = "2421587"
                )
                dao.upsertUser(userInDb)
                val user = User(
                    email = "reza@gmail.com",
                    password = "Arma5651522",
                    repeatedPassword = "Arma5651522",
                    recoveryCode = "2421587"
                )
                completableDeferred.complete(
                    !validateUserAlreadyExist.execute(
                        userInDb,
                        user
                    ).successful
                )
            } catch (e: Exception) {
                fail(e.message)
            }
        }
        //Act&Assert
        assertTrue(completableDeferred.await())
    }

    /**
     * Validate user already exist test
     * input: userInDb,user value: invalid user
     * Result: show valid error message
     */
    @Test
    fun testValidateUserAlreadyExist_inValidParameter_ShowValidErrorMessage() = runBlocking {
        try {
            //Arrange
            val userInDb = User(
                email = "reza@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            dao.upsertUser(userInDb)
            delay(3000)
            val user = User(
                email = "reza@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            //Act&Assert
            assertTrue(
                validateUserAlreadyExist.execute(userInDb, user).errorMessage == context.getString(
                    R.string.user_already_exist
                )
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Validate user already exist test
     * input: userInDb,user value: valid
     * Result: return true
     */
    @Test
    fun testValidateUserAlreadyExist_validParameter_ReturnTrue() = runBlocking {
        try {
            //Arrange
            val userInDb = User(
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            dao.upsertUser(userInDb)
            val user = User(
                email = "reza@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            //Act&Assert
            assertTrue(
                validateUserAlreadyExist.execute(
                    userInDb,
                    user
                ).successful
            )
        } catch (e: Exception) {
            fail(e.message)
        }
    }
}