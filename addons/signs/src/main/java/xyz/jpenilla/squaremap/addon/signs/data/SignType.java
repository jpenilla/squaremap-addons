package xyz.jpenilla.squaremap.addon.signs.data;

import java.util.Locale;
import org.bukkit.Material;
import xyz.jpenilla.squaremap.addon.signs.SignsPlugin;
import xyz.jpenilla.squaremap.api.Key;

public enum SignType {
    OAK(Icons.OAK),
    SPRUCE(Icons.SPRUCE),
    BIRCH(Icons.BIRCH),
    JUNGLE(Icons.JUNGLE),
    ACACIA(Icons.ACACIA),
    DARK_OAK(Icons.DARK_OAK),
    CRIMSON(Icons.CRIMSON),
    WARPED(Icons.WARPED),
    BAMBOO(Icons.OAK), // todo
    MANGROVE(Icons.OAK), // todo
    CHERRY(Icons.OAK); // todo

    public final Key iconKey;

    SignType(final Key iconKey) {
        this.iconKey = iconKey;
    }

    public static SignType typeOrDefault(final Material type) {
        return SignType.typeOrDefault(
            type.name()
                .replace("_WALL_HANGING_SIGN", "")
                .replace("_HANGING_SIGN", "")
                .replace("_WALL_SIGN", "")
                .replace("_SIGN", "")
        );
    }

    public static SignType typeOrDefault(final String name) {
        try {
            return SignType.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException e) {
            SignsPlugin.instance().getLogger().warning("Missing SignType for " + name);
            return OAK;
        }
    }
}
