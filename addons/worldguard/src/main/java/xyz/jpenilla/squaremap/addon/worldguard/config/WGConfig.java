package xyz.jpenilla.squaremap.addon.worldguard.config;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

public final class WGConfig extends xyz.jpenilla.squaremap.addon.common.config.Config<WGConfig, WorldConfig> {
    public String controlLabel = "WorldGuard";
    public boolean controlShow = true;
    public boolean controlHide = false;
    public int updateInterval = 300;
    public Color strokeColor = Color.GREEN;
    public int strokeWeight = 1;
    public double strokeOpacity = 1.0D;
    public Color fillColor = Color.GREEN;
    public double fillOpacity = 0.2D;
    public String claimTooltip = "<span style=\"font-size:120%;\">{regionname}</span><br />" +
        "Owner <span style=\"font-weight:bold;\">{playerowners}</span><br />" +
        "Flags<br /><span style=\"font-weight:bold;\">{flags}</span>";
    public ListMode listMode = ListMode.BLACKLIST;
    public List<String> regionList = new ArrayList<>();

    @SuppressWarnings("unused")
    private void init() {
        this.controlLabel = this.getString("settings.control.label", this.controlLabel);
        this.controlShow = this.getBoolean("settings.control.show", this.controlShow);
        this.controlHide = this.getBoolean("settings.control.hide-by-default", this.controlHide);
        this.updateInterval = this.getInt("settings.update-interval", this.updateInterval);
        this.strokeColor = this.getColor("settings.style.stroke.color", this.strokeColor);
        this.strokeWeight = this.getInt("settings.style.stroke.weight", this.strokeWeight);
        this.strokeOpacity = this.getDouble("settings.style.stroke.opacity", this.strokeOpacity);
        this.fillColor = this.getColor("settings.style.fill.color", this.fillColor);
        this.fillOpacity = this.getDouble("settings.style.fill.opacity", this.fillOpacity);
        this.claimTooltip = this.getString("settings.region.tooltip", this.claimTooltip);
        this.listMode = this.getEnum("settings.regions.list-mode", ListMode.class, this.listMode);
        this.regionList = this.getList(String.class, "settings.regions.list", this.regionList);
    }

    public enum ListMode {
        WHITELIST,
        BLACKLIST
    }

    public WGConfig(Plugin plugin) {
        super(WGConfig.class, plugin);
    }
}
