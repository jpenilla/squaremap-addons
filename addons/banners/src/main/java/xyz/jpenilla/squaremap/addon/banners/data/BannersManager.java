package xyz.jpenilla.squaremap.addon.banners.data;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.jpenilla.squaremap.addon.banners.SquaremapBanners;
import xyz.jpenilla.squaremap.api.Key;

public class BannersManager {
    private final SquaremapBanners plugin;
    private final File dataDir;

    public BannersManager(SquaremapBanners plugin) {
        this.plugin = plugin;
        this.dataDir = new File(plugin.getDataFolder(), "data");

        if (!this.dataDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            this.dataDir.mkdirs();
        }
    }

    public void load() {
        plugin.squaremapHook().getProviders().forEach((uuid, provider) -> {
            YamlConfiguration config = new YamlConfiguration();
            try {
                File file = new File(dataDir, uuid + ".yml");
                if (file.exists()) {
                    this.plugin.debug("Loading " + uuid + ".yml");
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
                    this.plugin.getLogger().log(Level.SEVERE, "Could not load " + entry + " from " + uuid, e);
                }
            });
        });
    }

    public void save() {
        plugin.squaremapHook().getProviders().forEach((uuid, provider) -> {
            YamlConfiguration config = new YamlConfiguration();
            provider.getData().forEach((pos, data) -> {
                String entry = pos.x() + "," + pos.y() + "," + pos.z();
                this.plugin.debug("Entry: " + entry + " key: " + data.key().getKey() + " name: " + data.name());
                config.set(entry + ".key", data.key().getKey());
                config.set(entry + ".name", data.name());
            });
            try {
                this.plugin.debug("Saving data/" + uuid + ".yml ...");
                config.save(new File(new File(plugin.getDataFolder(), "data"), uuid + ".yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeBanner(BlockState state) {
        BannerLayerProvider provider = plugin.squaremapHook().getProvider(state.getWorld());
        if (provider == null) {
            return;
        }
        Location loc = state.getLocation();
        provider.remove(Position.of(loc));
    }

    public void putBanner(BlockState state, String name) {
        BannerLayerProvider provider = plugin.squaremapHook().getProvider(state.getWorld());
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
        BannerLayerProvider provider = plugin.squaremapHook().getProvider(chunk.getWorld());
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
