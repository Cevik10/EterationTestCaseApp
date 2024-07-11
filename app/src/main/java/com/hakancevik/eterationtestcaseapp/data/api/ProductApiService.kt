package com.hakancevik.eterationtestcaseapp.data.api

import com.hakancevik.eterationtestcaseapp.data.model.ProductDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    @GET("products")
    suspend fun getAllProducts(): List<ProductDTO>

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: String): ProductDTO

}
