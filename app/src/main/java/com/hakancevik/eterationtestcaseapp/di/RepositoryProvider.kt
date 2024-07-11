package com.hakancevik.eterationtestcaseapp.di

import com.hakancevik.eterationtestcaseapp.data.datasource.ProductDataSource
import com.hakancevik.eterationtestcaseapp.data.datasource.ProductLocalDataSource
import com.hakancevik.eterationtestcaseapp.domain.repository.ProductLocalRepository
import com.hakancevik.eterationtestcaseapp.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryProvider {

    @Binds
    abstract fun bindIProductData(productDataSource: ProductDataSource): ProductRepository

    @Binds
    abstract fun bindILocalProductData(productLocalDataSource: ProductLocalDataSource): ProductLocalRepository

}
