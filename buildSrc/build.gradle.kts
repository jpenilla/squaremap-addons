plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("net.kyori", "indra-common", "3.1.3")
    implementation("xyz.jpenilla:resource-factory:1.1.1")
    implementation("com.github.johnrengelman:shadow:8.1.1")
}
