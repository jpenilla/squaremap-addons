description = "ClaimChunk addon for squaremap"

dependencies {
    compileOnly("com.github.cjburkey01:ClaimChunk:0.0.22") {
        isTransitive = false
    }
}

bukkit {
    main = "xyz.jpenilla.squaremap.addon.claimchunk.SquaremapClaimChunk"
    authors.add("BillyGalbreath")
    depend.add("ClaimChunk")
}
