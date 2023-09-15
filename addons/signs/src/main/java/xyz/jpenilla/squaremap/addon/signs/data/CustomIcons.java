package xyz.jpenilla.squaremap.addon.signs.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public final class CustomIcons {
    private static final String KEY_PREFIX = "signs_custom_";
    private static final String CUSTOM_ICONS_DIR = "customicons";

    private List<Key> keys = new ArrayList<>();

    public static CustomIcons register(final @NonNull JavaPlugin plugin) {
        CustomIcons out = new CustomIcons();

        final File iconDir = new File(plugin.getDataFolder(), CUSTOM_ICONS_DIR);
        iconDir.mkdir();

        final File[] iconFiles = iconDir.listFiles();
        if (iconFiles == null) {
            plugin.getSLF4JLogger().warn("unable to read customicons directory at \"{}\"", iconDir.getAbsolutePath());
            return out;
        }

        for (final File file : iconFiles) {
            if (!file.isFile()) {
                continue;
            }
            try {
                final String stem = file.getName().replaceAll("\\..*$", "");
                final BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    throw new IOException("no image data");
                }
                final Key key = CustomIcons.asKey(stem);
                out.keys.add(key);
                SquaremapProvider.get().iconRegistry().register(key, image);
                plugin.getSLF4JLogger().trace("loaded custom icon {} → \"{}\"", key, file);
            } catch (final IOException e) {
                plugin.getSLF4JLogger().warn("unable to read custom icon \"{}\" as an image", file);
            }
        }

        return out;
    }

    public final Optional<Key> lookup(final @Nullable String iconName) {
        if (iconName == null || iconName.isEmpty()) {
            return Optional.empty();
        }
        Key k = CustomIcons.asKey(iconName);
        if (keys.contains(k)) {
            return Optional.of(k);
        }
        return Optional.empty();
    }

    public void unregister() {
        for (final Key key : this.keys) {
            SquaremapProvider.get().iconRegistry().unregister(key);
        }
        this.keys.clear();
    }

    public boolean isEmpty() {
        return this.keys.isEmpty();
    }

    private static final Key asKey(final @NonNull String iconName) {
        return Key.of(KEY_PREFIX + iconName);
    }
}
