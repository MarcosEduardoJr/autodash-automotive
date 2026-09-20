plugins { alias(libs.plugins.kotlin.jvm) }
// Puro JVM: nenhum import de Android/car. Teste roda na hora.
dependencies { testImplementation(libs.junit) }
