package com.hakancevik.eterationtestcaseapp.domain.usecase

import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.domain.repository.ProductRepository
import com.hakancevik.eterationtestcaseapp.extension.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: String): Flow<Resource<ProductData>> {
        return repository.getProductDetail(id)
    }
}