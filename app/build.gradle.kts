plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.dagger)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.store.shop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.store.shop"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.preference.ktx)
    implementation(libs.joda.time)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.junit)
    implementation(libs.androidx.activity)
    implementation(project(":auth"))
    implementation(project(":network_state"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.arch)
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.android)
    implementation(libs.multidex)
    implementation(libs.dots.indicator)

    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler.ksp)

    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler.ksp)

    implementation(libs.coroutine.android)
    implementation(libs.coroutine.core)


    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)


    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)


    implementation(libs.glide.transformations)
    implementation(libs.gpu.image)
    implementation(libs.glide)


    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)

}