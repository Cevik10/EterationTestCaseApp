package com.hakancevik.eterationtestcaseapp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
