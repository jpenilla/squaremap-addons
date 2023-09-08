package xyz.jpenilla.squaremap.addon.banners.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.jpenilla.squaremap.addon.banners.SquaremapBanners;
import xyz.jpenilla.squaremap.addon.common.Util;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public final class BannersManager {
    private final SquaremapBanners plugin;
    private final Path dataDir;

    public BannersManager(SquaremapBanners plugin) {
        this.plugin = plugin;
        this.dataDir = new File(plugin.getDataFolder(), "data").toPath();

        if (!Files.exists(this.dataDir)) {
            try {
                Files.createDirectories(this.dataDir);
            } catch (final IOException e) {
                Util.rethrow(e);
            }
        }
    }

    private Path dataFile(final WorldIdentifier identifier) {
        final Path resolve = this.dataDir.resolve(identifier.asString().replace(":", "_") + ".yml");
        try {
            final Path old = this.dataDir.resolve(identifier + ".yml");
            if (Files.exists(old)) {
                Files.move(old, resolve);
            }
        } catch (final Exception ignore) {
        }
        return resolve;
    }

    public void load() {
        this.plugin.squaremapHook().getProviders().forEach((worldIdentifier, provider) -> {
            YamlConfiguration config = new YamlConfiguration();
            try {
                File file = this.dataFile(worldIdentifier).toFile();
                if (file.exists()) {
                    this.plugin.debug("Loading " + file);
                    config.load(file);
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            config.getKeys(false).forEach(entry -> {
                try {
                    this.plugin.debug("Entry: " + entry);
                    String[] split = entry.split(",");
                    int x = Integer.parseInt(split[0]);
                    int y = Integer.parseInt(split[1]);
                    int z = Integer.parseInt(split[2]);
                    Position pos = Position.of(x, y, z);
                    Key key = Key.of(Objects.requireNonNull(config.getString(entry + ".key")));
                    String name = config.getString(entry + ".name", "Unknown");
                    provider.add(pos, key, name);
                } catch (Exception e) {
                    this.plugin.getLogger().log(Level.SEVERE, "Could not load " + entry + " from " + worldIdentifier, e);
                }
            });
        });
    }

    public void save() {
        this.plugin.squaremapHook().getProviders().forEach((worldIdentifier, provider) -> {
            YamlConfiguration config = new YamlConfiguration();
            provider.getData().forEach((pos, data) -> {
                String entry = pos.x() + "," + pos.y() + "," + pos.z();
                this.plugin.debug("Entry: " + entry + " key: " + data.key().getKey() + " name: " + data.name());
                config.set(entry + ".key", data.key().getKey());
                config.set(entry + ".name", data.name());
            });
            try {
                final Path path = this.dataFile(worldIdentifier);
                this.plugin.debug("Saving data at " + path + " ...");
                config.save(path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeBanner(BlockState state) {
        BannerLayerProvider provider = this.plugin.squaremapHook().getProvider(state.getWorld());
        if (provider == null) {
            return;
        }
        Location loc = state.getLocation();
        provider.remove(Position.of(loc));
    }

    public void putBanner(BlockState state, String name) {
        BannerLayerProvider provider = this.plugin.squaremapHook().getProvider(state.getWorld());
        if (provider == null) {
            return;
        }
        provider.add(Position.of(state.getLocation()), Icons.getIcon(state.getType()), name);
    }

    public void checkChunk(Chunk chunk) {
        int minX = chunk.getX();
        int minZ = chunk.getZ();
        int maxX = minX + 16;
        int maxZ = minZ + 16;
        BannerLayerProvider provider = this.plugin.squaremapHook().getProvider(chunk.getWorld());
        if (provider != null) {
            provider.getPositions().forEach(pos -> {
                if (pos.x() >= minX && pos.z() >= minZ &&
                    pos.x() <= maxX && pos.z() <= maxZ &&
                    !pos.isBanner(chunk.getWorld())) {
                    provider.remove(pos);
                }
            });
        }
    }
}
