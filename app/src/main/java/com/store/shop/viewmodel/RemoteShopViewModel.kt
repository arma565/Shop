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
        viewModelScope.launch(IO) {
            getCategoriesLiveData.postValue(remoteShopRepository.getCategories().body())
        }
        return getCategoriesLiveData
    }


    /**
     * Get category
     */
    fun getCategory(): LiveData<BaseCategory> {
        getCategoryLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            getCategoryLiveData.postValue(remoteShopRepository.getCategory().body())
        }
        return getCategoryLiveData
    }


    /**
     * Get product category
     */
    fun getProductCategory(catId: String): LiveData<BaseProductCategory> {
        getProductCategoryLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            getProductCategoryLiveData.postValue(
                remoteShopRepository.getProductCategory(catId).body()
            )
        }
        return getProductCategoryLiveData
    }


    /**
     * Get home
     */
    fun getBaseHome(): LiveData<BaseHome> {
        getBaseHomeLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            getBaseHomeLiveData.postValue(remoteShopRepository.getBaseHome().body())
        }
        return getBaseHomeLiveData
    }


    /**
     * Search products
     */
    fun searchProduct(title: String): LiveData<BaseProductCategory> {
        searchProductLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            searchProductLiveData.postValue(remoteShopRepository.search(title).body())
        }
        return searchProductLiveData
    }

}