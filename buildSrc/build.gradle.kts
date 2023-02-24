plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("net.kyori", "indra-common", "2.2.0")
    implementation("net.minecrell:plugin-yml:0.5.3")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
}
