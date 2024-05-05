package com.store.shop.data.remote

import com.store.shop.data.model.BaseCategories
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.BaseProductCategory
import com.store.shop.data.model.New
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("getNews.php")
    suspend fun getNews(): Response<List<New>>

    @GET("getCategories.php")
    suspend fun getCategories(): Response<BaseCategories>

    @GET("getCategory.php")
    suspend fun getCategory(): Response<BaseCategory>

    @GET("getProductCategory.php")
    suspend fun getProductCategory(@Query("catId") catId: String): Response<BaseProductCategory>

    @GET("home.php")
    suspend fun getBaseHome(): Response<BaseHome>
}