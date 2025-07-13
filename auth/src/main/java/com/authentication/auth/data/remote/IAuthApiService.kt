package com.authentication.auth.data.remote

import com.authentication.auth.data.model.Change
import com.authentication.auth.data.model.Login
import com.authentication.auth.data.model.Profile
import com.authentication.auth.data.model.Recovery
import com.authentication.auth.data.model.Register
import com.authentication.auth.data.model.Reset
import com.authentication.auth.data.model.User
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface IAuthApiService {

    @Multipart
    @POST("Auth/user/upload/{userName}")
    suspend fun uploadProfileImage(
        @Path("userName") userName: String,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("Auth/user/download/{userName}")
    suspend fun downloadProfileImage(@Path("userName") userName: String): Response<ResponseBody>

    @GET("Auth/user/users")
    suspend fun getAllUsers(): Response<List<User>>

    @GET("Auth/user/{userName}")
    suspend fun getUser(@Path("userName") userName: String): Response<User>

    @POST("Auth/user/register")
    suspend fun register(@Body register: Register): Response<Register>

    @POST("Auth/user/login")
    suspend fun login(@Body login: Login): Response<ResponseBody>

    @DELETE("Auth/user/delete-all")
    suspend fun deleteAll(): Response<ResponseBody>

    @DELETE("Auth/user/delete/{userName}/{password}")
    suspend fun deleteUser(
        @Path("userName") userName: String,
        @Path("password") password: String
    ): Response<ResponseBody>

    @POST("Auth/user/recovery/account")
    suspend fun recovery(@Body recovery: Recovery): Response<ResponseBody>

    @POST("Auth/user/reset/password")
    suspend fun reset(@Body reset: Reset): Response<ResponseBody>

    @POST("Auth/user/change/password")
    suspend fun change(@Body change: Change): Response<ResponseBody>

    @PUT("Auth/user/edit/profile")
    suspend fun editProfile(@Body profile: Profile): Response<ResponseBody>
}