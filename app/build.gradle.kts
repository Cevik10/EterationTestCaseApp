plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    alias(libs.plugins.paparazzi)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.hakancevik.eterationtestcaseapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hakancevik.eterationtestcaseapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.bundles.androidxCore)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.otherLibraries)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.room)
    kapt(libs.androidxRoomCompiler)
    kapt(libs.bundles.hiltKapt)
    annotationProcessor(libs.hiltCompiler)

    implementation(libs.glide)
    kapt(libs.bundles.glideKapt)

    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.uiTesting)
}