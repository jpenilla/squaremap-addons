plugins {
    id("base-conventions")
    id("net.minecrell.plugin-yml.bukkit")
}

dependencies {
    "compileAndTest"("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    "compileAndTest"("xyz.jpenilla:squaremap-api:1.1.0-SNAPSHOT")
}

val jar = tasks.named<AbstractArchiveTask>("jar").flatMap { it.archiveFile }
val copyJar = tasks.register("copyJar", CopyFile::class) {
    fileToCopy.set(jar)
    destination.set(jar.flatMap { rootProject.layout.buildDirectory.file("libs/${it.asFile.name}") })
}

tasks {
    assemble {
        dependsOn(copyJar)
    }
}

bukkit {
    apiVersion = "1.18"
    addDepend("squaremap")
    addAuthor("jmp")
}
