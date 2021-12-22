dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.jpenilla.xyz/snapshots/") {
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
        maven("https://jitpack.io") {
            mavenContent {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

rootProject.name = "squaremap-addons"

includeAddon("signs")
includeAddon("mobs")
includeAddon("worldguard")
includeAddon("essentialsx")
includeAddon("deathspot")
includeAddon("skins")
includeAddon("griefprevention")
includeAddon("claimchunk")

fun includeAddon(addonName: String) {
    val name = "squaremap-$addonName"
    include(name)
    project(":$name").apply {
        projectDir = file("addons/$addonName")
    }
}
