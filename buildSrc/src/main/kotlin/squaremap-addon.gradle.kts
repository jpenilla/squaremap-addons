import xyz.jpenilla.resourcefactory.bukkit.bukkitPluginYml

plugins {
    id("base-conventions")
    id("xyz.jpenilla.resource-factory")
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

        fun reloc(pkg: String) = relocate(
            pkg,
            "xyz.jpenilla.squaremap.addon.${project.name.substring("squaremap-".length)}.shaded.$pkg"
        )
        reloc("io.leangen.geantyref")
        reloc("org.spongepowered.configurate")
        reloc("org.yaml.snakeyaml")
    }

    assemble {
        dependsOn(copyJar)
    }
}

val bukkitPluginYml = bukkitPluginYml {
    apiVersion = "1.18"
    depend.add("squaremap")
    authors.add("jmp")
}

sourceSets.main {
    resourceFactory.factory(bukkitPluginYml.resourceFactory())
}

extensions.add("bukkit", bukkitPluginYml)
