package com.hakancevik.eterationtestcaseapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: ProductEntity)

    @Query("SELECT * FROM products")
    suspend fun getAllProductList(): List<ProductEntity>?

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProductById(productId: String)
}