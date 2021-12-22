package xyz.jpenilla.squaremap.addon.signs.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import xyz.jpenilla.squaremap.api.Point;

public record Position(int x, int y, int z) {
    public static Position of(Location loc) {
        return new Position(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static Position of(int x, int y, int z) {
        return new Position(x, y, z);
    }

    public Point point() {
        return Point.of(x, z);
    }

    public boolean isSign(World world) {
        return world.getBlockAt(x, y, z).getState() instanceof Sign;
    }
}
