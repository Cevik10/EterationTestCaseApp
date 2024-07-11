package com.hakancevik.eterationtestcaseapp.data.datasource

import com.hakancevik.eterationtestcaseapp.data.datasource.local.ProductDao
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.domain.repository.ProductLocalRepository
import com.hakancevik.eterationtestcaseapp.extension.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(
    private val productDao: ProductDao
) : ProductLocalRepository {

    override suspend fun deleteProduct(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading(null))
        try {
            productDao.deleteProductById(id)
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Unknown error", null))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllProductList(): Flow<Resource<List<ProductEntity>>> = flow {
        emit(Resource.loading(null))
        val productIdList = productDao.getAllProductList()
        emit(Resource.success(productIdList))
    }.flowOn(Dispatchers.IO)

    override suspend fun insertProduct(productEntity: ProductEntity) {
        productDao.upsertProduct(productEntity)
    }
}
