package xyz.jpenilla.squaremap.addon.claimchunk.data;

import java.util.UUID;

public record Claim(int x, int z, UUID owner) {
    public boolean isTouching(Claim claim) {
        if (!owner.equals(claim.owner)) {
            return false; // not same owner
        }
        if (claim.x == x && claim.z == z - 1) {
            return true; // touches north
        }
        if (claim.x == x && claim.z == z + 1) {
            return true; // touches south
        }
        if (claim.x == x - 1 && claim.z == z) {
            return true; // touches west
        }
        if (claim.x == x + 1 && claim.z == z) {
            return true; // touches east
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Claim other)) {
            return false;
        }
        return this.x == other.x && this.z == other.z;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + z;
        return result;
    }
}
