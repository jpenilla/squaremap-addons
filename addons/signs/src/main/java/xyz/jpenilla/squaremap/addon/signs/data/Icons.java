package xyz.jpenilla.squaremap.addon.signs.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
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
}
