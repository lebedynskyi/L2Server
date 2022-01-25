plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0")

    // Yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.1")
}