package xyz.jpenilla.squaremap.addon.claimchunk.hook;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.claimchunk.configuration.Config;
import xyz.jpenilla.squaremap.addon.claimchunk.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapHook {
    private static final Key CLAIM_CHUNK_LAYER_KEY = key("claimchunk");

    private final Map<WorldIdentifier, SquaremapTask> tasks = new HashMap<>();

    public SquaremapHook(final Plugin plugin) {
        for (final MapWorld world : SquaremapProvider.get().mapWorlds()) {
            final SimpleLayerProvider provider = SimpleLayerProvider.builder(Config.CONTROL_LABEL)
                .showControls(Config.CONTROL_SHOW)
                .defaultHidden(Config.CONTROL_HIDE)
                .build();

            world.layerRegistry().register(CLAIM_CHUNK_LAYER_KEY, provider);

            final SquaremapTask task = new SquaremapTask(world, provider);
            task.runTaskTimerAsynchronously(plugin, 20L, 20L * Config.UPDATE_INTERVAL);

            this.tasks.put(world.identifier(), task);
        }
    }

    public void disable() {
        this.tasks.values().forEach(SquaremapTask::disable);
        this.tasks.clear();
    }
}
