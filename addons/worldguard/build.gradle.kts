description = "WorldGuard addon for squaremap"

dependencies {
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.14") {
        exclude("org.bstats")
        exclude("org.spigotmc")
    }
}

bukkitPluginYaml {
    main = "xyz.jpenilla.squaremap.addon.worldguard.SquaremapWorldGuard"
    authors.add("BillyGalbreath")
    depend.add("WorldGuard")
}
