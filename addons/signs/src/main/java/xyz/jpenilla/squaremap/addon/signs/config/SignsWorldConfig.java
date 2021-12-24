package xyz.jpenilla.squaremap.addon.signs.config;

import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

@SuppressWarnings("unused")
public final class SignsWorldConfig extends WorldConfig {
    public boolean enabled = true;
    public int updateInterval = 5;
    public boolean enableControls = true;
    public boolean controlsHiddenByDefault = false;
    public String tooltip = "<center>{line1}<br/>{line2}<br/>{line3}<br/>{line4}</center>";

    private void worldSettings() {
        this.enabled = this.getBoolean("enabled", this.enabled);
        this.updateInterval = this.getInt("update-interval", this.updateInterval);
        this.enableControls = this.getBoolean("controls.enabled", this.enableControls);
        this.controlsHiddenByDefault = this.getBoolean("controls.hidden-by-default", this.controlsHiddenByDefault);
        this.tooltip = this.getString("marker.tooltip", this.tooltip);
    }

    public String layerLabel = "Signs";
    public boolean layerControls = true;
    public boolean layerControlsHidden = false;
    public int layerPriority = 999;
    public int layerZindex = 999;

    private void layerSettings() {
        this.layerLabel = this.getString("layer.label", this.layerLabel);
        this.layerControls = this.getBoolean("layer.controls.enabled", this.layerControls);
        this.layerControlsHidden = this.getBoolean("layer.controls.hide-by-default", this.layerControlsHidden);
        this.layerPriority = this.getInt("layer.priority", this.layerPriority);
        this.layerZindex = this.getInt("layer.z-index", this.layerZindex);
    }

    public int iconSize = 16;

    private void iconSettings() {
        this.iconSize = getInt("icon.size", this.iconSize);
    }

    private SignsWorldConfig(Config<?, ?> parent, String world) {
        super(parent, world);
    }
}
