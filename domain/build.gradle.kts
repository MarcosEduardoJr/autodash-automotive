plugins { alias(libs.plugins.kotlin.jvm) }
// PURO: depende só de core:model/common + coroutines. Nada de Android/car.
dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
