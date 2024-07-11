package com.hakancevik.eterationtestcaseapp.ui.home

import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test


class HomeFragmentKtTest() {
    // Sample data for products
    private val productDataList = listOf(
        ProductData("1", "image1.jpg", "ModelX", "Product 1", "100", "Desc 1"),
        ProductData("2", "image2.jpg", "ModelY", "Product 2", "200", "Desc 2"),
        ProductData("3", "image3.jpg", "ModelZ", "Product 3", "300", "Desc 3")
    )

    // Sample data for entities
    private val productEntityList = listOf(
        ProductEntity("1", 10, false),
        ProductEntity("2", 0, true),
        ProductEntity("3", 5, true)
    )

    @Test
    fun `filterProductData returns only products with positive count in localProductList`() {
        // Execute
        val filteredProducts = filterProductData(productDataList, productEntityList)

        // Verify
        assertEquals(2, filteredProducts.size)
        assertTrue(filteredProducts.any { it.id == "1" })
        assertTrue(filteredProducts.any { it.id == "3" })
        assertFalse(filteredProducts.any { it.id == "2" }) // Product 2 should be excluded due to zero count
    }

    @Test
    fun `filterIsFavoriteProductData returns only favorite products in localProductList`() {
        // Execute
        val filteredProducts = filterIsFavoriteProductData(productDataList, productEntityList)

        // Verify
        assertEquals(2, filteredProducts.size)
        assertTrue(filteredProducts.any { it.id == "2" }) // Included despite zero count because it's a favorite
        assertTrue(filteredProducts.any { it.id == "3" })
        assertFalse(filteredProducts.any { it.id == "1" }) // Not a favorite
    }
}