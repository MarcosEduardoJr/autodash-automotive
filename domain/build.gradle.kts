plugins { alias(libs.plugins.kotlin.jvm) }
// PURO: depende só de core:model + coroutines. Nada de Android/car.
dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines)
}
