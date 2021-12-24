package xyz.jpenilla.squaremap.addon.essentialsx.config;

import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

@SuppressWarnings("unused")
public final class EssXWorldConfig extends WorldConfig {
    public boolean enabled = true;

    private void worldSettings() {
        this.enabled = this.getBoolean("enabled", enabled);
    }

    public String warpsLabel = "Warps";
    public boolean warpsShowControls = true;
    public boolean warpsControlsHidden = false;
    public int warpsPriority = 999;
    public int warpsZindex = 999;
    public String warpsTooltip = "{warp}";

    private void layerSettings() {
        this.warpsLabel = this.getString("warps.label", this.warpsLabel);
        this.warpsShowControls = this.getBoolean("warps.show-controls", this.warpsShowControls);
        this.warpsControlsHidden = this.getBoolean("warps.hide-by-default", this.warpsControlsHidden);
        this.warpsPriority = this.getInt("warps.priority", this.warpsPriority);
        this.warpsZindex = this.getInt("warps.z-index", this.warpsZindex);
        this.warpsTooltip = this.getString("warps.tooltip", this.warpsTooltip);
    }

    public int iconSize = 16;
    public int iconAnchorX = 8;
    public int iconAnchorZ = 16;

    private void iconSettings() {
        this.iconSize = this.getInt("icon.size", this.iconSize);
        this.iconAnchorX = this.getInt("icon.anchor.x", this.iconAnchorX);
        this.iconAnchorZ = this.getInt("icon.anchor.z", this.iconAnchorZ);
    }

    private EssXWorldConfig(final Config<?, ?> parent, final String world) {
        super(parent, world);
    }
}
