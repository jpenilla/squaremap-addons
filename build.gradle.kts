plugins {
    base
    id("xyz.jpenilla.run-paper") version "2.2.2"
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

tasks {
    runServer {
        minecraftVersion("1.20.1")
        downloadPlugins {
            hangar("squaremap", "1.2.0")
            url("https://ci.enginehub.org/repository/download/bt10/22612:id/worldedit-bukkit-7.2.16-SNAPSHOT-dist.jar?guest=1")
            url("https://ci.enginehub.org/repository/download/bt11/22585:id/worldguard-bukkit-7.0.10-SNAPSHOT-dist.jar?guest=1")
        }
    }
}
