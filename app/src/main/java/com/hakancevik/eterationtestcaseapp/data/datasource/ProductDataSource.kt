package com.hakancevik.eterationtestcaseapp.data.datasource

import com.hakancevik.eterationtestcaseapp.data.api.ProductApiService
import com.hakancevik.eterationtestcaseapp.data.model.toProductData
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.domain.repository.ProductRepository
import com.hakancevik.eterationtestcaseapp.extension.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductDataSource @Inject constructor(
    private val apiService: ProductApiService
) : ProductRepository {

    override suspend fun getAllProducts(): Flow<Resource<List<ProductData>>> = flow {
        emit(Resource.loading(null))
        try {
            val products = apiService.getAllProducts().map { it.toProductData() }
            emit(Resource.success(products))
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Unknown error", null))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProductDetail(id: String): Flow<Resource<ProductData>> = flow {
        emit(Resource.loading(null))
        try {
            val product = apiService.getProductDetail(id).toProductData()
            emit(Resource.success(product))
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Unknown error", null))
        }
    }.flowOn(Dispatchers.IO)

}