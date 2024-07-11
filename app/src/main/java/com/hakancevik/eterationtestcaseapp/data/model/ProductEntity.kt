package com.hakancevik.eterationtestcaseapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val count: Int,
    val isFavorite: Boolean,
)
