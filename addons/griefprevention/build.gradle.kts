description = "GriefPrevention integration for squaremap"

dependencies {
    compileOnly("com.github.TechFortress:GriefPrevention:17.0.0")
}

bukkitPluginYaml {
    main = "xyz.jpenilla.squaremap.addon.griefprevention.SquaremapGriefPrevention"
    authors.add("BillyGalbreath")
    depend.add("GriefPrevention")
}
