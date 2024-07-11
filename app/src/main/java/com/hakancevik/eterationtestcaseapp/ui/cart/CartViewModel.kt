package com.hakancevik.eterationtestcaseapp.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetLocalProductListUseCase
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetProductsUseCase
import com.hakancevik.eterationtestcaseapp.domain.usecase.InsertLocalProductUseCase
import com.hakancevik.eterationtestcaseapp.extension.Resource
import com.hakancevik.eterationtestcaseapp.extension.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductListUseCase: GetLocalProductListUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val updateProductCountUseCase: InsertLocalProductUseCase
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<CartItem>>>(Resource.loading(null))
    val products: StateFlow<Resource<List<CartItem>>> get() = _products

    private val _productInCart = MutableStateFlow<List<ProductEntity>>(arrayListOf())
    val productInCart: StateFlow<List<ProductEntity>> get() = _productInCart

    fun fetchProductFromLocal() {
        viewModelScope.launch {
            getCartProductListUseCase.invoke().collect { entityList ->
                if (!entityList.data.isNullOrEmpty()) {
                    _productInCart.value = entityList.data.filter { productEntity ->
                        productEntity.count > 0
                    }
                    fetchAllProducts()
                } else {
                    // Empty cart
                }
            }
        }
    }

    private fun fetchAllProducts() {
        viewModelScope.launch {
            getProductsUseCase().collect { productResult ->
                when (productResult.status) {
                    Status.SUCCESS -> {
                        productResult.data?.let { productList ->
                            val cartProductIds = _productInCart.value.map { it.id }.toSet()
                            val filteredProducts = productList.filter { it.id in cartProductIds }

                            val cartItems = filteredProducts.map { productData ->
                                CartItem(
                                    productData = productData,
                                    productEntity = _productInCart.value.first { it.id == productData.id }
                                )
                            }

                            _products.value = Resource.success(cartItems)
                        }
                    }
                    Status.ERROR -> {
                        _products.value = Resource.error(productResult.message ?: "Unknown error", null)
                    }
                    else -> {
                        _products.value = Resource.loading(null)
                    }
                }
            }
        }
    }

    fun increaseProductCount(cartItem: CartItem) {
        viewModelScope.launch {
            val updatedEntity = cartItem.productEntity.copy(count = cartItem.productEntity.count + 1)
            updateProductCountUseCase(updatedEntity)
            fetchProductFromLocal()
        }
    }

    fun decreaseProductCount(cartItem: CartItem) {
        viewModelScope.launch {
            val updatedEntity = cartItem.productEntity.copy(count = cartItem.productEntity.count - 1)
            updateProductCountUseCase(updatedEntity)
            fetchProductFromLocal()
        }
    }
}