plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(Dependencies.kotlinCoroutines)
}