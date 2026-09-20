plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.autodash.data.car"
    compileSdk = 34
    defaultConfig { minSdk = 29 }
    // android.car é lib OPCIONAL da plataforma (existe no head unit). Compilamos contra
    // ela, sem empacotar. useLibrary a adiciona ao classpath de compilação.
    useLibrary("android.car")
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
}
