plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.appmilsabores"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appmilsabores"
        minSdk = 24
        targetSdk = 36
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
// Pega este bloque completo en tu archivo app/build.gradle.kts

    dependencies {

        implementation("androidx.core:core-ktx:1.17.0")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
        implementation("androidx.activity:activity-compose:1.11.0")
        implementation(platform("androidx.compose:compose-bom:2024.09.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")

        // --- DEPENDENCIAS CORREGIDAS PARA MVVM Y NAVEGACIÓN ---
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4") // Versión estable y alineada
        implementation("androidx.navigation:navigation-compose:2.7.7")     // Versión estable y compatible

        // --- DEPENDENCIAS DE TEST (Déjalas como están en tu proyecto) ---
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
        androidTestImplementation("androidx.compose.ui:ui-test-junit4")
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")

    }
