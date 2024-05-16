package com.auth.login

import com.auth.login.data.model.Constants
import com.auth.login.data.model.User
import com.auth.login.data.remote.AuthApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(MockitoJUnitRunner::class)
class AuthRemoteNetworkTest {

    companion object {

        @Mock
        private lateinit var mockAuthApiService: AuthApiService

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            mockAuthApiService = Mockito.mock(AuthApiService::class.java)
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            mockAuthApiService = retrofit.create(AuthApiService::class.java)
        }
    }


    /**
     * Test register
     * input: username and password valid: yes
     * output: Successfully create user
     */
    @Test
    fun testRegister_UsernameAndPassword_SuccessfullyCreateUser() = runBlocking {
        try {
            val user = User(
                id = 120,
                "john_123",
                "John",
                "Nolan",
                "+178457455",
                "john1234@gmail.com",
                password = "Arm123",
                "7845715",
                "Arm123",
                profilePhoto = null
            )
            Mockito.`when`(mockAuthApiService.register(user.email!!,user.password!!))
                .thenReturn(Response.success(user))
            // Assert the result
            Assert.assertEquals(user, mockAuthApiService.register(user.email!!,user.password!!).body())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Test login
     * input: username and password valid: yes
     * output: Successfully login
     */
    @Test
    fun testLogin_UsernameAndPassword_SuccessfullyLogin() = runBlocking {
        try {
            val user = User(
                id = 120,
                "john_123",
                "John",
                "Nolan",
                "+178457455",
                "john1234@gmail.com",
                password = "Arm123",
                "7845715",
                "Arm123",
                profilePhoto = null
            )
            Mockito.`when`(mockAuthApiService.login(user.email!!,user.password!!))
                .thenReturn(Response.success(user))
            // Assert the result
            Assert.assertEquals(user, mockAuthApiService.login(user.email!!,user.password!!).body())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

}