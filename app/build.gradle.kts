plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.asknitt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.asknitt"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies{

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation(libs.androidx.media3.effect)

    val composeBom = platform("androidx.compose:compose-bom-beta:2025.06.01") // Use the latest stable BOM
    implementation(composeBom)
    androidTestImplementation(composeBom) // For tests

    implementation("androidx.compose.material3:material3") // Version managed by BOM
    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material)
    testImplementation(libs.junit)
    implementation("io.coil-kt:coil-compose:2.4.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.9.0") {
        exclude(group = "androidx.navigation", module = "navigation-compose-jvmstubs")
    }
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")


    implementation (libs.androidx.graphics.shapes)


}