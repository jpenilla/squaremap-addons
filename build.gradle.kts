plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "3.0.1"
}

allprojects {
    group = "xyz.jpenilla"
    version = "1.0.0-SNAPSHOT"
}

subprojects {
    if (name == "common") {
        return@subprojects
    }

    apply(plugin = "squaremap-addon")

    rootProject.tasks.runServer {
        pluginJars(tasks.named<AbstractArchiveTask>("shadowJar").flatMap { it.archiveFile })
    }
}

runPaper {
    disablePluginJarDetection()
}

tasks.jar {
    enabled = false
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    runServer {
        minecraftVersion("1.21.5")
        downloadPlugins {
            hangar("squaremap", "1.3.5")
            // url("https://ci.enginehub.org/repository/download/bt10/22612:id/worldedit-bukkit-7.2.16-SNAPSHOT-dist.jar?guest=1")
            // url("https://ci.enginehub.org/repository/download/bt11/22585:id/worldguard-bukkit-7.0.10-SNAPSHOT-dist.jar?guest=1")
        }
    }
}
