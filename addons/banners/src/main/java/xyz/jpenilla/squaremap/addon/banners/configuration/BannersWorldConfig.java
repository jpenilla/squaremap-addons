package xyz.jpenilla.squaremap.addon.banners.configuration;

import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

@SuppressWarnings("unused")
public class BannersWorldConfig extends WorldConfig {
    public boolean enabled = true;
    public int updateInterval = 5;
    public boolean enableControls = true;
    public boolean controlsHiddenByDefault = false;
    public String tooltip = "<center>{name}</center>";

    private BannersWorldConfig(Config<?, ?> parent, String world) {
        super(parent, world);
    }

    private void worldSettings() {
        enabled = getBoolean("enabled", enabled);
        updateInterval = getInt("update-interval", updateInterval);
        enableControls = getBoolean("controls.enabled", enableControls);
        controlsHiddenByDefault = getBoolean("controls.hidden-by-default", controlsHiddenByDefault);
        tooltip = getString("marker.tooltip", tooltip);
    }

    public String layerLabel = "Banners";
    public boolean layerControls = true;
    public boolean layerControlsHidden = false;
    public int layerPriority = 999;
    public int layerZindex = 999;

    private void layerSettings() {
        layerLabel = getString("layer.label", layerLabel);
        layerControls = getBoolean("layer.controls.enabled", layerControls);
        layerControlsHidden = getBoolean("layer.controls.hide-by-default", layerControlsHidden);
        layerPriority = getInt("layer.priority", layerPriority);
        layerZindex = getInt("layer.z-index", layerZindex);
    }

    public int ICON_SIZE = 16;

    private void iconSettings() {
        ICON_SIZE = getInt("icon.size", ICON_SIZE);
    }
}
