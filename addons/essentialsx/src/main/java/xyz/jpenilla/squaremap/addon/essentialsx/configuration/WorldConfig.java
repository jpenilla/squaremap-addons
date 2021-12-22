package xyz.jpenilla.squaremap.addon.essentialsx.configuration;

import java.util.HashMap;
import java.util.Map;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

@SuppressWarnings("unused")
public final class WorldConfig {
    private static final Map<WorldIdentifier, WorldConfig> configs = new HashMap<>();

    public static void reload() {
        configs.clear();
    }

    public static WorldConfig get(MapWorld world) {
        WorldConfig config = configs.get(world.identifier());
        if (config == null) {
            config = new WorldConfig(world);
            configs.put(world.identifier(), config);
        }
        return config;
    }

    private final String worldName;

    public WorldConfig(MapWorld world) {
        this.worldName = world.name();
        init();
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

    private void worldSettings() {
        ENABLED = getBoolean("enabled", ENABLED);
    }

    public String WARPS_LABEL = "Warps";
    public boolean WARPS_SHOW_CONTROLS = true;
    public boolean WARPS_CONTROLS_HIDDEN = false;
    public int WARPS_PRIORITY = 999;
    public int WARPS_ZINDEX = 999;
    public String WARPS_TOOLTIP = "{warp}";

    private void layerSettings() {
        WARPS_LABEL = getString("warps.label", WARPS_LABEL);
        WARPS_SHOW_CONTROLS = getBoolean("warps.show-controls", WARPS_SHOW_CONTROLS);
        WARPS_CONTROLS_HIDDEN = getBoolean("warps.hide-by-default", WARPS_CONTROLS_HIDDEN);
        WARPS_PRIORITY = getInt("warps.priority", WARPS_PRIORITY);
        WARPS_ZINDEX = getInt("warps.z-index", WARPS_ZINDEX);
        WARPS_TOOLTIP = getString("warps.tooltip", WARPS_TOOLTIP);
    }

    public int ICON_SIZE = 16;
    public int ICON_ANCHOR_X = 8;
    public int ICON_ANCHOR_Z = 16;

    private void iconSettings() {
        ICON_SIZE = getInt("icon.size", ICON_SIZE);
        ICON_ANCHOR_X = getInt("icon.anchor.x", ICON_ANCHOR_X);
        ICON_ANCHOR_Z = getInt("icon.anchor.z", ICON_ANCHOR_Z);
    }
}
