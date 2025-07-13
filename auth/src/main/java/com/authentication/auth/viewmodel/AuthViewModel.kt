package com.authentication.auth.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authentication.auth.data.config.AccountRecoveryPreferences
import com.authentication.auth.data.config.UserAutoLoginPreferencesRepository
import com.authentication.auth.data.model.Change
import com.authentication.auth.data.model.Login
import com.authentication.auth.data.model.Profile
import com.authentication.auth.data.model.Recovery
import com.authentication.auth.data.model.Register
import com.authentication.auth.data.model.Reset
import com.authentication.auth.data.model.User
import com.authentication.auth.data.repository.AuthRepository
import com.authentication.auth.data.use_case.ValidateFirstName
import com.authentication.auth.data.use_case.ValidateLastName
import com.authentication.auth.data.use_case.ValidatePhoneNumber
import com.authentication.auth.data.use_case.ValidateTerms
import com.authentication.auth.data.use_case.ValidateUserEmail
import com.authentication.auth.data.use_case.ValidateUserName
import com.authentication.auth.data.use_case.ValidateUserPassword
import com.authentication.auth.data.use_case.ValidateUserRepeatedPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userAutoLoginPreferencesRepository: UserAutoLoginPreferencesRepository,
    private val accountRecoveryPreferences: AccountRecoveryPreferences,
    @param:ApplicationContext private val context: Context
) : ViewModel() {



    private val _getAllUsersFlow = MutableStateFlow<List<User>>(emptyList())
    val getAllUsersFlow: StateFlow<List<User>> = _getAllUsersFlow.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user : StateFlow<User> = _user.asStateFlow()

    private val _profilePhoto = MutableStateFlow<Bitmap?>(null)
    val profilePhoto: StateFlow<Bitmap?> = _profilePhoto.asStateFlow()


    init {
        getAllUsers()
    }

    var personState by mutableStateOf(UserValidationState())

    private val validationEventChannel = Channel<ValidationEvent>()
    var validationEvent = validationEventChannel.receiveAsFlow()

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UserNameChanged -> {
                personState = personState.copy(userName = event.userName)
            }

            is UserEvent.FirstNameChanged -> {
                personState = personState.copy(firstName = event.firstName)
            }

            is UserEvent.LastNameChanged -> {
                personState = personState.copy(lastName = event.lastName)
            }

            is UserEvent.PhoneNumberChangedChanged -> {
                personState = personState.copy(phoneNumber = event.phoneNumber)
            }

            is UserEvent.EmailChanged -> {
                personState = personState.copy(email = event.email)
            }

            is UserEvent.TokenChanged -> {
                personState = personState.copy(token = event.token)
            }

            is UserEvent.CurrentUserPasswordChanged -> {
                personState =
                    personState.copy(currentUserPassword = event.currentUserPasswordChanged)
            }

            is UserEvent.PasswordChanged -> {
                personState = personState.copy(password = event.password)
            }

            is UserEvent.RepeatedPasswordChanged -> {
                personState = personState.copy(repeatedPassword = event.repeatedPassword)
            }

            is UserEvent.AcceptTermsChanged -> {
                personState = personState.copy(acceptedTerms = event.isAccepted)
            }

            is UserEvent.ForgotSubmit -> {
                validateForgot()
            }

            is UserEvent.LoginSubmit -> {
                validateLogin()
            }

            is UserEvent.RecoverySubmit -> {
                validateRecovery()
            }

            is UserEvent.RegisterSubmit -> {
                validateRegister()
            }

            is UserEvent.UpdateProfileSubmit -> {
                validateUpdateProfile()
            }
        }
    }

    private fun validateForgot() {
        val recovery = Recovery(
            email = personState.email
        )
        val emailResult = ValidateUserEmail(context).execute(recovery.email)
        val hasError = listOf(
            emailResult
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                emailError = emailResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun validateLogin() {
        val login = Login(
            userName = personState.userName,
            password = personState.password,
        )
        val userNameResult = ValidateUserName(context).execute(login.userName)
        val passwordResult = ValidateUserPassword(context).execute(login.password)

        val hasError = listOf(
            userNameResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                userNameError = userNameResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun validateRecovery() {
        val reset = Reset(
            newPassword = personState.password,
            repeatNewPassword = personState.repeatedPassword
        )
        val passwordResult = ValidateUserPassword(context).execute(reset.newPassword)
        val repeatedPasswordResult = ValidateUserRepeatedPassword(context).execute(
            reset.newPassword,
            reset.repeatNewPassword
        )
        val hasError = listOf(
            passwordResult,
            repeatedPasswordResult
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun validateRegister() {

        val register = Register(
            userName = personState.userName,
            email = personState.email,
            password = personState.password,
            repeatPassword = personState.repeatedPassword,
            acceptTerms = personState.acceptedTerms
        )
        val userNameResult = ValidateUserName(context).execute(register.userName)
        val emailResult = ValidateUserEmail(context).execute(register.email)
        val passwordResult = ValidateUserPassword(context).execute(register.password)
        val repeatedPasswordResult = ValidateUserRepeatedPassword(context).execute(
            register.password,
            register.repeatPassword
        )
        val acceptTermsResult = ValidateTerms(context).execute(register.acceptTerms)
        val hasError = listOf(
            userNameResult,
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            acceptTermsResult
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                userNameError = userNameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                acceptTermsError = acceptTermsResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun validateUpdateProfile() {
        val profile = Profile(
            userName = personState.userName,
            firstName = personState.firstName,
            lastName = personState.lastName,
            phoneNumber = personState.phoneNumber
        )
        val userNameResult = ValidateUserName(context).execute(profile.userName)
        val firstNameResult = ValidateFirstName(context).execute(profile.firstName)
        val lastNameResult = ValidateLastName(context).execute(profile.lastName)
        val phoneNumberResult = ValidatePhoneNumber(context).execute(profile.phoneNumber)

        val hasError = listOf(
            userNameResult,
            firstNameResult,
            lastNameResult,
            phoneNumberResult
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                userNameError = userNameResult.errorMessage,
                firstNameError = firstNameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                phoneNumberError = phoneNumberResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
    }

    fun uploadProfileImage(userName: String, file: File, onResult: (Response<ResponseBody>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                if (!file.exists()) {
                    throw FileNotFoundException("File not exist!")
                }
                repository.uploadProfileImage(userName, file).collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: FileNotFoundException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun downloadProfileImage(userName: String) {
        viewModelScope.launch(IO) {
            try {
                repository.downloadProfileImage(userName).collect { res ->
                    if (res.isSuccessful && res.body() != null) {
                        saveDownloadedImageToCacheDir(res.body()!!,userName){downloadedFile->
                            viewModelScope.launch(IO) {
                                val bitmapImg =  fileToBitmap(downloadedFile)
                                if (bitmapImg != null)
                                    _profilePhoto.emit(bitmapImg)
                                else
                                    _profilePhoto.emit(null)
                            }
                        }
                    }else{
                        Log.e("API", "Response error: ${res.errorBody()?.string()}")
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch(IO) {
            try {
                repository.getAllUsers().collect { res ->
                    _getAllUsersFlow.value = res.body()!!
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun getUser(userName: String) {
        viewModelScope.launch(IO) {
            try {
                repository.getUser(userName).collect { res ->
                    res.body().let {
                        _user.emit(res.body()!!)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }

        }
    }

    fun registerUser(register: Register, onResult: (Response<Register>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                repository.register(register).collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                        getAllUsers()
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }


        }
    }

    fun loginUser(login: Login, onResult: (Response<ResponseBody>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                repository.login(login).collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun deleteAll(onResult: (Response<ResponseBody>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                repository.deleteAll().collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun deleteUser(loginInfo: Login, onResult: (Response<ResponseBody>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                repository.deleteUser(loginInfo).collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun recovery(recovery: Recovery, onResult: (Response<ResponseBody>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                repository.recovery(recovery).collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun reset(reset: Reset, onResult: (Response<ResponseBody>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                repository.reset(reset).collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun change(change: Change, onResult: (Response<ResponseBody>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                repository.change(change).collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    fun editProfile(profile: Profile, onResult: (Response<ResponseBody>) -> Unit) {
        viewModelScope.launch(IO) {
            try {
                repository.editProfile(profile).collect { res ->
                    this@launch.launch(Main) {
                        onResult(res)
                    }
                }
            } catch (e: IOException) {
                // Handles timeout, no internet, failed to connect
                Log.e("API", "Network error: ${e.message}")

            } catch (e: HttpException) {
                // Handles non-2xx responses
                Log.e("API", "HTTP error: ${e.code()} ${e.message}")

            } catch (e: Exception) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Generic catch for anything else
                Log.e("API", "Unknown error: ${e.message}")
            }
        }
    }

    /**
     * Save user info into shared preferences
     */
    fun saveUserInfoIntoSharedPreferences(userName: String, isRememberMe: Boolean) {
        userAutoLoginPreferencesRepository.save(userName, isRememberMe)
    }

    /**
     * Get user info from shared preferences
     */
    fun getUserNameFromSharedPreferences(): String? =
        userAutoLoginPreferencesRepository.getUserName()

    /**
     * Get user login state from shared preferences(if user check remember me)
     */
    fun getRememberCheckFromSharedPreferences(): Boolean =
        userAutoLoginPreferencesRepository.getRememberCheck()

    /**
     * Save email and token into shared preferences
     */
    fun setEmailAndToken(email: String, token: String) {
        accountRecoveryPreferences.set(email, token)
    }

    /**
     * Get email and token from shared preferences
     */
    fun getEmailFromSharedPreferences(): String? = accountRecoveryPreferences.getEmail()

    /**
     * Get token from shared preferences
     */
    fun getTokenFromSharedPreferences(): String? = accountRecoveryPreferences.getToken()

    fun clearCache() {
        personState = UserValidationState()
    }

    fun saveBitmapToCacheDir(
        context: Context,
        bitmap: Bitmap,
        onBitmapSaved: (savedFile: File?) -> Unit
    ) {
        viewModelScope.launch(IO) {
            val fileName = UUID.randomUUID().toString() + ".png"
            // Create a file in the cache directory
            val file = File(context.cacheDir, fileName)

            try {
                // Open a file output stream and write the bitmap to it
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                    onBitmapSaved(file) // Return the saved file
                }
            } catch (e: IOException) {
                e.printStackTrace()
                onBitmapSaved(null) // Return null if there's an error
            } catch (e: Exception) {
                e.printStackTrace()
                onBitmapSaved(null) // Return null if there's an error
            }
        }
    }

    private fun saveDownloadedImageToCacheDir(result : ResponseBody , userName: String , onResult: (file : File) -> Unit){
        viewModelScope.launch(IO) {
            val outputDir = File(context.cacheDir, "download/auth")
            if (!outputDir.exists()) {
                outputDir.mkdir()
            }
            result.let { downloadedFile ->
                val outPutFile = File(outputDir, userName)
                val inputStream = downloadedFile.byteStream()
                val outPutStream = FileOutputStream(outPutFile)
                inputStream.use { input ->
                    outPutStream.use { outPut ->
                        input.copyTo(outPut)
                    }
                }
                onResult(outPutFile)
            }
        }
    }

    private fun fileToBitmap(file: File): Bitmap? {
        return try {
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}