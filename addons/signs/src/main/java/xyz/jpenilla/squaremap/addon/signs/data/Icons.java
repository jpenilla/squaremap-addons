package xyz.jpenilla.squaremap.addon.signs.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public final class Icons {
    private static final List<Key> KEYS = new ArrayList<>();

    public static final Key OAK = icon("sign_oak");
    public static final Key SPRUCE = icon("sign_spruce");
    public static final Key BIRCH = icon("sign_birch");
    public static final Key JUNGLE = icon("sign_jungle");
    public static final Key ACACIA = icon("sign_acacia");
    public static final Key DARK_OAK = icon("sign_dark_oak");
    public static final Key CRIMSON = icon("sign_crimson");
    public static final Key WARPED = icon("sign_warped");

    private static Key icon(final String name) {
        final Key key = Key.of(name);
        KEYS.add(key);
        return key;
    }

    public static void register(final JavaPlugin plugin) {
        try {
            for (final Key key : KEYS) {
                final String filename = "icons" + File.separator + key.getKey() + ".png";
                final File file = new File(plugin.getDataFolder(), filename);
                if (!file.exists()) {
                    plugin.saveResource(filename, false);
                }
                final BufferedImage image = ImageIO.read(file);
                SquaremapProvider.get().iconRegistry().register(key, image);
            }
        } catch (final IOException e) {
            throw new RuntimeException("Failed to register signs icons", e);
        }
    }

    public static void unregister() {
        for (final Key key : KEYS) {
            SquaremapProvider.get().iconRegistry().unregister(key);
        }
    }
}
