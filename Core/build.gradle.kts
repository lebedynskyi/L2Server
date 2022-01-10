plugins {
    id("java-library")
    id("kotlin")
}


val ktorVersion = "1.6.7"
dependencies {
    implementation(kotlin("stdlib"))

    // Ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-network:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.10")
}