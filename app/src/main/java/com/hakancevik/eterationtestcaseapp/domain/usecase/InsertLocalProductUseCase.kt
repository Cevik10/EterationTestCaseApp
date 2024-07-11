package com.hakancevik.eterationtestcaseapp.domain.usecase

import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.domain.repository.ProductLocalRepository
import javax.inject.Inject

class InsertLocalProductUseCase @Inject constructor(
    private val repository: ProductLocalRepository
) {
    suspend operator fun invoke(productEntity: ProductEntity) {
        return repository.insertProduct(productEntity)
    }
}