package xyz.jpenilla.squaremap.addon.signs.data;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import xyz.jpenilla.squaremap.api.Point;

public record Position(int x, int y, int z) {
    public static Position of(Location loc) {
        return new Position(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static Position of(final int x, final int y, final int z) {
        return new Position(x, y, z);
    }

    public Point point() {
        return Point.of(this.x, this.z);
    }

    public boolean isSign(final World world) {
        return world.getBlockAt(this.x, this.y, this.z).getState() instanceof Sign;
    }

    public static final class TypeAdapter extends com.google.gson.TypeAdapter<Position> {
        @Override
        public void write(final JsonWriter out, final Position value) throws IOException {
            out.value(value.x() + ";" + value.y() + ";" + value.z());
        }

        @Override
        public Position read(final JsonReader in) throws IOException {
            final List<Integer> coords = Arrays.stream(in.nextString().split(";"))
                .map(Integer::parseInt)
                .toList();
            return of(coords.get(0), coords.get(1), coords.get(2));
        }
    }
}
