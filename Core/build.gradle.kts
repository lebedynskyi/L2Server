plugins {
    id("java-library")
    id("kotlin")
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    // Kotlin coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0")

    // Yaml
    api("com.charleskorn.kaml:kaml:0.43.0")
    api ("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")

    implementation("com.zaxxer:HikariCP:5.0.1")
}