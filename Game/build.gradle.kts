plugins {
    id("application")
    id("kotlin")
    kotlin("plugin.serialization") version "1.6.10"
}

group = "org.example"
version = "1.0"
val appMainClassName = "lineage.vetal.server.game.MainKt"

application {
    mainClass.set(appMainClassName)
}

dependencies {
    implementation(project(":Shared"))
    implementation(project(":Network"))
}
