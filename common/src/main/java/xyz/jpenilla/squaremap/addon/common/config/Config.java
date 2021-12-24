package xyz.jpenilla.squaremap.addon.common.config;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

@SuppressWarnings("unused")
public abstract class Config<C extends Config<C, W>, W extends WorldConfig> {
    private static final String CONFIG_FILE_NAME = "config.yml";

    private final Class<C> configClass;
    private final File configFile;
    private final @Nullable Class<W> worldConfigClass;
    private final @Nullable Map<WorldIdentifier, W> worldConfigs;
    YamlConfiguration config;
    //static int VERSION;

    protected Config(
        final Class<C> configClass,
        final Plugin plugin
    ) {
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
        this.configFile = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);
    }

    public final void reload() {
        this.config = new YamlConfiguration();
        try {
            this.config.load(this.configFile);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            throw new RuntimeException("Could not load config.yml, please correct your syntax errors", ex);
        }
        this.config.options().copyDefaults(true);

        //VERSION = getInt("config-version", 1);
        //set("config-version", 1);

        this.readConfig(this.configClass, this);

        if (this.worldConfigs != null) {
            this.worldConfigs.clear();
        }
    }

    public W worldConfig(final WorldIdentifier identifier) {
        if (this.worldConfigs == null || this.worldConfigClass == null) {
            throw new IllegalArgumentException("No world config");
        }

        return this.worldConfigs.computeIfAbsent(identifier, id -> {
            final String name = SquaremapProvider.get().getWorldIfEnabled(identifier)
                .map(MapWorld::name) // use names for now
                .orElseThrow();

            try {
                final Constructor<W> ctr = this.worldConfigClass.getDeclaredConstructor(Config.class, String.class);
                ctr.setAccessible(true);
                final W worldConfig = ctr.newInstance(this, name);
                this.readConfig(this.worldConfigClass, worldConfig);
                return worldConfig;
            } catch (final ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        });
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

        try {
            this.config.save(this.configFile);
        } catch (IOException ex) {
            throw new RuntimeException("Could not save " + this.configFile, ex);
        }
    }

    protected void set(String path, Object val) {
        this.config.addDefault(path, val);
        this.config.set(path, val);
    }

    protected String getString(String path, String def) {
        this.config.addDefault(path, def);
        return this.config.getString(path, this.config.getString(path));
    }

    protected boolean getBoolean(String path, boolean def) {
        this.config.addDefault(path, def);
        return this.config.getBoolean(path, this.config.getBoolean(path));
    }

    protected int getInt(String path, int def) {
        this.config.addDefault(path, def);
        return this.config.getInt(path, this.config.getInt(path));
    }

    protected double getDouble(String path, double def) {
        this.config.addDefault(path, def);
        return this.config.getDouble(path, this.config.getDouble(path));
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(String path, List<T> def) {
        this.config.addDefault(path, def);
        return (List<T>) this.config.getList(path, this.config.getList(path));
    }

    protected Color getColor(String path, Color def) {
        this.config.addDefault(path, colorToHex(def));
        return hexToColor(this.config.getString(path, this.config.getString(path)));
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
}
