plugins {
    id("application")
    id("kotlin")
}

group = "org.example"
version = "1.0"

val appMainClassName = "lineage.vetal.server.login.MainKt"

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = appMainClassName
    }
}

dependencies {
    implementation(kotlin("script-runtime"))
    implementation(project(":Core"))
}