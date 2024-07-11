package com.hakancevik.eterationtestcaseapp.ui.cart

import com.hakancevik.eterationtestcaseapp.domain.usecase.GetLocalProductListUseCase
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetProductsUseCase
import com.hakancevik.eterationtestcaseapp.domain.usecase.InsertLocalProductUseCase
import com.hakancevik.eterationtestcaseapp.extension.Resource
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private val getCartProductListUseCase: GetLocalProductListUseCase = mockk()
    private val getProductsUseCase: GetProductsUseCase = mockk()
    private val updateProductCountUseCase: InsertLocalProductUseCase = mockk()


    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = CartViewModel(
            getCartProductListUseCase,
            getProductsUseCase,
            updateProductCountUseCase
        )
    }

    @Test
    fun `fetch product from local successfully updates live data`() = runTest {
        val productEntityList = listOf(ProductEntity("1", 1, true))
        val expectedEntityResource = Resource.success(productEntityList)
        val productList = listOf(ProductData("1", "", "ModelX", "Test Product", "100.0 TL", "Description"))
        val expectedProductResource = Resource.success(productList)

        coEvery { getCartProductListUseCase() } returns flow {
            emit(Resource.loading(null))
            delay(10)
            emit(expectedEntityResource)
        }

        coEvery { getProductsUseCase() } returns flow {
            emit(Resource.loading(null))
            delay(10)
            emit(expectedProductResource)
        }

        viewModel.fetchProductFromLocal()
        advanceUntilIdle()

        val expectedCartItems = listOf(CartItem(productList[0], productEntityList[0]))
        val expectedCartResource = Resource.success(expectedCartItems)

        assertEquals(expectedCartResource, viewModel.products.value)
    }

    @Test
    fun `increase product count successfully updates live data`() = runTest {
        val productEntity = ProductEntity("1", 1, true)
        val updatedEntity = productEntity.copy(count = 2)

        coEvery { getCartProductListUseCase() } returns flow {
            emit(Resource.success(listOf(productEntity)))
        }

        val productData = ProductData("1", "", "ModelX", "Test Product", "100.0 TL", "Description")
        coEvery { getProductsUseCase() } returns flow {
            emit(Resource.success(listOf(productData)))
        }

        coEvery { updateProductCountUseCase.invoke(updatedEntity) } returns Unit

        val cartItem = CartItem(productData, productEntity)

        viewModel.increaseProductCount(cartItem)
        advanceUntilIdle()

        coVerify(exactly = 1) { updateProductCountUseCase.invoke(updatedEntity) }
    }


    @Test
    fun `decrease product count successfully updates live data`() = runTest {
        val productEntity = ProductEntity("1", 2, true)
        val updatedEntity = productEntity.copy(count = 1)

        coEvery { getCartProductListUseCase() } returns flow {
            emit(Resource.success(listOf(productEntity)))
        }

        val productData = ProductData("1", "", "ModelX", "Test Product", "100.0 TL", "Description")
        coEvery { getProductsUseCase() } returns flow {
            emit(Resource.success(listOf(productData)))
        }

        coEvery { updateProductCountUseCase.invoke(updatedEntity) } returns Unit

        val cartItem = CartItem(productData, productEntity)

        viewModel.decreaseProductCount(cartItem)
        advanceUntilIdle()

        coVerify(exactly = 1) { updateProductCountUseCase.invoke(updatedEntity) }
    }
}