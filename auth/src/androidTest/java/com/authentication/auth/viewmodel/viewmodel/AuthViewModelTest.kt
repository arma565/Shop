package com.authentication.auth.viewmodel.viewmodel

import android.app.Instrumentation
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.test.platform.app.InstrumentationRegistry
import com.authentication.auth.data.config.AccountRecoveryPreferences
import com.authentication.auth.data.config.UserAutoLoginPreferencesRepository
import com.authentication.auth.data.model.Change
import com.authentication.auth.data.model.Login
import com.authentication.auth.data.model.Profile
import com.authentication.auth.data.model.Recovery
import com.authentication.auth.data.model.Register
import com.authentication.auth.data.model.Reset
import com.authentication.auth.data.remote.IAuthApiService
import com.authentication.auth.viewmodel.AuthViewModel
import com.authentication.auth.viewmodel.repository.AuthFakeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class AuthViewModelTest {

    companion object {
        private lateinit var context: Context
        private const val BASE_URL = "http://10.0.2.2:5068"
        private lateinit var mockWebServer: MockWebServer
        private lateinit var apiAuthService: IAuthApiService
        private lateinit var fakeRepository: AuthFakeRepository
        private lateinit var authViewModel: AuthViewModel
        private lateinit var registerUser: Register
        private lateinit var profile: Profile
        private lateinit var instrumentation: Instrumentation
    }

    @Before
    fun setup() {
        instrumentation = InstrumentationRegistry.getInstrumentation()
        context = instrumentation.targetContext
        mockWebServer = MockWebServer()
        mockWebServer.start()
        apiAuthService = createRetrofit().create(IAuthApiService::class.java)
        fakeRepository = AuthFakeRepository(apiAuthService)
        authViewModel = AuthViewModel(
            fakeRepository,
            UserAutoLoginPreferencesRepository(context),
            AccountRecoveryPreferences(
                context
            ), context
        )
        registerUser = Register(
            userName = "arma_565",
            email = "123@gmail.com",
            password = "Arma@565",
            repeatPassword = "Arma@565",
            acceptTerms = true
        )
        profile = Profile(
            userName = registerUser.userName,
            firstName = "rez",
            lastName = "Gor",
            phoneNumber = "+1213232"
        )
    }

    @After
    fun tearDown() {
        registerUser = Register()
        mockWebServer.shutdown()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url(BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun bitmapToFile(bitmap: Bitmap): File? {
        return try {
            val file = File(context.cacheDir.absolutePath)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun writeBitmapToPngFile(file: File, bitmap: Bitmap) {
        try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to write Bitmap to PNG file", e)
        }
    }

    private fun createSampleBitmap(): Bitmap {
        // Create a simple bitmap with a red circle
        val width = 200
        val height = 200
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE) // Background color

        // Draw a red circle
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
        }
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 50f, paint)

        return bitmap
    }

    private fun prepareImageFilePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    @Test
    fun testUpload_ProfileImage_InValidUserName_ValidFile_ServerRespondErrorMustBeNoSuchUserFound() =
        runBlocking {
            try {
                //Arrange
                apiAuthService.deleteAll()
                apiAuthService.register(registerUser)
                val dir = context.cacheDir
                val bitmap = createSampleBitmap()
                val fileName = "testImage.png"
                val file = File(dir, fileName)
                writeBitmapToPngFile(file, bitmap)
                //Act
                authViewModel.uploadProfileImage("123", file) { res ->
                    //Assert
                    assertTrue(
                        !res.isSuccessful && res.errorBody()?.string() == "No such user found!"
                    )
                }
                delay(5000L)
            } catch (e: Exception) {
                fail(e.message)
            } finally {
                apiAuthService.deleteAll()
                mockWebServer.shutdown()
            }
        }

    @Test
    fun testUpload_ProfileImage_ValidUserName_InValidFile_ThrowFileNotExistError() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            val dir = context.cacheDir
            val fileName = "123.png"
            val file = File(dir, fileName)
            //Act
            authViewModel.uploadProfileImage(registerUser.userName, file) {
                assert(false)
            }
            delay(5000L)
        } catch (e: FileNotFoundException) {
            assertTrue(e.message == "File not exist!")
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testUpload_ProfileImage_ValidUserName_ValidFile_ResponseMustBeSuccessful() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            val dir = context.cacheDir
            val bitmap = createSampleBitmap()
            val fileName = "testImage.png"
            val file = File(dir, fileName)
            writeBitmapToPngFile(file, bitmap)
            //Act
            authViewModel.uploadProfileImage(registerUser.userName, file) { res ->
                //Assert
                assertTrue(res.isSuccessful)
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }



    @Test
    fun testDownload_InvalidUserName_DownloadProfileImageUnSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.register(registerUser)
            val dir = context.cacheDir
            val bitmap = createSampleBitmap()
            val fileName = "testImage.png"
            val file = File(dir, fileName)
            writeBitmapToPngFile(file, bitmap)
            val imagePart = prepareImageFilePart(file)
            apiAuthService.uploadProfileImage(
                registerUser.userName,
                imagePart
            )
            val outputDir = File(dir, "download")
            if (!outputDir.exists()) {
                outputDir.mkdir()
            }
            //Act
            authViewModel.downloadProfileImage("123")

            //Assert
            assertNull(authViewModel.profilePhoto)
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testDownload_validUserName_DownloadProfileImageSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            val dir = context.cacheDir
            val bitmap = createSampleBitmap()
            val fileName = "testImage.png"
            val file = File(dir, fileName)
            writeBitmapToPngFile(file, bitmap)
            val imagePart = prepareImageFilePart(file)
            apiAuthService.uploadProfileImage(
                registerUser.userName,
                imagePart
            )
            val outputDir = File(dir, "download")
            if (!outputDir.exists()) {
                outputDir.mkdir()
            }
            //Act
            authViewModel.downloadProfileImage(registerUser.userName)

            //Assert
            assertTrue(bitmapToFile(authViewModel.profilePhoto.value!!)!!.exists())
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testGetAllUsers_NoParameter_GetUsersSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.getAllUsers()
            delay(5000L)
            //Assert
            assert(authViewModel.getAllUsersFlow.value.isNotEmpty())
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testGetUser_UserNameParameter_ActualRegisteredUserEqualsToResponseUser() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            //Act
            authViewModel.getUser(registerUser.userName)
            //Assert
            assertEquals(authViewModel.user.value.username, registerUser.userName)
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testGetUser_NoParameter_ResponseUnsuccessful() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.getUser("")
            //Assert
            assertNull(authViewModel.user.value)
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testRegister_ValidInputRegisterParameter_ActualRegisteredUserEqualsToResponseRegisteredUser() =
        runBlocking {
            try {
                //Arrange
                apiAuthService.deleteAll()
                //Act
                authViewModel.registerUser(
                    registerUser
                ) { res ->
                    //Assert
                    assert(res.isSuccessful && res.body()?.userName == registerUser.userName)
                }
                delay(5000L)
            } catch (e: Exception) {
                fail(e.message)
            } finally {
                apiAuthService.deleteAll()
                mockWebServer.shutdown()
            }
        }

    @Test
    fun testRegister_InValidInputRegisterParameter_ResponseUnsuccessful() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            //Act
            authViewModel.registerUser(Register()) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testLoginUser_InValidUserName_ValidPassword_LoginFailed() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.loginUser(Login("sss@343", registerUser.password)) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Unauthorized")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testLoginUser_ValidUserName_InValidPassword_LoginFailed() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.loginUser(
                Login(
                    registerUser.userName, "germany"
                )
            ) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Unauthorized")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testLogin_ValidInputLoginParameter_LoginUserSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.loginUser(
                Login(
                    registerUser.userName,
                    registerUser.password
                )
            ) { res ->
                //Assert
                assert(res.isSuccessful)
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testDeleteAllUsers_NoInputParameter_DeleteAllUsersSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.deleteAll {
                runBlocking {
                    //Assert
                    val res = apiAuthService.getAllUsers()
                    assert(res.body()?.isEmpty()!!)
                }
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testDeleteUser_InValidUserName_ValidPassword_UnsuccessfulRespond() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.deleteUser(
                Login(
                    userName = "",
                    password = registerUser.password
                )
            ) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Not Found")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testDeleteUser_ValidUserName_InValidPassword_UnsuccessfulRespond() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.deleteUser(
                Login(
                    userName = registerUser.userName,
                    password = "2131"
                )
            ) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testDeleteUser_ValidUserName_ValidPassword_DeleteUserSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.deleteUser(
                Login(
                    registerUser.userName,
                    registerUser.password
                )
            ) { res ->
                //Assert
                assert(res.isSuccessful)
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testRecoverUser_InValidEmail_UnsuccessfulRespond() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.recovery(Recovery("66666@gmail.com")) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testRecoverUser_ValidEmail_TokenMustNotBeNullOrEmpty() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.recovery(
                Recovery(
                    registerUser.email
                )
            ) { res ->
                //Assert
                assert(res.body()?.string()?.isNotEmpty()!!)
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testResetPassword_InvalidEmailParameter_UnsuccessfulRespond() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            val token = apiAuthService.recovery(
                Recovery(
                    registerUser.email
                )
            ).body()?.string()
                .toString()
            //Act
            authViewModel.reset(
                Reset(
                    email = "435@gmail.com",
                    token = token,
                    newPassword = "Arma@145",
                    repeatNewPassword = "Arma@145"
                )
            ) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Not Found")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testResetPassword_InvalidNewPassword_ResponseMustBeTokenError() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            val token = apiAuthService.recovery(
                Recovery(
                    registerUser.email
                )
            ).body()?.string()
                .toString()
            //Act
            authViewModel.reset(
                Reset(
                    email = registerUser.email,
                    token = token,
                    newPassword = "123",
                    repeatNewPassword = "Arma@123"
                )
            ) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testResetPassword_InvalidRepeatNewPassword_ResponseMustBeTokenError() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            val token = apiAuthService.recovery(
                Recovery(
                    registerUser.email
                )
            ).body()?.string()
                .toString()
            //Act
            authViewModel.reset(
                Reset(
                    email = registerUser.email,
                    token = token,
                    newPassword = "Arma@123",
                    repeatNewPassword = "123"
                )
            ) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }


    @Test
    fun testResetPassword_ValidParameters_ResetPasswordSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            val token = apiAuthService.recovery(
                Recovery(
                    registerUser.email
                )
            ).body()?.string()
                .toString()
            //Act
            authViewModel.reset(
                Reset(
                    email = registerUser.email,
                    token = token,
                    newPassword = "Arma@123",
                    repeatNewPassword = "Arma@123"
                )
            ) { res ->
                //Assert
                assert(res.isSuccessful)
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testChangePassword_InvalidUsername_BadRequestResponseError() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.change(
                Change(
                    userName = "123",
                    currentPassword = registerUser.password,
                    newPassword = "Arma@123",
                    repeatNewPassword = "Arma@123"
                )
            ) { res ->
                //Assert
                assertTrue(res.errorBody()?.string() == "No such user found!")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testChangePassword_InvalidCurrentPassword_BadRequestResponseError() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.change(
                Change(
                    userName = registerUser.userName,
                    currentPassword = "213213213",
                    newPassword = "Arma@123",
                    repeatNewPassword = "Arma@123"
                )
            ) { res ->
                //Assert
                assert(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testChangePassword_ValidParameters_ChangePasswordSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(
                registerUser
            )
            //Act
            authViewModel.change(
                Change(
                    userName = registerUser.userName,
                    currentPassword = registerUser.password,
                    newPassword = "Arma@123",
                    repeatNewPassword = "Arma@123"
                )
            ) { res ->
                //Assert
                assert(res.isSuccessful)
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testEditProfile_InValidUserName_InvalidUserNameErrorResponse() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            //Act
            authViewModel.editProfile(
                Profile(
                    userName = "123",
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    phoneNumber = profile.phoneNumber
                )
            ) { res ->
                //Assert
                assertTrue(!res.isSuccessful && res.errorBody()?.string() == "No such user found!")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testEditProfile_EmptyFirstName_ErrorResponse() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            //Act
            authViewModel.editProfile(
                Profile(
                    userName = registerUser.userName,
                    firstName = "",
                    lastName = profile.lastName,
                    phoneNumber = profile.phoneNumber
                )
            ) { res ->
                //Assert
                assertTrue(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testEditProfile_EmptyLastName_ErrorResponse() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            //Act
            authViewModel.editProfile(
                Profile(
                    userName = registerUser.userName,
                    firstName = profile.firstName,
                    lastName = "",
                    phoneNumber = profile.phoneNumber
                )
            ) { res ->
                //Assert
                assertTrue(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testEditProfile_EmptyPhoneNumber_ErrorResponse() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            //Act
            authViewModel.editProfile(
                Profile(
                    userName = registerUser.userName,
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    phoneNumber = ""
                )
            ) { res ->
                //Assert
                assertTrue(!res.isSuccessful && res.message() == "Bad Request")
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }

    @Test
    fun testEditProfile_ValidProfile_EditProfileSuccessfully() = runBlocking {
        try {
            //Arrange
            apiAuthService.deleteAll()
            apiAuthService.register(registerUser)
            //Act
            authViewModel.editProfile(profile) { res ->
                //Assert
                assertTrue(res.isSuccessful)
            }
            delay(5000L)
        } catch (e: Exception) {
            fail(e.message)
        } finally {
            apiAuthService.deleteAll()
            mockWebServer.shutdown()
        }
    }
}