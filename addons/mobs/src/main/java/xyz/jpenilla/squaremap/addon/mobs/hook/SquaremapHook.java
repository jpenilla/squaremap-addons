package xyz.jpenilla.squaremap.addon.mobs.hook;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.mobs.configuration.Config;
import xyz.jpenilla.squaremap.addon.mobs.configuration.WorldConfig;
import xyz.jpenilla.squaremap.addon.mobs.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public final class SquaremapHook {
    private static final Key MOBS_LAYER_KEY = Key.of("mobs");

    private final Map<WorldIdentifier, SquaremapTask> tasks = new HashMap<>();
    private final JavaPlugin plugin;

    public SquaremapHook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        for (final MapWorld mapWorld : SquaremapProvider.get().mapWorlds()) {
            final WorldConfig worldConfig = WorldConfig.get(mapWorld);
            if (!worldConfig.ENABLED) {
                continue;
            }

            final SimpleLayerProvider provider = SimpleLayerProvider.builder(worldConfig.LAYER_LABEL)
                .showControls(worldConfig.LAYER_SHOW_CONTROLS)
                .defaultHidden(worldConfig.LAYER_CONTROLS_HIDDEN)
                .build();
            mapWorld.layerRegistry().register(MOBS_LAYER_KEY, provider);
            final SquaremapTask task = new SquaremapTask(mapWorld, worldConfig, provider);
            task.runTaskTimer(this.plugin, 0, 20L * Config.UPDATE_INTERVAL);
            this.tasks.put(mapWorld.identifier(), task);
        }
    }

    public void disable() {
        this.tasks.values().forEach(SquaremapTask::disable);
        this.tasks.clear();
    }
}
