package com.store.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.shop.data.model.Product
import com.store.shop.data.repository.LocalShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalShopViewModel @Inject constructor(
    private val localRepository: LocalShopRepository
) : ViewModel() {

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
    fun productList(): Flow<List<Product>> = flow {
        localRepository.productList().collect {
            this.emit(it)
            delay(1000L)
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