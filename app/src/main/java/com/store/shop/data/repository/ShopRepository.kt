package com.store.shop.data.repository

import androidx.lifecycle.LiveData
import com.store.shop.data.model.BaseCategories
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.BaseProductCategory
import com.store.shop.data.model.New
import com.store.shop.data.remote.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getNews(): Flow<Response<List<New>>> = flow {
        this.emit(apiService.getNews())
        delay(1000L)
    }

    suspend fun getCategories(): Response<BaseCategories> = apiService.getCategories()

    suspend fun getCategory(): Response<BaseCategory> = apiService.getCategory()

    suspend fun getProductCategory(catId: String): Response<BaseProductCategory> = apiService.getProductCategory(catId)

    suspend fun getBaseHome(): Response<BaseHome> = apiService.getBaseHome()



}