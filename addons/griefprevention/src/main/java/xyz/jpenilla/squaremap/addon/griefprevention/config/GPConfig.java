package xyz.jpenilla.squaremap.addon.griefprevention.config;

import java.awt.Color;
import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

public final class GPConfig extends Config<GPConfig, WorldConfig> {
    public String controlLabel = "GriefPrevention";
    public boolean controlShow = true;
    public boolean controlHide = false;
    public int updateInterval = 300;
    public Color strokeColor = Color.GREEN;
    public int strokeWeight = 1;
    public double strokeOpacity = 1.0D;
    public Color fillColor = Color.GREEN;
    public double fillOpacity = 0.2D;
    public String stringsPublic = "Public";
    public int zIndex = 99;
    public int layerPriority = 99;
    public String claimTooltip = "Claim Owner: <span style=\"font-weight:bold;\">{owner}</span><br/>" +
        "Permission Trust: <span style=\"font-weight:bold;\">{managers}</span><br/>" +
        "Trust: <span style=\"font-weight:bold;\">{builders}</span><br/>" +
        "Container Trust: <span style=\"font-weight:bold;\">{containers}</span><br/>" +
        "Access Trust: <span style=\"font-weight:bold;\">{accessors}</span>";
    public String adminClaimTooltip = "<span style=\"font-weight:bold;\">Administrator Claim</span><br/>" +
        "Permission Trust: <span style=\"font-weight:bold;\">{managers}</span><br/>" +
        "Trust: <span style=\"font-weight:bold;\">{builders}</span><br/>" +
        "Container Trust: <span style=\"font-weight:bold;\">{containers}</span><br/>" +
        "Access Trust: <span style=\"font-weight:bold;\">{accessors}</span>";

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
        this.stringsPublic = this.getString("settings.strings.public", this.stringsPublic);
        this.claimTooltip = this.getString("settings.region.tooltip.regular-claim", this.claimTooltip);
        this.adminClaimTooltip = this.getString("settings.region.tooltip.admin-claim", this.adminClaimTooltip);
        this.zIndex = this.getInt("settings.control.z-index", this.zIndex);
        this.layerPriority = this.getInt("settings.control.layer-priority", this.layerPriority);
    }

    public GPConfig(Plugin plugin) {
        super(GPConfig.class, plugin);
    }
}
