package xyz.jpenilla.squaremap.addon.banners.configuration;

import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.addon.common.config.Config;

@DefaultQualifier(NonNull.class)
public class BannersConfig extends Config<BannersConfig, BannersWorldConfig> {
    public BannersConfig(final Plugin plugin) {
        super(BannersConfig.class, BannersWorldConfig.class, plugin);
    }

    public boolean debugMode = false;

    private void baseSettings() {
        this.debugMode = this.getBoolean("settings.debug-mode", this.debugMode);
    }
}
