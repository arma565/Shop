package com.store.shop

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.store.shop.data.local.ShopDao
import com.store.shop.data.local.ShopDatabase
import com.store.shop.data.model.Product
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalShopTest {
    companion object {
        private lateinit var database: ShopDatabase
        private lateinit var dao: ShopDao

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            val context = InstrumentationRegistry.getInstrumentation().context
            database = Room.inMemoryDatabaseBuilder(context, ShopDatabase::class.java).build()
            dao = database.dao()
        }
    }

    /**
     * Insert product test
     * Parameter: Product Value: Valid
     * Result: Successfully create product in db
     */
    @Test
    fun testInsertProduct_Product_ProductInsertedSuccessfully() {
        val completableDeferred = CompletableDeferred<Boolean>()
        try {
            CoroutineScope(IO).launch {
                //Arrange
                database.clearAllTables()
                val product = Product(
                    200,
                    "100",
                    "240",
                    "Cellphone",
                    "Samsung A50",
                    "Samsung",
                    "Yes",
                    "20",
                    "",
                    "",
                    "Yes",
                    "No",
                    "4.5",
                    "4544555544",
                    "",
                    listOf()
                )
                //Act
                dao.upsertProduct(product)
                completableDeferred.complete(dao.productList().isNotEmpty())
            }
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }

    /**
     * Update Product test
     * Parameter: Product Value: Valid
     * Result: Successfully update product in db
     */
    @Test
    fun testUpdateProduct_Product_ProductUpdateSuccessfully() {
        val completableDeferred = CompletableDeferred<Boolean>()
        try {
            CoroutineScope(IO).launch {
                //Arrange
                database.clearAllTables()
                val product = Product(
                    200,
                    "100",
                    "240",
                    "Cellphone",
                    "Samsung A50",
                    "Samsung",
                    "Yes",
                    "20",
                    "",
                    "",
                    "Yes",
                    "No",
                    "4.5",
                    "4544555544",
                    "",
                    listOf()
                )
                dao.upsertProduct(product)
                dao.upsertProduct(
                    Product(
                        200,
                        "100",
                        "240",
                        "Cellphone",
                        "Iphone 7",
                        "Iphone",
                        "Yes",
                        "50",
                        "",
                        "",
                        "No",
                        "Yes",
                        "4",
                        "4544555544",
                        "",
                        listOf()
                    )
                )
                //Act
                completableDeferred.complete(
                    dao.productList().first().title == "Iphone 7" && dao.productList()
                        .first().count == "50"
                )
            }
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }

    /**
     * Get Product list test
     * Parameter: No parameter Value: No parameter
     * Result: Successfully get product list from db
     */
    @Test
    fun testGetProductList_NoParameter_GetProductListSuccessfully() {
        val completableDeferred = CompletableDeferred<Boolean>()
        try {
            CoroutineScope(IO).launch {
                //Arrange
                database.clearAllTables()
                val product = Product(
                    200,
                    "100",
                    "240",
                    "Cellphone",
                    "Samsung A50",
                    "Samsung",
                    "Yes",
                    "20",
                    "",
                    "",
                    "Yes",
                    "No",
                    "4.5",
                    "4544555544",
                    "",
                    listOf()
                )
                dao.upsertProduct(product)
                //Act
                completableDeferred.complete(dao.productList().isNotEmpty())
            }
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }

    /**
     * Delete Product test
     * Parameter: Product Value: Valid Product id
     * Result: Successfully delete product
     */
    @Test
    fun testDeleteProduct_ValidProduct_SuccessfullyDeleteProduct() {
        val completableDeferred = CompletableDeferred<Boolean>()
        try {
            CoroutineScope(IO).launch {
                //Arrange
                database.clearAllTables()
                val product = Product(
                    200,
                    "100",
                    "240",
                    "Cellphone",
                    "Samsung A50",
                    "Samsung",
                    "Yes",
                    "20",
                    "",
                    "",
                    "Yes",
                    "No",
                    "4.5",
                    "4544555544",
                    "",
                    listOf()
                )
                dao.upsertProduct(product)
                dao.deleteProduct(product.productId)
                //Act
                completableDeferred.complete(dao.productList().isEmpty())
            }
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
        runBlocking {
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }
    //</editor-fold>

}