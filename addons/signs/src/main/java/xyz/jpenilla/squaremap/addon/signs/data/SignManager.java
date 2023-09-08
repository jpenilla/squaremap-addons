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
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.squaremap.addon.signs.SignsPlugin;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public final class SignManager {
    private final SignsPlugin plugin;
    private final File dataDir;
    private final Gson gson = GsonComponentSerializer.gson().populator().apply(new GsonBuilder())
        .registerTypeAdapter(Position.class, new Position.TypeAdapter())
        .create();

    public SignManager(final SignsPlugin plugin) {
        this.plugin = plugin;
        this.dataDir = new File(plugin.getDataFolder(), "data");

        if (!this.dataDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            this.dataDir.mkdirs();
        }
    }

    public void load(final WorldIdentifier id, final SignLayerProvider provider) throws IOException {
        this.migrateData(id, provider);

        List<SignMarkerData> data = new ArrayList<>();
        Path file = this.dataFile(id);
        if (Files.isRegularFile(file)) {
            try (final Reader r = Files.newBufferedReader(file)) {
                data = this.gson.fromJson(r, new TypeToken<List<SignMarkerData>>() {}.getType());
            }
        }
        data.forEach(entry -> {
            try {
                provider.add(entry.pos, SignType.typeOrDefault(entry.type), entry.frontLines, entry.backLines);
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, "Could not load " + entry + " from " + id, e);
            }
        });
    }

    private Path dataFile(final WorldIdentifier id) {
        return new File(this.dataDir, id.asString().replace(':', '_') + ".json").toPath();
    }

    public static final class SignMarkerData {
        public Position pos;
        public String type;
        public @Nullable List<Component> frontLines;
        public @Nullable List<Component> backLines;
    }

    public void save(final WorldIdentifier id, final SignLayerProvider provider) {
        final List<SignMarkerData> list = new ArrayList<>();
        provider.getData().forEach((pos, data) -> {
            final SignMarkerData s = new SignMarkerData();
            s.pos = pos;
            s.type = data.type().name();
            s.frontLines = data.front();
            s.backLines = data.back();
            list.add(s);
        });
        try (final Writer w = Files.newBufferedWriter(this.dataFile(id))) {
            this.gson.toJson(list, new TypeToken<List<SignMarkerData>>() {}.getType(), w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeSign(final BlockState state) {
        final @Nullable SignLayerProvider provider = this.plugin.layerProviders().provider(state.getWorld());
        if (provider == null) {
            return;
        }
        provider.remove(Position.of(state.getLocation()));
    }

    public void putSign(@NonNull BlockState state, @NonNull List<Component> front, @NonNull List<Component> back) {
        final @Nullable SignLayerProvider provider = this.plugin.layerProviders().provider(state.getWorld());
        if (provider == null) {
            return;
        }
        provider.add(Position.of(state.getLocation()), SignType.typeOrDefault(state.getType()), List.copyOf(front), List.copyOf(back));
    }

    public boolean isTracked(BlockState state) {
        final @Nullable SignLayerProvider provider = this.plugin.layerProviders().provider(state.getWorld());
        if (provider == null) {
            return false;
        }
        return provider.getData(Position.of(state.getLocation())) != null;
    }

    public void checkChunk(final Chunk chunk) {
        final int minX = chunk.getX();
        final int minZ = chunk.getZ();
        final int maxX = minX + 16;
        final int maxZ = minZ + 16;
        final SignLayerProvider provider = this.plugin.layerProviders().provider(chunk.getWorld());
        if (provider == null) {
            return;
        }
        provider.getPositions().forEach(pos -> {
            if (pos.x() >= minX && pos.z() >= minZ
                && pos.x() <= maxX && pos.z() <= maxZ
                && !pos.isSign(chunk.getWorld())) {
                provider.remove(pos);
            }
        });
    }

    private void migrateData(final WorldIdentifier id, final SignLayerProvider provider) {
        final UUID uuid = Bukkit.getWorld(BukkitAdapter.namespacedKey(id)).getUID();
        final YamlConfiguration config = new YamlConfiguration();
        File file = new File(this.dataDir, uuid + ".yml");
        try {
            if (file.exists()) {
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
                List<String> lines = config.getStringList(entry + ".lines");
                provider.add(
                    pos,
                    SignType.typeOrDefault(config.getString(entry + ".key").replace("sign_", "")),
                    lines.stream().map(s -> LegacyComponentSerializer.legacySection().deserialize(s)).collect(Collectors.toList()),
                    List.of(Component.empty(), Component.empty(), Component.empty(), Component.empty())
                );
            } catch (final Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, "Could not load " + entry + " from " + uuid, e);
            }
        });
        this.save(id, provider);
        provider.clear();
        file.delete();
    }
}
