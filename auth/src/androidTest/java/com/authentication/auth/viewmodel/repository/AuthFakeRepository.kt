package com.authentication.auth.viewmodel.repository

import com.authentication.auth.data.model.Change
import com.authentication.auth.data.model.Login
import com.authentication.auth.data.model.Profile
import com.authentication.auth.data.model.Recovery
import com.authentication.auth.data.model.Register
import com.authentication.auth.data.model.Reset
import com.authentication.auth.data.model.User
import com.authentication.auth.data.remote.IAuthApiService
import com.authentication.auth.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class AuthFakeRepository(private val authApiService: IAuthApiService) :
    AuthRepository(authApiService) {

    override fun uploadProfileImage(userName: String, file: File): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.uploadProfileImage(userName, prepareImageFilePart(file)))
    }

    override fun downloadProfileImage(userName: String): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.downloadProfileImage(userName))
    }

    override fun getAllUsers(): Flow<Response<List<User>>> = flow {
        emit(authApiService.getAllUsers())
    }

    override fun getUser(userName: String): Flow<Response<User>> = flow {
        emit(authApiService.getUser(userName))
    }

    override fun register(register: Register): Flow<Response<Register>> = flow {
        emit(authApiService.register(register))
    }

    override fun login(login: Login): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.login(login))
    }

    override fun deleteAll(): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.deleteAll())
    }

    override fun deleteUser(loginInfo: Login): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.deleteUser(loginInfo.userName, loginInfo.password))
    }

    override fun recovery(recovery: Recovery): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.recovery(recovery))
    }

    override fun reset(reset: Reset): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.reset(reset))
    }

    override fun change(change: Change): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.change(change))
    }

    override fun editProfile(profile: Profile): Flow<Response<ResponseBody>> = flow {
        emit(authApiService.editProfile(profile))
    }

    private fun prepareImageFilePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}