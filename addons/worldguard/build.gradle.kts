description = "WorldGuard addon for squaremap"

dependencies {
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9") {
        exclude("org.bstats")
        exclude("org.spigotmc")
    }
}

bukkit {
    main = "xyz.jpenilla.squaremap.addon.worldguard.SquaremapWorldGuard"
    addAuthor("BillyGalbreath")
    addDepend("WorldGuard")
}
