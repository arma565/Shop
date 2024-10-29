package com.store.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.shop.data.model.Product
import com.store.shop.data.repository.LocalShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalShopViewModel @Inject constructor(
    private val localRepository: LocalShopRepository
) : ViewModel() {
    private var _getProductListMutableStateFlow: MutableStateFlow<List<Product>> =
        MutableStateFlow(mutableListOf())
    var productList = _getProductListMutableStateFlow.asStateFlow()

    /**
     * Upsert Method
     * @param product: product model
     * This Method Insert or Update Data
     */
    fun upsertProduct(product: Product) {
        viewModelScope.launch(IO) {
            localRepository.upsertProduct(product)
        }
    }

    /**
     *Product list
     */
    fun getProductList() {
        viewModelScope.launch(IO) {
            localRepository.productList().collect {
                _getProductListMutableStateFlow.emit(it)
                delay(1000)
            }
        }
    }

    /**
     * Delete product Method
     * @param productId: Product model productId
     * This Method Delete Data from Database
     */
    fun deleteProduct(productId: Int) {
        viewModelScope.launch(IO) {
            localRepository.deleteProduct(productId)
        }

    }

    /**
     * Delete all products
     */
    fun deleteAllProducts() {
        viewModelScope.launch(IO) {
            localRepository.deleteAllProducts()
        }
    }
}