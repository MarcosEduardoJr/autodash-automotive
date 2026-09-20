plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.autodash.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.autodash.app"
        minSdk = 29          // AAOS moderno
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

    buildFeatures { compose = true }
    kotlinOptions { jvmTarget = "17" }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:carapp"))
    implementation(project(":data:car"))
    implementation(project(":core:designsystem"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
}
