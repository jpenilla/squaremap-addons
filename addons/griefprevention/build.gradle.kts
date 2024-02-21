description = "GriefPrevention integration for squaremap"

dependencies {
    compileOnly("com.github.TechFortress:GriefPrevention:16.18.2")
}

bukkit {
    main = "xyz.jpenilla.squaremap.addon.griefprevention.SquaremapGriefPrevention"
    addAuthor("BillyGalbreath")
    addDepend("GriefPrevention")
}
