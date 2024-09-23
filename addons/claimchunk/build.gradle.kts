description = "ClaimChunk addon for squaremap"

dependencies {
    compileOnly("maven.modrinth.workaround:claimchunk:unused")
}

bukkitPluginYaml {
    main = "xyz.jpenilla.squaremap.addon.claimchunk.SquaremapClaimChunk"
    authors.add("BillyGalbreath")
    depend.add("ClaimChunk")
}
