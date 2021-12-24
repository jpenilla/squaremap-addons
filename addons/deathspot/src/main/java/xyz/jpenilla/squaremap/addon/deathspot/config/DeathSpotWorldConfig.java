package xyz.jpenilla.squaremap.addon.deathspot.config;

import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

public final class DeathSpotWorldConfig extends WorldConfig {
    public boolean enabled = true;
    public int updateInterval = 5;
    public boolean enableControls = true;
    public boolean controlsHiddenByDefault = false;
    public String tooltip = "{name}'s<br/>Death Spot";
    public int removeMarkerAfter = 60 * 5;

    @SuppressWarnings("unused")
    private void worldSettings() {
        this.enabled = this.getBoolean("enabled", this.enabled);
        this.updateInterval = this.getInt("update-interval", this.updateInterval);
        this.enableControls = this.getBoolean("controls.enabled", this.enableControls);
        this.controlsHiddenByDefault = this.getBoolean("controls.hidden-by-default", this.controlsHiddenByDefault);
        this.tooltip = this.getString("marker.tooltip", this.tooltip);
        this.removeMarkerAfter = this.getInt("marker.remove-after", this.removeMarkerAfter);
    }

    private DeathSpotWorldConfig(final Config<?, ?> parent, final String world) {
        super(parent, world);
    }
}
