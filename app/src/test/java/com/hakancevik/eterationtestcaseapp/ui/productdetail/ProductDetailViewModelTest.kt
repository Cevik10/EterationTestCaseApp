package com.hakancevik.eterationtestcaseapp.ui.productdetail

import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetLocalProductListUseCase
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetProductDetailUseCase
import com.hakancevik.eterationtestcaseapp.domain.usecase.InsertLocalProductUseCase
import com.hakancevik.eterationtestcaseapp.extension.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
class ProductDetailViewModelTest {

    private lateinit var viewModel: ProductDetailViewModel
    private val getProductDetailUseCase: GetProductDetailUseCase = mockk()
    private val insertLocalProductUseCase: InsertLocalProductUseCase = mockk()
    private val getLocalProductUseCase: GetLocalProductListUseCase = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ProductDetailViewModel(
            getProductDetailUseCase,
            insertLocalProductUseCase,
            getLocalProductUseCase
        )
    }

    @Test
    fun `fetch product detail successfully updates live data`() = runTest {
        val productData = ProductData(
            id = "1",
            image = "",
            model = "ModelX",
            name = "Test Product",
            price = "100.0 TL",
            description = "Description here"
        )
        val expectedResource = Resource.success(productData)

        coEvery { getProductDetailUseCase("1") } returns flow {
            emit(Resource.loading(null))
            delay(10)
            emit(expectedResource)
        }

        viewModel.fetchProductDetail("1")
        advanceUntilIdle()

        assertEquals(expectedResource, viewModel.productDetail.value)
    }

    @Test
    fun `fetch local product data successfully updates live data`() = runTest {
        val productEntityList = listOf(ProductEntity("1", 1, true))
        val expectedResource = Resource.success(productEntityList)

        coEvery { getLocalProductUseCase() } returns flow {
            emit(Resource.loading(null))
            delay(10)
            emit(expectedResource)
        }
        viewModel.fetchLocalProductData()
        advanceUntilIdle()

        assertEquals(expectedResource, viewModel.localProductDetail.value)
    }


    @Test
    fun `insert local product calls use case correctly`() = runTest {
        val productEntity = ProductEntity("1", 1, true)

        coEvery { insertLocalProductUseCase.invoke(match { it.id == "1" && it.count == 1 && it.isFavorite == true }) } returns Unit

        viewModel.insertLocalProduct(productEntity)

        delay(50)

        coVerify(exactly = 1) { insertLocalProductUseCase.invoke(productEntity) }
    }
}