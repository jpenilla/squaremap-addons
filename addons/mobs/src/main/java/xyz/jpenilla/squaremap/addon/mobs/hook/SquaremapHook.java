package xyz.jpenilla.squaremap.addon.mobs.hook;

import java.util.HashMap;
import java.util.Map;
import xyz.jpenilla.squaremap.addon.mobs.SquaremapMobs;
import xyz.jpenilla.squaremap.addon.mobs.config.MobsWorldConfig;
import xyz.jpenilla.squaremap.addon.mobs.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public final class SquaremapHook {
    private static final Key MOBS_LAYER_KEY = Key.of("mobs");

    private final Map<WorldIdentifier, SquaremapTask> tasks = new HashMap<>();
    private final SquaremapMobs plugin;

    public SquaremapHook(SquaremapMobs plugin) {
        this.plugin = plugin;
    }

    public void load() {
        for (final MapWorld mapWorld : SquaremapProvider.get().mapWorlds()) {
            final MobsWorldConfig worldConfig = this.plugin.config().worldConfig(mapWorld.identifier());
            if (!worldConfig.enabled) {
                continue;
            }

            final SimpleLayerProvider provider = SimpleLayerProvider.builder(worldConfig.layerLabel)
                .showControls(worldConfig.layerShowControls)
                .defaultHidden(worldConfig.layerControlsHidden)
                .build();
            mapWorld.layerRegistry().register(MOBS_LAYER_KEY, provider);
            final SquaremapTask task = new SquaremapTask(mapWorld, worldConfig, provider);
            task.runTaskTimer(this.plugin, 0, 20L * this.plugin.config().updateInterval);
            this.tasks.put(mapWorld.identifier(), task);
        }
    }

    public void disable() {
        this.tasks.values().forEach(SquaremapTask::disable);
        this.tasks.clear();
    }
}
