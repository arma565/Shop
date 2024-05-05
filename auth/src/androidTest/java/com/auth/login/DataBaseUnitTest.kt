package com.auth.login

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.auth.login.data.local.IAuthDao
import com.auth.login.data.local.AuthDatabase
import com.auth.login.data.model.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataBaseUnitTest {

    companion object {
        private lateinit var context: Context
        private lateinit var database: AuthDatabase
        private lateinit var dao: IAuthDao

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            database = Room.inMemoryDatabaseBuilder(context, AuthDatabase::class.java).build()
            dao = database.dao()
        }

        @AfterClass
        @JvmStatic
        fun teardownClass() {
            database.close()
        }
    }

    @Before
    fun setup() {
        database.clearAllTables()
    }

    /**
     * Create user test
     * Parameter: User Value: Valid
     * Result: Successfully create user in db
     */
    @Test
    fun testCreateUser_User_UserCreateSuccessfully() {
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val user = User(email = "123@gmail.com", password =  "Arma5651522", confirm =  "Arma5651522", recoveryCode =  "48752")
                    //Act
                    dao.upsertUser(user)
                    completableDeferred.complete(dao.usersListDesc().isNotEmpty())
                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }

    /**
     * Get user list test
     * Parameter: No parameter Value: No parameter
     * Result: Successfully get user list from db
     */
    @Test
    fun testGetUserList_NoParameter_GetUserListSuccessfully() {
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val user = User(email = "123@gmail.com", password =  "Arma5651522", confirm =  "Arma5651522", recoveryCode =  "48752")
                    dao.upsertUser(user)
                    //Act
                    completableDeferred.complete(dao.usersListDesc().isNotEmpty())
                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }

    /**
     * Update user test
     * Parameter: User Value: Valid
     * Result: Successfully update user in db
     */
    @Test
    fun testUpdateUser_User_UserUpdateSuccessfully() {
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val user = User(
                        120,
                        "arma_258",
                        "John",
                        "Nolan",
                        "+984575221",
                        "123@gmail.com",
                        "Arma5651522",
                        "Arma5651522",
                        "48752",
                        null
                    )
                    dao.upsertUser(user)
                    //Act
                    dao.upsertUser(
                        User(
                            120,
                            "arma_565",
                            "Joseph",
                            "Jackson",
                            "+1897445124",
                            "123@gmail.com",
                            "Arma5651522",
                            "748975",
                            "Arma5651522",
                            null
                        )
                    )
                    completableDeferred.complete(
                        dao.usersListDesc().first().firstName != user.firstName
                    )
                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }

    }

    /**
     * Delete user test
     * Parameter: User Value: Invalid user id
     * Result: Does not delete
     */
    @Test
    fun testDeleteUser_InvalidUser_DoesNotDelete() {
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val user = User(email = "123@gmail.com", password =  "Arma5651522", confirm =  "Arma5651522", recoveryCode =  "48752")
                    dao.upsertUser(user)
                    //Act
                    dao.deleteUser(User(2332))
                    completableDeferred.complete(dao.usersListDesc().isNotEmpty())
                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }

    /**
     * Delete user test
     * Parameter: User Value: Valid user id
     * Result: Successfully delete user
     */
    @Test
    fun testDeleteUser_ValidUser_SuccessfullyDeleteUser() {
        val completableDeferred = CompletableDeferred<Boolean>()
        CoroutineScope(IO).launch {
            try {
                //Arrange
                val user = User(
                    100,
                    "arma_258",
                    "john",
                    "Nolan",
                    "+81475744",
                    "123@gmail.com",
                    "Arma5651522",
                    "Arma5651522",
                    "48752",
                    null
                )
                dao.upsertUser(user)
                //Act
                dao.deleteUser(user)
                completableDeferred.complete(dao.usersListDesc().isEmpty())
            } catch (e: Exception) {
                Assert.fail(e.message)
            }
        }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }

    /**
     * Delete all users test
     * Parameter: None Value: None
     * Result: Successfully delete all users
     */
    @Test
    fun testDeleteAllUser_NoParameters_SuccessfullyDeleteAllUsers() {
        val completableDeferred = CompletableDeferred<Boolean>()
        CoroutineScope(IO).launch {
            try {
                //Arrange
                val user1 = User(
                    100,
                    "arma_258",
                    "john",
                    "Nolan",
                    "+81475744",
                    "123@gmail.com",
                    "Arma5651522",
                    "Arma5651522",
                    "48752",
                    null
                )

                val user2 = User(
                    120,
                    "arma_258",
                    "john",
                    "Nolan",
                    "+81475744",
                    "123@gmail.com",
                    "Arma5651522",
                    "Arma5651522",
                    "48752",
                    null
                )
                dao.upsertUser(user1)
                dao.upsertUser(user2)
                //Act
                dao.deleteAllUsers()
                completableDeferred.complete(dao.usersListDesc().isEmpty())
            } catch (e: Exception) {
                Assert.fail(e.message)
            }
        }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }
}