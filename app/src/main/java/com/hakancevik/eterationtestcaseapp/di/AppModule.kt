package com.hakancevik.eterationtestcaseapp.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hakancevik.eterationtestcaseapp.R
import com.hakancevik.eterationtestcaseapp.data.datasource.local.AppDatabase
import com.hakancevik.eterationtestcaseapp.ui.cart.CartAdapter
import com.hakancevik.eterationtestcaseapp.ui.favorite.FavoriteAdapter
import com.hakancevik.eterationtestcaseapp.ui.home.ProductAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGlide(@ApplicationContext context: Context): RequestManager {

        return Glide.with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_cart_selector)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_cart_selector)
        )

    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, AppDatabase::class.java, "EterationAppDatabase"
        ).build()

    @Singleton
    @Provides
    fun provideProductAdapter(glide: RequestManager): ProductAdapter = ProductAdapter(glide)


    @Singleton
    @Provides
    fun provideCartAdapter(): CartAdapter = CartAdapter()

    @Singleton
    @Provides
    fun provideFavoriteAdapter(glide: RequestManager): FavoriteAdapter = FavoriteAdapter(glide)

    @Singleton
    @Provides
    fun provideDao(database: AppDatabase) = database.productDao()

}