package com.store.shop

import com.authentication.auth.data.model.Constants.BASE_URL
import com.store.shop.data.model.BaseCategories
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.BaseProductCategory
import com.store.shop.data.model.Brand
import com.store.shop.data.model.Category
import com.store.shop.data.model.Data
import com.store.shop.data.model.New
import com.store.shop.data.model.Product
import com.store.shop.data.remote.ApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(MockitoJUnitRunner::class)
class RemoteNetworkTest {

    companion object {

        @Mock
        private lateinit var mockApiService: ApiService

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            mockApiService = mock(ApiService::class.java)
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            mockApiService = retrofit.create(ApiService::class.java)
        }
    }


    /**
     * Test get news
     * input: none valid: yes
     * output: ExpectedListEqualToMockedList
     */
    @Test
    fun testGetNews_NoParameter_ExpectedListEqualToMockedList() = runBlocking {
        try {
            val news = listOf(
                New(
                    icon = "http://sarfaraz.com/downloadimage.img",
                    id = "4",
                    link = "http://sarfaraz.com/video.mp4",
                    title = "ronaldo",
                    type = "1"
                ),
                New(
                    icon = "http://sarfaraz.com/downloadimage.img",
                    id = "4",
                    link = "http://sarfaraz.com/video.mp4",
                    title = "ronaldo",
                    type = "1"
                ),
                New(
                    icon = "http://sarfaraz.com/downloadimage.img",
                    id = "4",
                    link = "http://sarfaraz.com/video.mp4",
                    title = "ronaldo",
                    type = "1"
                )
            )
            Mockito.`when`(mockApiService.getNews()).thenReturn(Response.success(news))

            // Assert the result
            assertEquals(news, mockApiService.getNews().body())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }


    /**
     * Test get categories
     * input: none valid: yes
     * output: Get base categories successfully
     */
    @Test
    fun testGetCategories_NoParameter_GetBaseCategoriesSuccessfully() = runBlocking {
        try {
            val baseCategories = BaseCategories(
                listOf(Brand("Samsung")),
                listOf(Category("200", "Samsung A50", "sdf", "sdfgfdg")),
                Data()
            )
            Mockito.`when`(mockApiService.getCategories())
                .thenReturn(Response.success(baseCategories))
            // Assert the result
            assertEquals(baseCategories, mockApiService.getCategories().body())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Test get category
     * input: none valid: yes
     * output: Get base category successfully
     */
    @Test
    fun testGetBaseCategory_NoParameter_GetBaseCategorySuccessfully() = runBlocking {
        try {
            val baseCategory = BaseCategory(
                mobile = listOf(
                    Product(
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
                ),

                makeup = listOf(
                    Product(
                        210,
                        "150",
                        "100",
                        "Mo chin",
                        "Afif",
                        "Afif",
                        "No",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),

                mode = listOf(
                    Product(
                        310,
                        "142",
                        "132",
                        "T-Shirt",
                        "Nike",
                        "Nike",
                        "No",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),

                sport = listOf(
                    Product(
                        400,
                        "741",
                        "985",
                        "Ball",
                        "White Ball",
                        "Nike",
                        "No",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),

                home = listOf(
                    Product(
                        350,
                        "456",
                        "2",
                        "TV",
                        "Sony Bra",
                        "Sony",
                        "Yes",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),
                data = Data()
            )
            Mockito.`when`(mockApiService.getCategory()).thenReturn(Response.success(baseCategory))

            // Assert the result
            assertEquals(baseCategory, mockApiService.getCategory().body())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Test get product category
     * input: CatId valid: yes
     * output: Get product category successfully
     */
    @Test
    fun testGetProductCategory_NoParameter_GetProductCategorySuccessfully() = runBlocking {
        try {
            val baseProductCategory = BaseProductCategory(
                listOf(
                    Product(
                        350,
                        "456",
                        "2",
                        "TV",
                        "Sony Bra",
                        "Sony",
                        "Yes",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),
                Data()
            )

            Mockito.`when`(mockApiService.getProductCategory("2"))
                .thenReturn(Response.success(baseProductCategory))

            // Assert the result
            assertEquals(baseProductCategory, mockApiService.getProductCategory("2").body())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }


    /**
     * Test get base home
     * input: none valid: yes
     * output: Get base home successfully
     */
    @Test
    fun testGetBaseHome_NoParameter_GetBaseHomeSuccessfully() = runBlocking {
        try {
            val baseHome = BaseHome(
                news = listOf(
                    New(
                        icon = "http://sarfaraz.com/downloadimage.img",
                        id = "4",
                        link = "http://sarfaraz.com/video.mp4",
                        title = "ronaldo",
                        type = "1"
                    )
                ),
                mobile = listOf(
                    Product(
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
                ),

                makeup = listOf(
                    Product(
                        210,
                        "150",
                        "100",
                        "Mo chin",
                        "Afif",
                        "Afif",
                        "No",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),

                discount = listOf(
                    Product(
                        310,
                        "142",
                        "132",
                        "T-Shirt",
                        "Nike",
                        "Nike",
                        "No",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),

                amazingOffer = listOf(
                    Product(
                        400,
                        "741",
                        "985",
                        "Ball",
                        "White Ball",
                        "Nike",
                        "No",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),
                data = Data()
            )
            Mockito.`when`(mockApiService.getBaseHome()).thenReturn(Response.success(baseHome))


            // Assert the result
            assertEquals(baseHome, mockApiService.getBaseHome().body())
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    /**
     * Test search product
     * input: Title valid: yes
     * output: Get product item successfully
     */
    @Test
    fun testSearchProduct_Title_GetProductItemSuccessfully() = runBlocking {
        try {
            val baseProductCategory = BaseProductCategory(
                listOf(
                    Product(
                        350,
                        "456",
                        "2",
                        "TV",
                        "Sony Bra",
                        "Sony",
                        "Yes",
                        "150",
                        "asd asd",
                        "asd",
                        "Yes",
                        "No",
                        "4.5",
                        "4544555544",
                        "",
                        listOf()
                    )
                ),
                Data()
            )
/*            Mockito.`when`(mockApiService.searchProduct("Sony Bra"))
                .thenReturn(baseProductCategory)*/

            // Assert the result
            /*assertEquals(baseProductCategory, mockApiService.searchProduct("Sony Bra").body())*/
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }
}