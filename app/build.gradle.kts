plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)   // Compose compiler (Kotlin 2.0)
}

android {
    namespace = "com.autodash.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.autodash.app"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    // Multi-brand: uma base, várias montadoras (ver docs/06).
    flavorDimensions += "brand"
    productFlavors {
        create("volvo")  { dimension = "brand"; applicationIdSuffix = ".volvo" }
        create("scania") { dimension = "brand"; applicationIdSuffix = ".scania" }
    }

    buildTypes { release { isMinifyEnabled = false } }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:carapp"))
    implementation(project(":data:car"))
    implementation(project(":domain"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
