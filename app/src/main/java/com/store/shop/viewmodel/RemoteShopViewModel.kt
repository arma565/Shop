package com.store.shop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.shop.data.model.BaseCategories
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.BaseProductCategory
import com.store.shop.data.model.New
import com.store.shop.data.repository.RemoteShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RemoteShopViewModel @Inject constructor(
    private val remoteShopRepository: RemoteShopRepository
) : ViewModel() {
    private var getNewsFlow = MutableStateFlow<List<New>>(mutableListOf())
    private lateinit var getCategoriesLiveData: MutableLiveData<BaseCategories>
    private lateinit var getCategoryLiveData: MutableLiveData<BaseCategory>
    private lateinit var getProductCategoryLiveData: MutableLiveData<BaseProductCategory>
    private lateinit var getBaseHomeLiveData: MutableLiveData<BaseHome>
    private lateinit var searchProductLiveData: MutableLiveData<BaseProductCategory>

    /**
     * Get news
     */
    fun getNews(): StateFlow<List<New>> {
        viewModelScope.launch(IO) {
            remoteShopRepository.getNews().collect {
                getNewsFlow.emit(it.body()!!)
            }
        }
        return getNewsFlow.asStateFlow()
    }

    /**
     * Get categories
     */
    fun getCategories(): LiveData<BaseCategories> {
        getCategoriesLiveData = MutableLiveData()
        remoteShopRepository.getCategories().enqueue(object : Callback<BaseCategories> {
            override fun onResponse(
                call: Call<BaseCategories>,
                response: Response<BaseCategories>
            ) {
                getCategoriesLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<BaseCategories>, p1: Throwable) {
                getCategoriesLiveData.postValue(BaseCategories())
            }

        })

        return getCategoriesLiveData
    }


    /**
     * Get category
     */
    fun getCategory(): LiveData<BaseCategory> {
        getCategoryLiveData = MutableLiveData()
        remoteShopRepository.getCategory().enqueue(object : Callback<BaseCategory> {
            override fun onResponse(call: Call<BaseCategory>, response: Response<BaseCategory>) {
                getCategoryLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<BaseCategory>, p1: Throwable) {
                getCategoryLiveData.postValue(BaseCategory())
            }

        })
        return getCategoryLiveData
    }


    /**
     * Get product category
     */
    fun getProductCategory(catId: String): LiveData<BaseProductCategory> {
        getProductCategoryLiveData = MutableLiveData()
        remoteShopRepository.getProductCategory(catId)
            .enqueue(object : Callback<BaseProductCategory> {
                override fun onResponse(
                    call: Call<BaseProductCategory>,
                    response: Response<BaseProductCategory>
                ) {
                    getProductCategoryLiveData.postValue(response.body())
                }

                override fun onFailure(call: Call<BaseProductCategory>, p1: Throwable) {
                    getProductCategoryLiveData.postValue(BaseProductCategory())
                }

            })
        return getProductCategoryLiveData
    }


    /**
     * Get home
     */
    fun getBaseHome(): LiveData<BaseHome> {
        getBaseHomeLiveData = MutableLiveData()
        remoteShopRepository.getBaseHome().enqueue(object : Callback<BaseHome> {
            override fun onResponse(call: Call<BaseHome>, response: Response<BaseHome>) {
                getBaseHomeLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<BaseHome>, p1: Throwable) {
                getBaseHomeLiveData.postValue(BaseHome())
            }

        })
        return getBaseHomeLiveData
    }


    /**
     * Search products
     */
    fun searchProduct(title: String): LiveData<BaseProductCategory> {
        searchProductLiveData = MutableLiveData()
        remoteShopRepository.search(title).enqueue(object : Callback<BaseProductCategory> {
            override fun onResponse(
                call: Call<BaseProductCategory>,
                response: Response<BaseProductCategory>
            ) {
                searchProductLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<BaseProductCategory>, p1: Throwable) {
                searchProductLiveData.postValue(BaseProductCategory())
            }

        })
        return searchProductLiveData
    }

}