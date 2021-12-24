package xyz.jpenilla.squaremap.addon.common.config;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

@SuppressWarnings("unused")
public abstract class Config<C extends Config<C, W>, W extends WorldConfig> {
    private static final String CONFIG_FILE_NAME = "config.yml";

    private final Class<C> configClass;
    private final Path configFile;
    private final @Nullable Class<W> worldConfigClass;
    private final @Nullable Map<WorldIdentifier, W> worldConfigs;
    ConfigurationNode config;
    //static int VERSION;

    protected Config(final Class<C> configClass, final Plugin plugin) {
        this(configClass, null, plugin);
    }

    protected Config(
        final Class<C> configClass,
        final @Nullable Class<W> worldConfigClass,
        final Plugin plugin
    ) {
        this.configClass = configClass;
        this.worldConfigClass = worldConfigClass;
        this.worldConfigs = worldConfigClass == null ? null : new ConcurrentHashMap<>();
        this.configFile = plugin.getDataFolder().toPath().resolve(CONFIG_FILE_NAME);
    }

    public final void reload() {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .path(this.configFile)
            .nodeStyle(NodeStyle.BLOCK)
            .build();

        try {
            this.config = loader.load();
        } catch (IOException ex) {
            throw new RuntimeException("Could not load config.yml, exception occurred (are there syntax errors?)", ex);
        }
        //this.config.options().copyDefaults(true);

        //VERSION = getInt("config-version", 1);
        //set("config-version", 1);

        this.readConfig(this.configClass, this);

        if (this.worldConfigs != null) {
            final Set<WorldIdentifier> removed = new HashSet<>(this.worldConfigs.keySet());
            this.worldConfigs.clear();

            if (removed.isEmpty()) {
                this.createWorldConfig("fake-world-999999999999999"); // load default section (dum but works)
            } else {
                // re-load previously loaded
                for (final WorldIdentifier identifier : removed) {
                    this.worldConfig(identifier);
                }
            }
        }

        try {
            loader.save(this.config);
        } catch (IOException ex) {
            throw new RuntimeException("Could not save " + this.configFile, ex);
        }
    }

    public final W worldConfig(final WorldIdentifier identifier) {
        if (this.worldConfigs == null || this.worldConfigClass == null) {
            throw new IllegalArgumentException("No world config");
        }

        return this.worldConfigs.computeIfAbsent(identifier, id -> {
            final String name = SquaremapProvider.get().getWorldIfEnabled(identifier)
                .map(MapWorld::name) // use names for now
                .orElseThrow();

            return this.createWorldConfig(name);
        });
    }

    private W createWorldConfig(String id) {
        if (this.worldConfigs == null || this.worldConfigClass == null) {
            throw new IllegalArgumentException("No world config");
        }

        try {
            final Constructor<W> ctr = this.worldConfigClass.getDeclaredConstructor(Config.class, String.class);
            ctr.setAccessible(true);
            final W worldConfig = ctr.newInstance(this, id);
            this.readConfig(this.worldConfigClass, worldConfig);
            return worldConfig;
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void readConfig(Class<?> clazz, Object instance) {
        for (final Method method : clazz.getDeclaredMethods()) {
            if (!Modifier.isPrivate(method.getModifiers()) || method.getParameterTypes().length != 0 || method.getReturnType() != Void.TYPE) {
                continue;
            }

            try {
                method.setAccessible(true);
                method.invoke(instance);
            } catch (Exception ex) {
                throw new RuntimeException("Error invoking " + method, ex);
            }
        }
    }

    protected final String getString(String path, String def) {
        return this.config.node((Object[]) splitPath(path)).getString(def);
    }

    protected final boolean getBoolean(String path, boolean def) {
        return this.config.node((Object[]) splitPath(path)).getBoolean(def);
    }

    protected final int getInt(String path, int def) {
        return this.config.node((Object[]) splitPath(path)).getInt(def);
    }

    protected final double getDouble(String path, double def) {
        return this.config.node((Object[]) splitPath(path)).getDouble(def);
    }

    protected final <T> List<T> getList(Class<T> elementType, String path, List<T> def) {
        try {
            return this.config.node((Object[]) splitPath(path)).getList(elementType, def);
        } catch (SerializationException e) {
            throw rethrow(e);
        }
    }

    protected final Color getColor(String path, Color def) {
        return hexToColor(this.getString(path, colorToHex(def)));
    }

    private static String colorToHex(final Color color) {
        return Integer.toHexString(color.getRGB() & 0x00FFFFFF);
    }

    private static Color hexToColor(final String hex) {
        if (hex == null) {
            return Color.RED;
        }
        String stripped = hex.replace("#", "");
        int rgb = (int) Long.parseLong(stripped, 16);
        return new Color(rgb);
    }

    static String[] splitPath(final String path) {
        return path.split("\\.");
    }

    @SuppressWarnings("unchecked")
    static <X extends Throwable> RuntimeException rethrow(final Throwable t) throws X {
        throw (X) t;
    }
}
