package xyz.jpenilla.squaremap.addon.banners.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.bukkit.Material;
import xyz.jpenilla.squaremap.addon.banners.SquaremapBanners;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public class Icons {
    public static final Key BLACK = register("BLACK");
    public static final Key BLUE = register("BLUE");
    public static final Key BROWN = register("BROWN");
    public static final Key CYAN = register("CYAN");
    public static final Key GRAY = register("GRAY");
    public static final Key GREEN = register("GREEN");
    public static final Key LIGHT_BLUE = register("LIGHT_BLUE");
    public static final Key LIGHT_GRAY = register("LIGHT_GRAY");
    public static final Key LIME = register("LIME");
    public static final Key MAGENTA = register("MAGENTA");
    public static final Key ORANGE = register("ORANGE");
    public static final Key PINK = register("PINK");
    public static final Key PURPLE = register("PURPLE");
    public static final Key RED = register("RED");
    public static final Key WHITE = register("WHITE");
    public static final Key YELLOW = register("YELLOW");

    private static Key register(String name) {
        SquaremapBanners plugin = SquaremapBanners.instance();
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
            SquaremapBanners.instance().getLogger().log(Level.WARNING, "Failed to register banners icon", e);
        }
        return key;
    }

    public static Key getIcon(Material type) {
        switch (type) {
            case BLACK_BANNER:
            case BLACK_WALL_BANNER:
                return BLACK;
            case BLUE_BANNER:
            case BLUE_WALL_BANNER:
                return BLUE;
            case BROWN_BANNER:
            case BROWN_WALL_BANNER:
                return BROWN;
            case CYAN_BANNER:
            case CYAN_WALL_BANNER:
                return CYAN;
            case GRAY_BANNER:
            case GRAY_WALL_BANNER:
                return GRAY;
            case GREEN_BANNER:
            case GREEN_WALL_BANNER:
                return GREEN;
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_WALL_BANNER:
                return LIGHT_BLUE;
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
                return LIGHT_GRAY;
            case LIME_BANNER:
            case LIME_WALL_BANNER:
                return LIME;
            case MAGENTA_BANNER:
            case MAGENTA_WALL_BANNER:
                return MAGENTA;
            case ORANGE_BANNER:
            case ORANGE_WALL_BANNER:
                return ORANGE;
            case PINK_BANNER:
            case PINK_WALL_BANNER:
                return PINK;
            case PURPLE_BANNER:
            case PURPLE_WALL_BANNER:
                return PURPLE;
            case RED_BANNER:
            case RED_WALL_BANNER:
                return RED;
            case YELLOW_BANNER:
            case YELLOW_WALL_BANNER:
                return YELLOW;
            case WHITE_BANNER:
            case WHITE_WALL_BANNER:
            default:
                return WHITE;
        }
    }
}
