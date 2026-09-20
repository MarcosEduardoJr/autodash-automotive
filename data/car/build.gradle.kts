plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.autodash.data.car"
    compileSdk = 34
    defaultConfig { minSdk = 29 }
    // android.car é da PLATAFORMA (existe no head unit). Precisamos compilar contra ele,
    // mas NÃO empacotar. useLibrary marca a lib do sistema; o ideal é compileOnly.
    useLibrary("android.car")
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines)
}
