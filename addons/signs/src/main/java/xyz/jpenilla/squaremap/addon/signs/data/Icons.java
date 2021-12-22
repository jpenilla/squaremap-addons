package xyz.jpenilla.squaremap.addon.signs.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.bukkit.Material;
import xyz.jpenilla.squaremap.addon.signs.SignsPlugin;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public final class Icons {
    public static final Key OAK = register("sign_oak");
    public static final Key SPRUCE = register("sign_spruce");
    public static final Key BIRCH = register("sign_birch");
    public static final Key JUNGLE = register("sign_jungle");
    public static final Key ACACIA = register("sign_acacia");
    public static final Key DARK_OAK = register("sign_dark_oak");
    public static final Key CRIMSON = register("sign_crimson");
    public static final Key WARPED = register("sign_warped");

    private static Key register(String name) {
        SignsPlugin plugin = SignsPlugin.getInstance();
        String filename = "icons" + File.separator + name + ".png";
        File file = new File(plugin.getDataFolder(), filename);
        if (!file.exists()) {
            plugin.saveResource(filename, false);
        }
        Key key = Key.of(name);
        try {
            BufferedImage image = ImageIO.read(file);
            SquaremapProvider.get().iconRegistry().register(key, image);
        } catch (IOException e) {
           throw new RuntimeException("Failed to register signs icon", e);
        }
        return key;
    }

    public static Key getIcon(Material type) {
        return switch (type) {
            case SPRUCE_SIGN, SPRUCE_WALL_SIGN -> SPRUCE;
            case BIRCH_SIGN, BIRCH_WALL_SIGN -> BIRCH;
            case JUNGLE_SIGN, JUNGLE_WALL_SIGN -> JUNGLE;
            case ACACIA_SIGN, ACACIA_WALL_SIGN -> ACACIA;
            case DARK_OAK_SIGN, DARK_OAK_WALL_SIGN -> DARK_OAK;
            case CRIMSON_SIGN, CRIMSON_WALL_SIGN -> CRIMSON;
            case WARPED_SIGN, WARPED_WALL_SIGN -> WARPED;
            // case OAK_SIGN, OAK_WALL_SIGN -> OAK;
            default -> OAK;
        };
    }
}
