package com.hakancevik.eterationtestcaseapp.domain.repository

import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.extension.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(): Flow<Resource<List<ProductData>>>
    suspend fun getProductDetail(id: String): Flow<Resource<ProductData>>
}