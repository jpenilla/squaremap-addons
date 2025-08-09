plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("net.kyori", "indra-common", "3.1.3")
    implementation("xyz.jpenilla:resource-factory:1.3.0")
    implementation("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:9.0.1")
}
