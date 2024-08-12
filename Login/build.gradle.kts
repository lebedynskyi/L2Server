plugins {
    id("application")
    id("kotlin")
    kotlin("plugin.serialization") version "2.0.10"
}

apply(from = "../buildTools/lint/lint.gradle")

group = "org.example"
version = "1.0"
val appMainClassName = "lineage.vetal.server.login.MainKt"

application {
    mainClass.set(appMainClassName)
}

dependencies {
    implementation(project(":Shared"))
}