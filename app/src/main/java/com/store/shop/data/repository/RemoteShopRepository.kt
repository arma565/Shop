package com.store.shop.data.repository

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

class RemoteShopRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getNews(): Flow<Response<List<New>>> = flow {
        this.emit(apiService.getNews())
        delay(1000L)
    }

    fun getCategories(): Call<BaseCategories> = apiService.getCategories()
    fun getCategory(): Call<BaseCategory> = apiService.getCategory()
    fun getProductCategory(catId: String): Call<BaseProductCategory> =
        apiService.getProductCategory(catId)

    fun getBaseHome(): Call<BaseHome> = apiService.getBaseHome()
    fun search(title: String): Call<BaseProductCategory> = apiService.searchProduct(title)
}