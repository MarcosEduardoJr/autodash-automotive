plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.autodash.feature.carapp"
    compileSdk = 34
    defaultConfig { minSdk = 29 }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":core:model"))
    implementation(libs.androidx.car.app)            // Car App Library (templates)
    // implementation(libs.androidx.car.app.automotive) // host automotivo em runtime
}
