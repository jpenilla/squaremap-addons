plugins {
    id("net.kyori.indra")
}

indra {
    javaVersions {
        target(17)
    }
}

tasks {
    jar {
        manifest {
            attributes(
                "paperweight-mappings-namespace" to "mojang"
            )
        }
    }
}
