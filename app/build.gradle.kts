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
    compileSdk = 35

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

    //Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    debugImplementation(libs.androidx.ui.compose.test.manifest)
    ksp(libs.room.compiler.ksp)

    //Dagger Hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler.ksp)
    
    implementation(libs.androidx.compose.coil)
    implementation(libs.androidx.ui.compose)
    implementation(libs.androidx.ui.compose.graphics)
    implementation(project(":auth"))
    implementation(project(":network_state"))
}