package com.hakancevik.eterationtestcaseapp.domain.repository

import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.extension.Resource
import kotlinx.coroutines.flow.Flow

interface ProductLocalRepository {
    suspend fun insertProduct(productEntity: ProductEntity)
    suspend fun deleteProduct(id: String): Flow<Resource<Unit>>
    suspend fun getAllProductList(): Flow<Resource<List<ProductEntity>>>
}