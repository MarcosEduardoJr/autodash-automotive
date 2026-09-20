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
        // Marca PADRÃO deste build. White-label: a identidade é config, não código.
        // Em produção, cada OEM = um flavor que muda este valor + fornece recursos/RRO.
        buildConfigField("String", "DEFAULT_BRAND", "\"slate\"")
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
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
