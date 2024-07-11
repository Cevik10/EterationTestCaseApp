package com.hakancevik.eterationtestcaseapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetLocalProductListUseCase
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetProductsUseCase
import com.hakancevik.eterationtestcaseapp.domain.usecase.InsertLocalProductUseCase
import com.hakancevik.eterationtestcaseapp.extension.Resource
import com.hakancevik.eterationtestcaseapp.extension.Status
import com.hakancevik.eterationtestcaseapp.ui.cart.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getCartProductListUseCase: GetLocalProductListUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val updateProductFavoriteStatusUseCase: InsertLocalProductUseCase
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<CartItem>>>(Resource.loading(null))
    val products: StateFlow<Resource<List<CartItem>>> get() = _products

    private val _productInFavorite = MutableStateFlow<List<ProductEntity>>(arrayListOf())
    val productInFavorite: StateFlow<List<ProductEntity>> get() = _productInFavorite

    fun fetchProductFromLocal() {
        viewModelScope.launch {
            getCartProductListUseCase.invoke().collect { entityList ->
                if (!entityList.data.isNullOrEmpty()) {
                    _productInFavorite.value = entityList.data.filter { productEntity ->
                        productEntity.isFavorite
                    }
                    fetchAllProducts()
                } else {
                    // Empty cart list
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
                            val cartProductIds = _productInFavorite.value.map { it.id }.toSet()
                            val filteredProducts = productList.filter { it.id in cartProductIds }

                            val cartItems = filteredProducts.map { productData ->
                                CartItem(
                                    productData = productData,
                                    productEntity = _productInFavorite.value.first { it.id == productData.id }
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

    fun updateFavoriteStatus(cartItem: CartItem) {
        viewModelScope.launch {
            val updatedEntity = cartItem.productEntity.copy(isFavorite = !cartItem.productEntity.isFavorite)
            updateProductFavoriteStatusUseCase(updatedEntity)
            fetchProductFromLocal()
        }
    }
}