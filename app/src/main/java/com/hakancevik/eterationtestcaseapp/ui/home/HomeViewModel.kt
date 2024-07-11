package com.hakancevik.eterationtestcaseapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetProductsUseCase
import com.hakancevik.eterationtestcaseapp.extension.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,

) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<ProductData>>>(Resource.loading(null))
    val products: StateFlow<Resource<List<ProductData>>> get() = _products



    fun fetchAllProducts() {
        viewModelScope.launch {
            getProductsUseCase().collect {
                _products.value = it
            }
        }
    }


}