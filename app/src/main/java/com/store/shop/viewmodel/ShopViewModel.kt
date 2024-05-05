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
import com.store.shop.data.repository.ShopRepository
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
class ShopViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {
    private var getNewsFlow = MutableStateFlow<List<New>>(mutableListOf())
    private lateinit var getCategoriesLiveData: MutableLiveData<BaseCategories>
    private lateinit var getCategoryLiveData: MutableLiveData<BaseCategory>
    private lateinit var getProductCategoryLiveData: MutableLiveData<BaseProductCategory>
    private lateinit var getBaseHomeLiveData: MutableLiveData<BaseHome>
    private lateinit var loginLiveData: MutableLiveData<String>
    private lateinit var registerLiveData: MutableLiveData<Int>


    /**
     * Get news
     */
    fun getNews(): StateFlow<List<New>> {
        viewModelScope.launch(IO) {
            shopRepository.getNews().collect {
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
        viewModelScope.launch(IO) {
            getCategoriesLiveData.postValue(shopRepository.getCategories().body())
        }
        return getCategoriesLiveData
    }


    /**
     * Get category
     */
    fun getCategory(): LiveData<BaseCategory> {
        getCategoryLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            getCategoryLiveData.postValue(shopRepository.getCategory().body())
        }
        return getCategoryLiveData
    }


    /**
     * Get product category
     */
    fun getProductCategory(catId: String): LiveData<BaseProductCategory> {
        getProductCategoryLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            getProductCategoryLiveData.postValue(shopRepository.getProductCategory(catId).body())
        }
        return getProductCategoryLiveData
    }


    /**
     * Get home
     */
    fun getBaseHome(): LiveData<BaseHome> {
        getBaseHomeLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            getBaseHomeLiveData.postValue(shopRepository.getBaseHome().body())
        }
        return getBaseHomeLiveData
    }


    /**
     * login
     */
    fun login(username: String, password: String): LiveData<String> {
        loginLiveData = MutableLiveData()
        viewModelScope.launch {
            shopRepository.login(username, password).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    loginLiveData.postValue(response.body())
                }

                override fun onFailure(p0: Call<String>, p1: Throwable) {}

            })
        }
        return loginLiveData
    }

    /**
     * register
     */
    fun register(username: String, password: String): LiveData<Int> {
        registerLiveData = MutableLiveData()
        viewModelScope.launch {
            shopRepository.register(username, password).enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    registerLiveData.postValue(response.body())
                }

                override fun onFailure(p0: Call<Int>, p1: Throwable) {}

            })
        }
        return registerLiveData
    }
}