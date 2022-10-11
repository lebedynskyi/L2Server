plugins {
    id("java-library")
    id("kotlin")
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    implementation(project(":Network"))

    // Kotlin coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0")

    // Yaml
    api("com.charleskorn.kaml:kaml:0.43.0")
    api ("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")

    //HikariCP and its logger
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-core:1.2.11")
    implementation("ch.qos.logback:logback-classic:1.2.11")
}