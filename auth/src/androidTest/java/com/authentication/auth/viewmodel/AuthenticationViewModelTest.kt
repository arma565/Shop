package com.authentication.auth.viewmodel

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.data.local.IUserDao
import com.authentication.auth.data.local.UserDatabase
import com.authentication.auth.data.model.User
import com.authentication.auth.data.use_case.ValidateTerms
import com.authentication.auth.data.use_case.ValidateUserAlreadyExist
import com.authentication.auth.data.use_case.ValidateUserEmail
import com.authentication.auth.data.use_case.ValidateUserNotFound
import com.authentication.auth.data.use_case.ValidateUserPassword
import com.authentication.auth.data.use_case.ValidateUserRecoveryCode
import com.authentication.auth.data.use_case.ValidateUserRecoveryCodeNotExist
import com.authentication.auth.data.use_case.ValidateUserRepeatedPassword
import com.authentication.auth.viewmodel.repositories.FakeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class AuthenticationViewModelTest {

    companion object {
        private lateinit var context: Context
        private lateinit var database: UserDatabase
        private lateinit var dao: IUserDao
        private lateinit var viewModel: AuthenticationViewModel


        @JvmStatic
        @BeforeClass
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            database = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
            dao = database.userDao()
        }
    }

    @Before
    fun setUp() {
        viewModel = AuthenticationViewModel(
            FakeRepository(dao),
            ValidateUserEmail(context),
            ValidateUserPassword(context),
            ValidateUserRepeatedPassword(context),
            ValidateUserNotFound(context),
            ValidateUserAlreadyExist(context),
            ValidateUserRecoveryCode(context),
            ValidateUserRecoveryCodeNotExist(context),
            ValidateTerms(context)
        )
        database.clearAllTables()
    }

    /**
     * Insert user test
     * input: user value: valid
     * Result: user must be added to db
     */
    @Test
    fun testInsertUser_User_AddUserToDB() = runBlocking {
        try {
            //Arrange
            val user = User(
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            //Act
            viewModel.upsertUser(user)
            delay(3000)
            //Assert
            assertTrue(dao.usersListDesc().first().isNotEmpty())
        } catch (e: Exception) {
            fail(e.message)
        }
    }


    /**
     * get user list test
     * input: no value:no input
     * Result: FetchUserListSuccessfully
     */
    @Test
    fun testGetGetUserList_NoParameter_FetchUserListSuccessfully() = runBlocking {
        try {
            //Arrange
            val user = User(
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            dao.upsertUser(user)
            delay(3000)
            //Act&Assert
            assertTrue(viewModel.getUserList.value.isNotEmpty())
        } catch (e: Exception) {
            fail(e.message)
        }
    }


    /**
     * get user list test
     * input: no value:no input
     * Result: User list must be empty
     */
    @Test
    fun testGetGetUserList_NoParameter_ListMustBeEmpty() = runBlocking {
        try {
            //Arrange
            val user = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            dao.upsertUser(user)
            dao.deleteUser(user)
            //Act&Assert
            delay(3000)
            assertTrue(viewModel.getUserList.value.isEmpty())
        } catch (e: Exception) {
            fail(e.message)
        }
    }


    /**
     * Update user test
     * input: user value: valid
     * Result: user must be updated
     */
    @Test
    fun testUpdateUser_User_UserMustBeUpdated() = runBlocking {
        try {
            //Arrange
            val user = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            dao.upsertUser(user)
            val updatedUser = user.copy(
                id = 100,
                email = "reza@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            //Act
            viewModel.upsertUser(updatedUser)
            delay(3000)
            //Assert
            assertEquals(dao.usersListDesc().first().first().email, updatedUser.email)
        } catch (e: Exception) {
            fail(e.message)
        }
    }


    /**
     * Delete user test
     * input: user value: valid
     * Result: user must be deleted
     */
    @Test
    fun deleteUser_User_UserMustBeDeleted() = runBlocking {
        try {
            //Arrange
            val user = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            dao.upsertUser(user)
            //Act
            viewModel.deleteUser(user)
            delay(3000)
            //Assert
            assertTrue(dao.usersListDesc().first().isEmpty())
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    /**
     * Get specific user test
     * input: userID value: valid
     * Result: valid user must be obtain
     */
    @Test
    fun getSpecificUser_User_validUserMustBeObtain() = runBlocking {
        try {
            //Arrange
            val user1 = User(
                id = 100,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            val user2 = User(
                id = 120,
                email = "123@gmail.com",
                password = "Arma5651522",
                repeatedPassword = "Arma5651522",
                recoveryCode = "2421587"
            )
            dao.upsertUser(user1)
            dao.upsertUser(user2)
            delay(3000)
            //Act&Assert
            assertEquals(viewModel.getSpecificUser(user1.id).id, user1.id)
        } catch (e: Exception) {
            fail(e.message)
        }
    }
}