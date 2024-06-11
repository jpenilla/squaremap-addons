package xyz.jpenilla.squaremap.addon.essentialsx.hook;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import xyz.jpenilla.squaremap.addon.essentialsx.SquaremapEssentials;
import xyz.jpenilla.squaremap.addon.essentialsx.config.EssXWorldConfig;
import xyz.jpenilla.squaremap.addon.essentialsx.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapHook {
    public static final Key WARP_LAYER_KEY = key("essentials_warps");
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
            EssXWorldConfig worldConfig = this.plugin.config().worldConfig(mapWorld.identifier());
            if (!worldConfig.enabled) {
                continue;
            }

            SimpleLayerProvider provider = SimpleLayerProvider.builder(worldConfig.warpsLabel)
                .showControls(worldConfig.warpsShowControls)
                .defaultHidden(worldConfig.warpsControlsHidden)
                .build();
            mapWorld.layerRegistry().register(WARP_LAYER_KEY, provider);
            SquaremapTask task = new SquaremapTask(mapWorld, worldConfig, provider);
            task.runTaskTimerAsynchronously(this.plugin, 0, 20L * this.plugin.config().updateInterval);
            this.tasks.put(mapWorld.identifier(), task);
        }
    }

    public void disable() {
        this.tasks.values().forEach(SquaremapTask::disable);
        SquaremapProvider.get().mapWorlds().forEach(w -> {
            if (w.layerRegistry().hasEntry(WARP_LAYER_KEY)) {
                w.layerRegistry().unregister(WARP_LAYER_KEY);
            }
        });
        this.tasks.clear();
    }
}
