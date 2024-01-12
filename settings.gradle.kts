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
