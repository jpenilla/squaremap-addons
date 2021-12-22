package xyz.jpenilla.squaremap.addon.griefprevention.configuration;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public final class Config {
    public static String CONTROL_LABEL = "GriefPrevention";
    public static boolean CONTROL_SHOW = true;
    public static boolean CONTROL_HIDE = false;
    public static int UPDATE_INTERVAL = 300;
    public static Color STROKE_COLOR = Color.GREEN;
    public static int STROKE_WEIGHT = 1;
    public static double STROKE_OPACITY = 1.0D;
    public static Color FILL_COLOR = Color.GREEN;
    public static double FILL_OPACITY = 0.2D;
    public static String STRINGS_PUBLIC = "Public";
    public static String CLAIM_TOOLTIP = "Claim Owner: <span style=\"font-weight:bold;\">{owner}</span><br/>" +
            "Permission Trust: <span style=\"font-weight:bold;\">{managers}</span><br/>" +
            "Trust: <span style=\"font-weight:bold;\">{builders}</span><br/>" +
            "Container Trust: <span style=\"font-weight:bold;\">{containers}</span><br/>" +
            "Access Trust: <span style=\"font-weight:bold;\">{accessors}</span>";
    public static String ADMIN_CLAIM_TOOLTIP = "<span style=\"font-weight:bold;\">Administrator Claim</span><br/>" +
            "Permission Trust: <span style=\"font-weight:bold;\">{managers}</span><br/>" +
            "Trust: <span style=\"font-weight:bold;\">{builders}</span><br/>" +
            "Container Trust: <span style=\"font-weight:bold;\">{containers}</span><br/>" +
            "Access Trust: <span style=\"font-weight:bold;\">{accessors}</span>";

    private static void init() {
        CONTROL_LABEL = getString("settings.control.label", CONTROL_LABEL);
        CONTROL_SHOW = getBoolean("settings.control.show", CONTROL_SHOW);
        CONTROL_HIDE = getBoolean("settings.control.hide-by-default", CONTROL_HIDE);
        UPDATE_INTERVAL = getInt("settings.update-interval", UPDATE_INTERVAL);
        STROKE_COLOR = getColor("settings.style.stroke.color", STROKE_COLOR);
        STROKE_WEIGHT = getInt("settings.style.stroke.weight", STROKE_WEIGHT);
        STROKE_OPACITY = getDouble("settings.style.stroke.opacity", STROKE_OPACITY);
        FILL_COLOR = getColor("settings.style.fill.color", FILL_COLOR);
        FILL_OPACITY = getDouble("settings.style.fill.opacity", FILL_OPACITY);
        STRINGS_PUBLIC = getString("settings.strings.public", STRINGS_PUBLIC);
        CLAIM_TOOLTIP = getString("settings.region.tooltip.regular-claim", CLAIM_TOOLTIP);
        ADMIN_CLAIM_TOOLTIP = getString("settings.region.tooltip.admin-claim", ADMIN_CLAIM_TOOLTIP);
    }

    public static void reload(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "config.yml");
        yaml = new YamlConfiguration();

        try {
            yaml.load(file);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            plugin.getLogger().severe("Could not load config.yml, please correct your syntax errors");
            throw new RuntimeException(ex);
        }

        yaml.options().copyDefaults(true);

        init();

        try {
            yaml.save(file);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save " + file);
            ex.printStackTrace();
        }
    }

    private static YamlConfiguration yaml;

    private static String getString(String path, String def) {
        yaml.addDefault(path, def);
        return yaml.getString(path, yaml.getString(path));
    }

    private static boolean getBoolean(String path, boolean def) {
        yaml.addDefault(path, def);
        return yaml.getBoolean(path, yaml.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        yaml.addDefault(path, def);
        return yaml.getInt(path, yaml.getInt(path));
    }

    private static double getDouble(String path, double def) {
        yaml.addDefault(path, def);
        return yaml.getDouble(path, yaml.getDouble(path));
    }

    private static Color getColor(String path, Color def) {
        yaml.addDefault(path, colorToHex(def));
        return hexToColor(yaml.getString(path, yaml.getString(path)));
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
