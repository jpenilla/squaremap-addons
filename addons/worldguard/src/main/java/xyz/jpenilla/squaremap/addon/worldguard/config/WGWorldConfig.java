package xyz.jpenilla.squaremap.addon.worldguard.config;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.ListMode;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

public class WGWorldConfig extends WorldConfig {
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
    public ListMode flagListMode = ListMode.BLACKLIST;
    public List<String> flagList = List.of("squaremap-visible");

    @SuppressWarnings("unused")
    private void init() {
        this.controlLabel = this.getString("control.label", this.controlLabel);
        this.controlShow = this.getBoolean("control.show", this.controlShow);
        this.controlHide = this.getBoolean("control.hide-by-default", this.controlHide);
        this.updateInterval = this.getInt("update-interval", this.updateInterval);
        this.strokeColor = this.getColor("default-style.stroke.color", this.strokeColor);
        this.strokeWeight = this.getInt("default-style.stroke.weight", this.strokeWeight);
        this.strokeOpacity = this.getDouble("default-style.stroke.opacity", this.strokeOpacity);
        this.fillColor = this.getColor("default-style.fill.color", this.fillColor);
        this.fillOpacity = this.getDouble("default-style.fill.opacity", this.fillOpacity);
        this.claimTooltip = this.getString("default-style.click-tooltip", this.claimTooltip);
        this.listMode = this.getEnum("regions.list-mode", ListMode.class, this.listMode);
        this.regionList = this.getList(String.class, "regions.list", this.regionList);
        this.flagListMode = this.getEnum("flags.list-mode", ListMode.class, this.flagListMode);
        this.flagList = this.getList(String.class, "flags.list", this.flagList);
    }

    protected WGWorldConfig(final Config<?, ?> parent, final String world) {
        super(parent, world);
    }
}
