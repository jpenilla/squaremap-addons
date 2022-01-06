package xyz.jpenilla.squaremap.addon.banners.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Banner;
import xyz.jpenilla.squaremap.api.Point;

public record Position(int x, int y, int z) {
    public static Position of(Location loc) {
        return new Position(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static Position of(int x, int y, int z) {
        return new Position(x, y, z);
    }

    public Point point() {
        return Point.of(this.x, this.z);
    }

    public boolean isBanner(World world) {
        return world.getBlockAt(this.x, this.y, this.z).getState() instanceof Banner;
    }
}
