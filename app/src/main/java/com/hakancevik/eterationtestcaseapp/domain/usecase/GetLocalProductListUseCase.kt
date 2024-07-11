package com.hakancevik.eterationtestcaseapp.domain.usecase

import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.domain.repository.ProductLocalRepository
import com.hakancevik.eterationtestcaseapp.extension.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalProductListUseCase @Inject constructor(
    private val repository: ProductLocalRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<ProductEntity>>> {
        return repository.getAllProductList()
    }
}