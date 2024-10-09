package xyz.jpenilla.squaremap.addon.claimchunk.config;

import java.awt.Color;
import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

public final class ClaimChunkConfig extends Config<ClaimChunkConfig, WorldConfig> {
    public String controlLabel = "ClaimChunk";
    public boolean controlShow = true;
    public boolean controlHide = false;
    public int updateInterval = 300;
    public Color strokeColor = Color.RED;
    public int strokeWeight = 2;
    public double strokeOpacity = 0.75D;
    public Color fillColor = Color.RED;
    public double fillOpacity = 0.2D;
    public String claimTooltip = "{name}";
    public boolean showChunks = false;

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
        this.claimTooltip = this.getString("settings.claim.tooltip", this.claimTooltip);
        this.showChunks = this.getBoolean("settings.claim.show-chunks", this.showChunks);
    }

    public ClaimChunkConfig(final Plugin plugin) {
        super(ClaimChunkConfig.class, plugin);
    }
}
