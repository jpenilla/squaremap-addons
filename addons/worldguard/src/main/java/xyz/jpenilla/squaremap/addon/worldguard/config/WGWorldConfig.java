package xyz.jpenilla.squaremap.addon.worldguard.config;

import io.leangen.geantyref.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.ListMode;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;

public class WGWorldConfig extends WorldConfig {
    public String controlLabel = "WorldGuard";
    public boolean controlShow = true;
    public boolean controlHide = false;
    public int updateInterval = 300;
    public StyleSettings defaultStyle = StyleSettings.defaultSettings();
    public ListMode listMode = ListMode.BLACKLIST;
    public List<String> regionList = new ArrayList<>();
    public ListMode flagListMode = ListMode.BLACKLIST;
    public List<String> flagList = List.of(
        "squaremap-visible",
        "squaremap-stroke-color",
        "squaremap-stroke-weight",
        "squaremap-stroke-opacity",
        "squaremap-fill-color",
        "squaremap-fill-opacity",
        "squaremap-click-tooltip"
    );
    public Map<String, StyleSettings> styleOverrides = new HashMap<>();

    @SuppressWarnings("unused")
    private void init() {
        this.controlLabel = this.getString("control.label", this.controlLabel);
        this.controlShow = this.getBoolean("control.show", this.controlShow);
        this.controlHide = this.getBoolean("control.hide-by-default", this.controlHide);
        this.updateInterval = this.getInt("update-interval", this.updateInterval);
        this.defaultStyle = this.get("default-style", StyleSettings.class, this.defaultStyle);
        this.defaultStyle.validate();
        this.listMode = this.getEnum("regions.list-mode", ListMode.class, this.listMode);
        this.regionList = this.getList(String.class, "regions.list", this.regionList);
        this.flagListMode = this.getEnum("flags.list-mode", ListMode.class, this.flagListMode);
        this.flagList = this.getList(String.class, "flags.list", this.flagList);
        this.styleOverrides = this.get("style-overrides", new TypeToken<Map<String, StyleSettings>>() {}.getType(), this.styleOverrides);
    }

    protected WGWorldConfig(final Config<?, ?> parent, final String world) {
        super(parent, world);
    }
}
