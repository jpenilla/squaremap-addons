plugins {
    `java-library`
    id("net.kyori.indra")
}

val compileAndTest: Configuration by configurations.creating
configurations.named("compileOnly") {
    extendsFrom(compileAndTest)
}
configurations.named("testImplementation") {
    extendsFrom(compileAndTest)
}

indra {
    javaVersions {
        target(17)
    }
}
