plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    id(libs.plugins.kotlin.kapt.get().pluginId)
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
    buildFeatures {
        viewBinding = true
    }
    testOptions {
        unitTests {
            unitTests.isIncludeAndroidResources = true
        }
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
    implementation(libs.androidx.fragment.testing)
    kapt(libs.androidxRoomCompiler)
    kapt(libs.bundles.hiltKapt)
    annotationProcessor(libs.hiltCompiler)

    implementation(libs.glide)
    kapt(libs.bundles.glideKapt)

    testImplementation(libs.bundles.testing)
    testImplementation (libs.hamcrest)
    androidTestImplementation(libs.bundles.uiTesting)

    // AndroidX Test - JVM testing
    testImplementation (libs.junit)
    testImplementation (libs.mockito.core.v3112)
    testImplementation (libs.truth)
    testImplementation(libs.robolectric)
    testImplementation (libs.kotlinx.coroutines.test.v161)

    // AndroidX Test - Instrumented testing
    androidTestImplementation (libs.androidx.junit.v113)
    androidTestImplementation (libs.androidx.runner)
    androidTestImplementation (libs.androidx.rules)
    debugImplementation (libs.androidx.fragment.testing.v141)
    androidTestImplementation (libs.mockito.android)
    testImplementation(libs.robolectric.v473)

    implementation("androidx.security:security-crypto:1.1.0-alpha03")
}
