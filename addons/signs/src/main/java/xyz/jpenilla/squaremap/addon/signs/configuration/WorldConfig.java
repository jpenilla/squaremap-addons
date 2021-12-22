package xyz.jpenilla.squaremap.addon.signs.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;

@SuppressWarnings("unused")
public final class WorldConfig {
    private static final Map<UUID, WorldConfig> configs = new HashMap<>();

    public static void reload() {
        configs.clear();
        Bukkit.getWorlds().forEach(WorldConfig::get);
    }

    public static WorldConfig get(World world) {
        WorldConfig config = configs.get(world.getUID());
        if (config == null) {
            config = new WorldConfig(world);
            configs.put(world.getUID(), config);
        }
        return config;
    }

    private final World world;
    private final String worldName;

    public WorldConfig(World world) {
        this.world = world;
        this.worldName = world.getName();
        init();
    }

    public World getWorld() {
        return this.world;
    }

    public void init() {
        Config.readConfig(WorldConfig.class, this);
    }

    private void set(String path, Object val) {
        Config.CONFIG.addDefault("world-settings.default." + path, val);
        Config.CONFIG.set("world-settings.default." + path, val);
        if (Config.CONFIG.get("world-settings." + worldName + "." + path) != null) {
            Config.CONFIG.addDefault("world-settings." + worldName + "." + path, val);
            Config.CONFIG.set("world-settings." + worldName + "." + path, val);
        }
    }

    private boolean getBoolean(String path, boolean def) {
        Config.CONFIG.addDefault("world-settings.default." + path, def);
        return Config.CONFIG.getBoolean("world-settings." + worldName + "." + path, Config.CONFIG.getBoolean("world-settings.default." + path));
    }

    private int getInt(String path, int def) {
        Config.CONFIG.addDefault("world-settings.default." + path, def);
        return Config.CONFIG.getInt("world-settings." + worldName + "." + path, Config.CONFIG.getInt("world-settings.default." + path));
    }

    private String getString(String path, String def) {
        Config.CONFIG.addDefault("world-settings.default." + path, def);
        return Config.CONFIG.getString("world-settings." + worldName + "." + path, Config.CONFIG.getString("world-settings.default." + path));
    }

    public boolean ENABLED = true;
    public int UPDATE_INTERVAL = 5;
    public boolean ENABLE_CONTROLS = true;
    public boolean CONTROLS_HIDDEN_BY_DEFAULT = false;
    public String TOOLTIP = "<center>{line1}<br/>{line2}<br/>{line3}<br/>{line4}</center>";

    private void worldSettings() {
        ENABLED = getBoolean("enabled", ENABLED);
        UPDATE_INTERVAL = getInt("update-interval", UPDATE_INTERVAL);
        ENABLE_CONTROLS = getBoolean("controls.enabled", ENABLE_CONTROLS);
        CONTROLS_HIDDEN_BY_DEFAULT = getBoolean("controls.hidden-by-default", CONTROLS_HIDDEN_BY_DEFAULT);
        TOOLTIP = getString("marker.tooltip", TOOLTIP);
    }

    public String LAYER_LABEL = "Signs";
    public boolean LAYER_CONTROLS = true;
    public boolean LAYER_CONTROLS_HIDDEN = false;
    public int LAYER_PRIORITY = 999;
    public int LAYER_ZINDEX = 999;

    private void layerSettings() {
        LAYER_LABEL = getString("layer.label", LAYER_LABEL);
        LAYER_CONTROLS = getBoolean("layer.controls.enabled", LAYER_CONTROLS);
        LAYER_CONTROLS_HIDDEN = getBoolean("layer.controls.hide-by-default", LAYER_CONTROLS_HIDDEN);
        LAYER_PRIORITY = getInt("layer.priority", LAYER_PRIORITY);
        LAYER_ZINDEX = getInt("layer.z-index", LAYER_ZINDEX);
    }

    public int ICON_SIZE = 16;

    private void iconSettings() {
        ICON_SIZE = getInt("icon.size", ICON_SIZE);
    }
}
