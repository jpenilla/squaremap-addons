description = "ClaimChunk addon for squaremap"

dependencies {
    compileAndTest("com.github.cjburkey01:ClaimChunk:0.0.22") {
        isTransitive = false
    }
}

bukkit {
    main = "xyz.jpenilla.squaremap.addon.claimchunk.SquaremapClaimChunk"
    addAuthor("BillyGalbreath")
    addDepend("ClaimChunk")
}
