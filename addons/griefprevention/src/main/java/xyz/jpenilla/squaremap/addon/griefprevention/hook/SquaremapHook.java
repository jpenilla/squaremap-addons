package xyz.jpenilla.squaremap.addon.griefprevention.hook;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import xyz.jpenilla.squaremap.addon.griefprevention.SquaremapGriefPrevention;
import xyz.jpenilla.squaremap.addon.griefprevention.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public final class SquaremapHook {
    private static final Key GP_LAYER_KEY = Key.of("griefprevention");

    private final Map<WorldIdentifier, SquaremapTask> tasks = new HashMap<>();

    public SquaremapHook(SquaremapGriefPrevention plugin) {
        for (final MapWorld world : SquaremapProvider.get().mapWorlds()) {
            final World bukkitWorld = BukkitAdapter.bukkitWorld(world);
            if (!GPHook.isWorldEnabled(bukkitWorld.getUID())) {
                continue;
            }

            SimpleLayerProvider provider = SimpleLayerProvider
                .builder(plugin.config().controlLabel)
                .showControls(plugin.config().controlShow)
                .defaultHidden(plugin.config().controlHide)
                .zIndex(plugin.config().zIndex)
                .layerPriority(plugin.config().layerPriority)
                .build();
            world.layerRegistry().register(GP_LAYER_KEY, provider);
            SquaremapTask task = new SquaremapTask(plugin, world, provider);
            task.runTaskTimerAsynchronously(plugin, 0, 20L * plugin.config().updateInterval);
            this.tasks.put(world.identifier(), task);
        }
    }

    public void disable() {
        this.tasks.values().forEach(SquaremapTask::disable);
        SquaremapProvider.get().mapWorlds().forEach(w -> {
            if (w.layerRegistry().hasEntry(GP_LAYER_KEY)) {
                w.layerRegistry().unregister(GP_LAYER_KEY);
            }
        });
        this.tasks.clear();
    }
}
