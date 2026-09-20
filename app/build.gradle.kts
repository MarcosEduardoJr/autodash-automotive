plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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

    // WHITE-LABEL: cada marca é um FLAVOR (aparece em Build Variants). Nomes NEUTROS,
    // não OEMs reais. Trocar a identidade = trocar de flavor (sem tocar nas telas).
    flavorDimensions += "brand"
    productFlavors {
        create("slate")  { dimension = "brand"; buildConfigField("String", "DEFAULT_BRAND", "\"slate\"") }
        create("aurora") { dimension = "brand"; applicationIdSuffix = ".aurora"; buildConfigField("String", "DEFAULT_BRAND", "\"aurora\"") }
        create("ember")  { dimension = "brand"; applicationIdSuffix = ".ember"; buildConfigField("String", "DEFAULT_BRAND", "\"ember\"") }
        create("nord")   { dimension = "brand"; applicationIdSuffix = ".nord"; buildConfigField("String", "DEFAULT_BRAND", "\"nord\"") }
    }

    buildTypes { release { isMinifyEnabled = false } }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:carapp"))
    implementation(project(":feature:climate"))
    implementation(project(":data:car"))
    implementation(project(":domain"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
