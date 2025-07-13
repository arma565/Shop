plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.hilt.dagger)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.authentication.auth"
    compileSdk = 36

    packaging {
        resources {
            excludes += arrayOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md" // (optional if you also get this one)
            )
        }
    }

    defaultConfig {
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
    }

    kotlin {
        jvmToolchain(24)
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.gson)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.constraint.layout)
    runtimeOnly(libs.androidx.compose.compiler)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.compose.livedata)
    implementation(libs.androidx.compose.icons.core)
    implementation(libs.androidx.compose.icons.extended)
    implementation(libs.androidx.compose.window.size)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.tooling.debug)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.compose.activity)

    //dagger hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler.ksp)

    //coroutine
    implementation(libs.coroutine.android)
    implementation(libs.coroutine.core)

    //Retrofit
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)

    //test
    implementation(libs.androidx.junit)
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.androidx.test.core)
    implementation(libs.coroutine.test)
    implementation(libs.turbine)
    implementation(libs.mockk)
    implementation(libs.mockk.android)
    implementation(libs.mockwebserver)
    implementation(libs.androidx.ext)
    implementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(project(":network_state"))
}