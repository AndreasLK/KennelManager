plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.compose")

}

android {
    namespace = "com.example.diarreatracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.diarreatracker"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // ─── AndroidX UI / Core / Testing (unchanged) ────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.bcprov.jdk15on)
    implementation(libs.bcpkix.jdk15on)
    implementation(libs.androidx.media3.common.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ─── Retrofit, OkHttp, kotlinx-serialization & Coroutines (NEW) ────────────

    // 1) Retrofit 3.0.0: type-safe HTTP client with first-party Kotlinx serialization converter :contentReference[oaicite:0]{index=0}
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)

    // 2) OkHttp 4.10.0: underlying HTTP client for Retrofit :contentReference[oaicite:1]{index=1}
    implementation(libs.okhttp)

    // 3) kotlinx-serialization-json 1.8.1: JSON runtime for Kotlin serialization :contentReference[oaicite:2]{index=2}
    implementation(libs.kotlinx.serialization.json.jvm)

    // 4) kotlinx-coroutines-android & core 1.10.2: coroutine support on Android :contentReference[oaicite:3]{index=3}
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}
