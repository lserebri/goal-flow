import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.adarshr.test-logger") version "4.0.0"
}

android {
    namespace = "lserebri.goalflow"
    compileSdk = 36

    defaultConfig {
        applicationId = "lserebri.goalflow"
        minSdk = 28
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
	kotlin {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_17
		}
	}
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
	// Core Android
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)

	// Compose
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.compose.material.icons.extended)
	implementation(libs.androidx.compose.ui.text.google.fonts)

	// Dependency Injection - Hilt
	implementation(libs.hilt.android)
	implementation(libs.androidx.hilt.navigation.compose)
	implementation(libs.androidx.compose.animation.core)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.foundation)
	implementation(libs.androidx.compose.foundation.layout)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui)
	ksp(libs.hilt.android.compiler)

	// Coroutines
	implementation(libs.kotlinx.coroutines.android)

	// Database - Room
	implementation(libs.room.runtime)
	implementation(libs.room.ktx)
	ksp(libs.room.compiler)

	// Unit Tests
	testImplementation(libs.junit)
	testImplementation(libs.mockk)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(kotlin("test"))

	// Android Instrumentation Tests
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)

	// Debug Dependencies
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}
