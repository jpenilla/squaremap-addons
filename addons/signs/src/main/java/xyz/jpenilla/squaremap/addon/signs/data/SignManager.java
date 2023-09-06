package xyz.jpenilla.squaremap.addon.signs.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leangen.geantyref.TypeToken;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.jpenilla.squaremap.addon.signs.SignsPlugin;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public final class SignManager {
    private final SignsPlugin plugin;
    private final File dataDir;
    private final Gson gson = GsonComponentSerializer.gson().populator().apply(new GsonBuilder()).create();

    public SignManager(SignsPlugin plugin) {
        this.plugin = plugin;
        this.dataDir = new File(plugin.getDataFolder(), "data");

        if (!this.dataDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            this.dataDir.mkdirs();
        }
    }

    public void load() {
        this.migrateData();

        this.plugin.squaremapHook().getProviders().forEach((id, provider) -> {
            List<SignMarkerData> data = new ArrayList<>();
            try {
                Path file = this.dataFile(id);
                if (Files.isRegularFile(file)) {
                    try (final Reader r = Files.newBufferedReader(file)) {
                        data = this.gson.fromJson(r, new TypeToken<List<SignMarkerData>>() {}.getType());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            data.forEach(entry -> {
                try {
                    provider.add(entry.pos, SignType.typeOrDefault(entry.type), entry.frontLines.toArray(new Component[0]), entry.backLines.toArray(new Component[0]));
                } catch (Exception e) {
                    this.plugin.getLogger().log(Level.SEVERE, "Could not load " + entry + " from " + id, e);
                }
            });
        });
    }

    private Path dataFile(final WorldIdentifier id) {
        return new File(this.dataDir, id.asString().replace(':', '_') + ".json").toPath();
    }

    public static final class SignMarkerData {
        public Position pos;
        public String type;
        public List<Component> frontLines;
        public List<Component> backLines;
    }

    public void save() {
        plugin.squaremapHook().getProviders().forEach((id, provider) -> {
            List<SignMarkerData> list = new ArrayList<>();
            provider.getData().forEach((pos, data) -> {
                final SignMarkerData s = new SignMarkerData();
                s.pos = pos;
                s.type = data.type().name();
                s.frontLines = Arrays.asList(data.front());
                s.backLines = Arrays.asList(data.back());
                list.add(s);
            });
            try (final Writer w = Files.newBufferedWriter(this.dataFile(id))) {
                this.gson.toJson(list, new TypeToken<List<SignMarkerData>>() {}.getType(), w);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeSign(BlockState state) {
        SignLayerProvider provider = plugin.squaremapHook().getProvider(state.getWorld());
        if (provider == null) {
            return;
        }
        Location loc = state.getLocation();
        provider.remove(Position.of(loc));
    }

    public void putSign(BlockState state, Component[] front, Component[] back) {
        SignLayerProvider provider = plugin.squaremapHook().getProvider(state.getWorld());
        if (provider == null) {
            return;
        }
        provider.add(Position.of(state.getLocation()), SignType.typeOrDefault(state.getType()), front, back);
    }

    public boolean isTracked(BlockState state) {
        SignLayerProvider provider = plugin.squaremapHook().getProvider(state.getWorld());
        if (provider == null) {
            return false;
        }
        return provider.getData(Position.of(state.getLocation())) != null;
    }

    public void checkChunk(Chunk chunk) {
        int minX = chunk.getX();
        int minZ = chunk.getZ();
        int maxX = minX + 16;
        int maxZ = minZ + 16;
        SignLayerProvider provider = plugin.squaremapHook().getProvider(chunk.getWorld());
        if (provider != null) {
            provider.getPositions().forEach(pos -> {
                if (pos.x() >= minX && pos.z() >= minZ &&
                    pos.x() <= maxX && pos.z() <= maxZ &&
                    !pos.isSign(chunk.getWorld())) {
                    provider.remove(pos);
                }
            });
        }
    }

    private void migrateData() {
        final boolean[] migrated = new boolean[]{false};
        this.plugin.squaremapHook().getProviders().forEach((id, provider) -> {
            final UUID uuid = Bukkit.getWorld(BukkitAdapter.namespacedKey(id)).getUID();
            final YamlConfiguration config = new YamlConfiguration();
            File file = new File(this.dataDir, uuid + ".yml");
            try {
                if (file.exists()) {
                    migrated[0] = true;
                    config.load(file);
                } else {
                    return;
                }
            } catch (final IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            config.getKeys(false).forEach(entry -> {
                try {
                    String[] split = entry.split(",");
                    int x = Integer.parseInt(split[0]);
                    int y = Integer.parseInt(split[1]);
                    int z = Integer.parseInt(split[2]);
                    Position pos = Position.of(x, y, z);
                    String[] lines = config.getStringList(entry + ".lines").toArray(new String[0]);
                    final Component[] back = new Component[]{Component.empty(), Component.empty(), Component.empty(), Component.empty()};
                    provider.add(
                        pos,
                        SignType.typeOrDefault(config.getString(entry + ".key").replace("sign_", "")),
                        Arrays.stream(lines).map(s -> LegacyComponentSerializer.legacySection().deserialize(s)).toArray(Component[]::new),
                        back
                    );
                } catch (final Exception e) {
                    this.plugin.getLogger().log(Level.SEVERE, "Could not load " + entry + " from " + uuid, e);
                }
            });
            file.delete();
        });
        if (migrated[0]) {
            this.save();
            this.plugin.squaremapHook().getProviders().forEach((id, provider) -> provider.clear());
        }
    }
}
