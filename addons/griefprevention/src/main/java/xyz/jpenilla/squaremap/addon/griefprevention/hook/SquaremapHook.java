package xyz.jpenilla.squaremap.addon.griefprevention.hook;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.griefprevention.configuration.Config;
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

    public SquaremapHook(Plugin plugin) {
        for (final MapWorld world : SquaremapProvider.get().mapWorlds()) {
            final World bukkitWorld = BukkitAdapter.bukkitWorld(world);
            if (!GPHook.isWorldEnabled(bukkitWorld.getUID())) {
                continue;
            }

            SimpleLayerProvider provider = SimpleLayerProvider
                .builder(Config.CONTROL_LABEL)
                .showControls(Config.CONTROL_SHOW)
                .defaultHidden(Config.CONTROL_HIDE)
                .build();
            world.layerRegistry().register(GP_LAYER_KEY, provider);
            SquaremapTask task = new SquaremapTask(world, provider);
            task.runTaskTimerAsynchronously(plugin, 0, 20L * Config.UPDATE_INTERVAL);
            this.tasks.put(world.identifier(), task);
        }
    }

    public void disable() {
        this.tasks.values().forEach(SquaremapTask::disable);
        this.tasks.clear();
    }
}
