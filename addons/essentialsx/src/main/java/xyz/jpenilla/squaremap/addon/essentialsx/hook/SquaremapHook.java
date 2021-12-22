package xyz.jpenilla.squaremap.addon.essentialsx.hook;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import xyz.jpenilla.squaremap.addon.essentialsx.SquaremapEssentials;
import xyz.jpenilla.squaremap.addon.essentialsx.configuration.Config;
import xyz.jpenilla.squaremap.addon.essentialsx.configuration.WorldConfig;
import xyz.jpenilla.squaremap.addon.essentialsx.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapHook {
    public static final Key WARP_ICON_KEY = key("essentials_warp_icon");

    private final Map<WorldIdentifier, SquaremapTask> tasks = new HashMap<>();
    private final SquaremapEssentials plugin;

    public SquaremapHook(SquaremapEssentials squaremapEssentials) {
        this.plugin = squaremapEssentials;
    }

    public void load() {
        try {
            BufferedImage icon = ImageIO.read(new File(this.plugin.getDataFolder(), "warp.png"));
            SquaremapProvider.get().iconRegistry().register(WARP_ICON_KEY, icon);
        } catch (IOException e) {
            throw new RuntimeException("Failed to register warp icon", e);
        }

        for (final MapWorld mapWorld : SquaremapProvider.get().mapWorlds()) {
            WorldConfig worldConfig = WorldConfig.get(mapWorld);
            if (!worldConfig.ENABLED) {
                continue;
            }

            SimpleLayerProvider provider = SimpleLayerProvider.builder(worldConfig.WARPS_LABEL)
                .showControls(worldConfig.WARPS_SHOW_CONTROLS)
                .defaultHidden(worldConfig.WARPS_CONTROLS_HIDDEN)
                .build();
            mapWorld.layerRegistry().register(Key.of("essentials_warps"), provider);
            SquaremapTask task = new SquaremapTask(mapWorld, worldConfig, provider);
            task.runTaskTimerAsynchronously(this.plugin, 0, 20L * Config.UPDATE_INTERVAL);
            this.tasks.put(mapWorld.identifier(), task);
        }
    }

    public void disable() {
        this.tasks.values().forEach(SquaremapTask::disable);
        this.tasks.clear();
    }
}
