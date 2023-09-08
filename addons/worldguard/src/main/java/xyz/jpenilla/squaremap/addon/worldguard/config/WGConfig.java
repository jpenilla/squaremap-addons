package xyz.jpenilla.squaremap.addon.worldguard.config;

import java.util.ArrayList;
import java.util.List;
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

        final ConfigurationTransformation twoToThree = ConfigurationTransformation.builder()
            .addAction(NodePath.path("world-settings", "default", "flags", "list"), (path, node) -> {
                final List<String> list = new ArrayList<>(node.getList(String.class, List.of()));
                list.addAll(List.of(
                    "squaremap-stroke-color",
                    "squaremap-stroke-weight",
                    "squaremap-stroke-opacity",
                    "squaremap-fill-color",
                    "squaremap-fill-opacity",
                    "squaremap-click-tooltip"
                ));
                node.setList(String.class, list);
                return null;
            })
            .build();

        versionedBuilder.addVersion(1, zeroToOne)
            .addVersion(2, oneToTwo)
            .addVersion(3, twoToThree);
    }
}
