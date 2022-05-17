plugins {
    base
    id("xyz.jpenilla.run-paper") version "1.0.6"
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
        minecraftVersion("1.18.2")
    }
}
