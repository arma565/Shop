package com.authentication.auth.data.repository

import com.authentication.auth.data.model.Change
import com.authentication.auth.data.model.Login
import com.authentication.auth.data.model.Profile
import com.authentication.auth.data.model.Recovery
import com.authentication.auth.data.model.Register
import com.authentication.auth.data.model.Reset
import com.authentication.auth.data.model.User
import com.authentication.auth.data.remote.IAuthApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

open class AuthRepository @Inject constructor(
    private val authApiService: IAuthApiService
) {
    open fun uploadProfileImage(userName: String, file: File): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.uploadProfileImage(userName, prepareImageFilePart(file)))
    }

    open fun downloadProfileImage(userName: String): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.downloadProfileImage(userName))
    }

    open fun getAllUsers(): Flow<Response<List<User>>> = flow {
        emit(authApiService.getAllUsers())
    }

    open fun getUser(userName: String): Flow<Response<User>> = flow {
        emit(authApiService.getUser(userName))
    }

    open fun register(register: Register): Flow<Response<Register>> = flow {
        emit(authApiService.register(register))
    }

    open fun login(login: Login): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.login(login))
    }

    open fun deleteAll(): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.deleteAll())
    }

    open fun deleteUser(loginInfo: Login): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.deleteUser(loginInfo.userName, loginInfo.password))
    }

    open fun recovery(recovery: Recovery): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.recovery(recovery))
    }

    open fun reset(reset: Reset): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.reset(reset))
    }

    open fun change(change: Change): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.change(change))
    }

    open fun editProfile(profile: Profile): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.editProfile(profile))
    }

    private fun prepareImageFilePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}