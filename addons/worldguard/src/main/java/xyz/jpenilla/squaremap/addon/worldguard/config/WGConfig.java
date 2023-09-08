package xyz.jpenilla.squaremap.addon.worldguard.config;

import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;
import xyz.jpenilla.squaremap.addon.common.config.Config;

public final class WGConfig extends Config<WGConfig, WGWorldConfig> {
    public WGConfig(Plugin plugin) {
        super(WGConfig.class, WGWorldConfig.class, plugin);
    }

    @Override
    protected void addVersions(final ConfigurationTransformation.VersionedBuilder versionedBuilder) {
        final ConfigurationTransformation zeroToOne = ConfigurationTransformation.builder()
            .addAction(NodePath.path("settings", "style"), (path, node) -> new Object[]{"settings", "default-style"})
            .addAction(NodePath.path("settings", "region", "tooltip"), (path, node) -> new Object[]{"settings", "default-style", "click-tooltip"})
            .addAction(NodePath.path("settings", "region"), TransformAction.remove())
            .build();

        final ConfigurationTransformation oneToTwo = ConfigurationTransformation.builder()
            .addAction(NodePath.path("settings"), (path, node) -> new Object[]{"world-settings", "default"})
            .build();

        versionedBuilder.addVersion(1, zeroToOne)
            .addVersion(2, oneToTwo);
    }
}
