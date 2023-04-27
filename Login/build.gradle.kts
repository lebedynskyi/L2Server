plugins {
    id("application")
    id("kotlin")
    kotlin("plugin.serialization") version "1.6.10"
}

apply(from = "../tools/lint/lint.gradle")

group = "org.example"
version = "1.0"
val appMainClassName = "lineage.vetal.server.login.MainKt"

application {
    mainClass.set(appMainClassName)
}

dependencies {
    implementation(project(":Shared"))
    implementation(project(":Network"))
}