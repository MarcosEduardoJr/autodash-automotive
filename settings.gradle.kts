// ─────────────────────────────────────────────────────────────
// Raiz do build multi-módulo. Cada include() é um módulo Gradle.
// Modularizar não é firula: no carro, um build por marca (flavor)
// precisa ser rápido, e camadas isoladas mantêm a regra de negócio
// testável na JVM, sem emulador automotivo. Ver README.md da raiz.
// ─────────────────────────────────────────────────────────────
pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}
rootProject.name = "autodash-automotive"

include(":app")
include(":core:model", ":core:common", ":core:designsystem")
include(":domain")
include(":data:car")
include(":feature:dashboard", ":feature:carapp", ":feature:climate")
