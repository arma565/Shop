package com.store.shop.data.remote

import com.store.shop.data.model.BaseCategories
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.BaseProductCategory
import com.store.shop.data.model.New
import com.store.shop.data.model.Product
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("getNews.php")
    suspend fun getNews(): Response<List<New>>

    @GET("getCategories.php")
    fun getCategories(): Call<BaseCategories>

    @GET("getCategory.php")
    fun getCategory(): Call<BaseCategory>

    @GET("getProductCategory.php")
    fun getProductCategory(@Query("catId") catId: String): Call<BaseProductCategory>

    @GET("home.php")
    fun getBaseHome(): Call<BaseHome>

    @GET("search.php")
    fun searchProduct(@Query("title") title: String): Call<BaseProductCategory>
}