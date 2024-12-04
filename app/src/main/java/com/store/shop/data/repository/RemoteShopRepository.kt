package com.store.shop.data.repository

import com.store.shop.data.model.BaseCategories
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.BaseProductCategory
import com.store.shop.data.model.New
import com.store.shop.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RemoteShopRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getNews(): Flow<Response<List<New>>> = flowOf(apiService.getNews())
    suspend fun getBaseHome(): Response<BaseHome> = apiService.getBaseHome()
    suspend fun getBaseCategory(): Response<BaseCategory> = apiService.getCategory()
    suspend fun getCategories(): Response<BaseCategories> = apiService.getCategories()
    suspend fun getProductCategory(catId: String): Flow<Response<BaseProductCategory>> =
        flowOf(apiService.getProductCategory(catId))

    fun searchProduct(title: String): Call<BaseProductCategory> =
        apiService.searchProduct(title)
}