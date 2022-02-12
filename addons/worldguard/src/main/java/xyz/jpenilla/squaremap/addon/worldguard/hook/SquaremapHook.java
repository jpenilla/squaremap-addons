package xyz.jpenilla.squaremap.addon.worldguard.hook;

import java.util.HashMap;
import java.util.Map;
import xyz.jpenilla.squaremap.addon.worldguard.SquaremapWorldGuard;
import xyz.jpenilla.squaremap.addon.worldguard.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapHook {
    private static final Key WORLDGUARD_LAYER_KEY = key("worldguard");

    private final Map<WorldIdentifier, SquaremapTask> provider = new HashMap<>();

    public SquaremapHook(SquaremapWorldGuard plugin) {
        SquaremapProvider.get().mapWorlds().forEach(world -> {
            SimpleLayerProvider provider = SimpleLayerProvider.builder(plugin.config().controlLabel)
                .showControls(plugin.config().controlShow)
                .defaultHidden(plugin.config().controlHide)
                .build();
            world.layerRegistry().register(WORLDGUARD_LAYER_KEY, provider);
            SquaremapTask task = new SquaremapTask(plugin, world, provider);
            task.runTaskTimerAsynchronously(plugin, 0, 20L * plugin.config().updateInterval);
            this.provider.put(world.identifier(), task);
        });
    }

    public void disable() {
        this.provider.values().forEach(SquaremapTask::disable);
        this.provider.clear();
    }
}
