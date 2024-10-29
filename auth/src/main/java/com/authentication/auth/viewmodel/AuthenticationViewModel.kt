package com.authentication.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authentication.auth.data.model.User
import com.authentication.auth.data.repository.UserRepository
import com.authentication.auth.data.use_case.ValidateTerms
import com.authentication.auth.data.use_case.ValidateUserAlreadyExist
import com.authentication.auth.data.use_case.ValidateUserEmail
import com.authentication.auth.data.use_case.ValidateUserNotFound
import com.authentication.auth.data.use_case.ValidateUserPassword
import com.authentication.auth.data.use_case.ValidateUserRecoveryCode
import com.authentication.auth.data.use_case.ValidateUserRecoveryCodeNotExist
import com.authentication.auth.data.use_case.ValidateUserRepeatedPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model
 * @param repository instance of NoteRepositoryImp
 */
@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: UserRepository,
    private val validateUserEmail: ValidateUserEmail,
    private val validateUserPassword: ValidateUserPassword,
    private val validateUserRepeatedPassword: ValidateUserRepeatedPassword,
    private val validateUserNotFound: ValidateUserNotFound,
    private val validateUserAlreadyExist: ValidateUserAlreadyExist,
    private val validateUserRecoveryCode: ValidateUserRecoveryCode,
    private val validateUserRecoveryCodeNotExist: ValidateUserRecoveryCodeNotExist,
    private val validateTerms: ValidateTerms
) : ViewModel() {

    private var _getUserListStateFlow: MutableStateFlow<List<User>> =
        MutableStateFlow(mutableListOf())
    var getUserList = _getUserListStateFlow.asStateFlow()

    init {
        prepareUserList()
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

            is UserEvent.CurrentUserPasswordChanged -> {
                personState =
                    personState.copy(currentUserPassword = event.currentUserPasswordChanged)
            }

            is UserEvent.PasswordChanged -> {
                personState = personState.copy(password = event.password)
            }

            is UserEvent.RepeatedPasswordChanged -> {
                personState =
                    personState.copy(repeatedPassword = event.repeatedPassword)
            }

            is UserEvent.RecoveryCodeChange -> {
                personState = personState.copy(recoveryCode = event.recoveryCode)
            }

            is UserEvent.AcceptTermsChanged -> {
                personState = personState.copy(acceptedTerms = event.isAccepted)
            }

            is UserEvent.ProfilePhotoChanged -> {
                personState = personState.copy(profilePhoto = event.profilePhoto)
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
                viewModelScope.launch {
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }

            is UserEvent.ChangePasswordSubmit -> {
                validateChangePassword()
            }
        }
    }

    private fun validateForgot() {
        val userList = getUserList.value
        val user = User(
            recoveryCode = personState.recoveryCode,
        )
        val userInDb: User =
            if (userList.any { it.recoveryCode == user.recoveryCode })
                userList.first { it.recoveryCode == user.recoveryCode }
            else
                User(email = "No User", password = "No User")

        val recoveryCodeResult = validateUserRecoveryCode.execute(user.recoveryCode)
        val recoveryCodeNotExistResult = validateUserRecoveryCodeNotExist.execute(
            userInDb.recoveryCode,
            user.recoveryCode
        )


        val hasError = listOf(
            recoveryCodeResult,
            recoveryCodeNotExistResult,
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                recoveryCodeError = recoveryCodeResult.errorMessage,
                recoveryCodeNotExistError = recoveryCodeNotExistResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun validateLogin() {
        val userList = getUserList.value
        val user = User(
            email = personState.email,
            password = personState.password,
        )
        val userInDb: User =
            if (userList.any { it.email == user.email && it.password == user.password })
                userList.first { it.email == user.email && it.password == user.password }
            else
                User(email = "No User", password = "No User")

        val emailResult = validateUserEmail.execute(user.email)
        val passwordResult = validateUserPassword.execute(user.password)
        val userNotFoundResult = validateUserNotFound.execute(
            userInDb,
            user
        )
        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }
        if (!userNotFoundResult.successful) {
            personState = personState.copy(
                emailError = "",
                passwordError = "",
                userNotFoundError = userNotFoundResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun validateRecovery() {
        val user = User(
            password = personState.password,
            repeatedPassword = personState.repeatedPassword
        )
        val passwordResult = validateUserPassword.execute(user.password)
        val repeatedPasswordResult = validateUserRepeatedPassword.execute(
            user.password,
            user.repeatedPassword
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
        val userList = getUserList.value
        val user = User(
            email = personState.email,
            password = personState.password,
            repeatedPassword = personState.repeatedPassword,
            acceptTerms = personState.acceptedTerms,
        )
        val userInDb: User =
            if (userList.any { it.email == user.email && it.password == user.password })
                userList.first { it.email == user.email && it.password == user.password }
            else
                User(email = "No User", password = "No User")

        val emailResult = validateUserEmail.execute(user.email)
        val passwordResult = validateUserPassword.execute(user.password)
        val repeatedPasswordResult = validateUserRepeatedPassword.execute(
            user.password,
            user.repeatedPassword
        )
        val acceptTermsResult = validateTerms.execute(user.acceptTerms)

        val userAlreadyExistResult = validateUserAlreadyExist.execute(
            userInDb,
            user
        )

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            acceptTermsResult
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                acceptTermsError = acceptTermsResult.errorMessage
            )
            return
        }

        if (!userAlreadyExistResult.successful) {
            personState = personState.copy(
                emailError = "",
                passwordError = "",
                repeatedPasswordError = "",
                acceptTermsError = "",
                userAlreadyExistError = userAlreadyExistResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun validateChangePassword() {
        val userList = getUserList.value
        val user = User(
            email = personState.email,
            password = personState.currentUserPassword
        )
        val userInDb: User =
            if (userList.any { it.email == user.email && it.password == user.password })
                userList.first { it.email == user.email && it.password == user.password }
            else
                User(email = "No User", password = "No User")

        val currentPasswordResult = validateUserPassword.execute(personState.currentUserPassword)
        val newPasswordResult = validateUserPassword.execute(personState.password)
        val newRepeatedPasswordResult = validateUserRepeatedPassword.execute(
            personState.password,
            personState.repeatedPassword
        )
        val userNotFoundResult = validateUserNotFound.execute(
            userInDb,
            user
        )
        val hasError = listOf(
            currentPasswordResult,
            newPasswordResult,
            newRepeatedPasswordResult
        ).any { !it.successful }

        if (hasError) {
            personState = personState.copy(
                currentUserPasswordError = currentPasswordResult.errorMessage,
                passwordError = newPasswordResult.errorMessage,
                repeatedPasswordError = newRepeatedPasswordResult.errorMessage
            )
            return
        }

        if (!userNotFoundResult.successful) {
            personState = personState.copy(
                currentUserPasswordError = "",
                passwordError = "",
                repeatedPasswordError = "",
                userNotFoundError = userNotFoundResult.errorMessage
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

    /**
     * Upsert method
     * @param user: Object of note model
     * This method insert or update a user
     */
    fun upsertUser(user: User) {
        viewModelScope.launch {
            repository.upsertUser(user)
        }
    }

    /**
     * Get user list
     * This method get user list from database
     */
    private fun prepareUserList() {
        viewModelScope.launch(IO) {
            repository.usersListDesc().collect {
                _getUserListStateFlow.value = it
            }
        }
    }

    /**
     * Delete method
     * @param user: Object of user model
     * This method delete a user from database
     */
    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    /**
     * Get specific user using id
     * @param id userId
     * This method get a user using userId from database
     */
    fun getSpecificUser(id: Int): User {
        val get = getUserList.value
        if (get.any { it.id == id }) {
            return get.first { it.id == id }
        }
        return User()
    }
}