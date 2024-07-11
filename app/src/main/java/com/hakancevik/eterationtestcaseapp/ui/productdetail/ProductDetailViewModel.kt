package com.hakancevik.eterationtestcaseapp.ui.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.domain.usecase.GetProductDetailUseCase
import com.hakancevik.eterationtestcaseapp.extension.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,

) : ViewModel() {

    private val _productDetail = MutableStateFlow<Resource<ProductData>>(Resource.loading(null))
    val productDetail: StateFlow<Resource<ProductData>> get() = _productDetail


    fun fetchProductDetail(id: String) {
        viewModelScope.launch {
            getProductDetailUseCase(id).collect {
                _productDetail.value = it
            }
        }
    }


}
