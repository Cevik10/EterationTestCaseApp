package com.hakancevik.eterationtestcaseapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@HiltAndroidApp
class EterationCaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val masterKeyAlias = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "secret_shared_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        sharedPreferences.edit().putString("BASE_URL", "https://5fc9346b2af77700165ae514.mockapi.io/").apply()
    }


}