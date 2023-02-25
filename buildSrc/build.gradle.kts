plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("net.kyori", "indra-common", "2.1.1")
    implementation("net.minecrell:plugin-yml:0.5.3")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:8.0.0")
}
