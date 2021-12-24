plugins {
    id("base-conventions")
    id("net.minecrell.plugin-yml.bukkit")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":common"))
}

tasks {
    val copyJar = register("copyJar", CopyFile::class) {
        fileToCopy.set(shadowJar.flatMap { it.archiveFile })

        val projectVersion = project.version
        inputs.property("projectVer", projectVersion)
        destination.set(
            base.archivesName.flatMap {
                rootProject.layout.buildDirectory.file("libs/$it-$projectVersion.jar")
            }
        )
    }

    shadowJar {
        relocate(
            "xyz.jpenilla.squaremap.addon.common",
            "xyz.jpenilla.squaremap.addon.${project.name.substring("squaremap-".length)}.shaded.common"
        )
    }

    assemble {
        dependsOn(copyJar)
    }
}

bukkit {
    apiVersion = "1.18"
    addDepend("squaremap")
    addAuthor("jmp")
}
