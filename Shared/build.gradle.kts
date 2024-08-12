plugins {
    id("java-library")
    id("kotlin")
    kotlin("plugin.serialization") version "2.0.10"
}

dependencies {
    api(project(":Socks"))

    // Yaml
    api("com.charleskorn.kaml:kaml:0.60.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.1")

    //HikariCP and its logger
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")
    implementation ("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-core:1.4.14")
    implementation("ch.qos.logback:logback-classic:1.4.12")
}