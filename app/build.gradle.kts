plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.hilt.dagger)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.store.shop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.store.shop"
        minSdk = 25
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
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.joda.time)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ext)
    androidTestRuntimeOnly(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.android)

    //Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    debugImplementation(libs.androidx.ui.compose.test.manifest)
    ksp(libs.room.compiler.ksp)

    //Dagger Hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler.ksp)

    //Coroutine
    implementation(libs.coroutine.android)
    implementation(libs.coroutine.core)

    //Retrofit
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)

    //Compose
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.constraint.layout)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.compose.livedata)
    implementation(libs.androidx.compose.icons.core)
    implementation(libs.androidx.compose.icons.extended)
    implementation(libs.androidx.compose.window.size)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.ui.compose.tooling.preview)
    debugImplementation(libs.androidx.ui.compose.tooling.debug)
    androidTestImplementation(libs.androidx.ui.compose.test.junit4)
    debugRuntimeOnly(libs.androidx.ui.compose.test.manifest)
    implementation(libs.androidx.compose.animation)
    implementation(libs.coil.compose)
    implementation(libs.androidx.ui.compose)
    implementation(libs.androidx.ui.compose.graphics)
    implementation(project(":auth"))
    implementation(project(":network_state"))
}