dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            mavenContent {
                snapshotsOnly()
                includeGroup("xyz.jpenilla")
            }
        }
        maven("https://maven.enginehub.org/repo/") {
            mavenContent {
                includeGroup("com.sk89q")
                includeGroupByRegex("com\\.sk89q\\..*")
            }
        }
        maven("https://repo.essentialsx.net/releases/") {
            mavenContent {
                includeGroup("net.essentialsx")
            }
        }
        maven("https://jitpack.io/") {
            mavenContent {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
        modrinthMavenWorkaround(
            "claimchunk",
            "0.0.25-FIX3",
            "claimchunk-0.0.25-FIX3-plugin.jar"
        )
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "squaremap-addons"

include(":common")

includeAddon("signs")
includeAddon("mobs")
includeAddon("worldguard")
includeAddon("essentialsx")
includeAddon("deathspot")
includeAddon("skins")
includeAddon("griefprevention")
includeAddon("claimchunk")
includeAddon("banners")
includeAddon("vanish")

fun includeAddon(addonName: String) {
    val name = "squaremap-$addonName"
    include(name)
    project(":$name").apply {
        projectDir = file("addons/$addonName")
    }
}

// https://github.com/modrinth/code/issues/2428
fun RepositoryHandler.modrinthMavenWorkaround(nameOrId: String, version: String, fileName: String) {
    val url = "https://api.modrinth.com/maven/maven/modrinth/$nameOrId/$version/$fileName"
    val group = "maven.modrinth.workaround"
    ivy(url.substringBeforeLast('/')) {
        name = "Modrinth Maven Workaround for $nameOrId"
        patternLayout { artifact(url.substringAfterLast('/')) }
        metadataSources { artifact() }
        content { includeModule(group, nameOrId) }
    }
}
