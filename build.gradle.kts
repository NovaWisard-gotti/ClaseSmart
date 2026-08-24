// ClaseSmart - build.gradle.kts raíz
// Versiones fijas y estables (sin rangos dinámicos), compatibles entre sí:
// AGP 8.5.2 + Kotlin 1.9.24 + Compose Compiler 1.5.14 + Room 2.6.1 + JDK 17 + minSdk 24

plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "1.9.24" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
