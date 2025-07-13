package com.store.shop.data.repository

import com.store.shop.data.model.BaseCategories
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.BaseProductCategory
import com.store.shop.data.model.New
import com.store.shop.data.remote.ShopApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RemoteShopRepository @Inject constructor(
    private val shopApiService: ShopApiService
) {
    suspend fun getNews(): Flow<Response<List<New>>> = flowOf(shopApiService.getNews())
    suspend fun getBaseHome(): Response<BaseHome> = shopApiService.getBaseHome()
    suspend fun getBaseCategory(): Response<BaseCategory> = shopApiService.getCategory()
    suspend fun getCategories(): Response<BaseCategories> = shopApiService.getCategories()
    suspend fun getProductCategory(catId: String): Flow<Response<BaseProductCategory>> =
        flowOf(shopApiService.getProductCategory(catId))

    fun searchProduct(title: String): Call<BaseProductCategory> =
        shopApiService.searchProduct(title)
}