package com.store.shop.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.shop.data.model.BaseCategories
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.BaseProductCategory
import com.store.shop.data.model.Data
import com.store.shop.data.model.New
import com.store.shop.data.model.Product
import com.store.shop.data.repository.RemoteShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
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

    var product by mutableStateOf<Product?>(null)
        private set
    private var _getNewsStateFlow: MutableStateFlow<List<New>> = MutableStateFlow(mutableListOf())
    var getNewList = _getNewsStateFlow.asStateFlow()
    private var _getBaseHomeMutableLiveData: MutableLiveData<BaseHome> = MutableLiveData(BaseHome())
    var getBaseHome = _getBaseHomeMutableLiveData
    private var _getBaseCategoryMutableLiveData: MutableLiveData<BaseCategory> =
        MutableLiveData(BaseCategory())
    var getBaseCategory = _getBaseCategoryMutableLiveData
    private var _getBaseCategoriesMutableLiveData: MutableLiveData<BaseCategories> =
        MutableLiveData(BaseCategories())
    var getBaseCategories = _getBaseCategoriesMutableLiveData
    private var _getProductCategoryFlow: MutableStateFlow<List<Product>> =
        MutableStateFlow(mutableListOf())
    var getProductCategory = _getProductCategoryFlow.asStateFlow()

    init {
        prepareNews()
        prepareBaseHome()
        prepareBaseCategory()
        prepareBaseCategories()
    }

    /**
     * Get news
     */
    private fun prepareNews() {
        viewModelScope.launch(IO) {
            remoteShopRepository.getNews().collect {
                _getNewsStateFlow.emit(it.body()!!)
                delay(1000)
            }
        }
    }

    /**
     * Get home
     */
    private fun prepareBaseHome() {
        viewModelScope.launch(IO) {
            _getBaseHomeMutableLiveData.postValue(remoteShopRepository.getBaseHome().body())
        }
    }

    /**
     * Get category
     */
    private fun prepareBaseCategory() {
        viewModelScope.launch(IO) {
            _getBaseCategoryMutableLiveData.postValue(remoteShopRepository.getBaseCategory().body())
        }
    }


    /**
     * Get categories
     */
    private fun prepareBaseCategories() {
        viewModelScope.launch(IO) {
            _getBaseCategoriesMutableLiveData.postValue(remoteShopRepository.getCategories().body())
        }
    }

    /**
     * Get product list
     */
    fun getProductCategory(catId: String) {
        viewModelScope.launch(IO) {
            remoteShopRepository.getProductCategory(catId).collect {
                _getProductCategoryFlow.emit(it.body()?.products!!)
                delay(1000)
            }
        }
    }

    /**
     * Search products
     */
    fun searchProduct(title: String): LiveData<BaseProductCategory> {
        val searchProductMutableLiveData = MutableLiveData<BaseProductCategory>()
        remoteShopRepository.searchProduct(title).enqueue(object : Callback<BaseProductCategory> {
            override fun onResponse(
                call: Call<BaseProductCategory>,
                response: Response<BaseProductCategory>
            ) {
                searchProductMutableLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<BaseProductCategory>, exception: Throwable) {
                searchProductMutableLiveData.postValue(
                    BaseProductCategory(
                        emptyList(),
                        Data(state = exception.hashCode())
                    )
                )
            }

        })
        return searchProductMutableLiveData
    }

    fun addProduct(newProduct: Product) {
        product = newProduct
    }
}