package com.hakancevik.eterationtestcaseapp.data.model

import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData

data class ProductDTO(
    val brand: String,
    val createdAt: String,
    val description: String,
    val id: String,
    val image: String,
    val model: String,
    val name: String,
    val price: String
)

fun ProductDTO.toProductData(): ProductData {
    return ProductData(
        id = id,
        image = image,
        model = model,
        name = name,
        price = price,
        description = description
    )
}