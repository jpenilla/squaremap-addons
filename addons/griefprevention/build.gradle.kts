description = "GriefPrevention integration for squaremap"

dependencies {
    compileOnly("com.github.TechFortress:GriefPrevention:16.18.2")
}

bukkitPluginYml {
    main = "xyz.jpenilla.squaremap.addon.griefprevention.SquaremapGriefPrevention"
    authors.add("BillyGalbreath")
    depend.add("GriefPrevention")
}
