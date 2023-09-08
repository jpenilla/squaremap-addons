package xyz.jpenilla.squaremap.addon.worldguard.hook;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.squaremap.addon.worldguard.SquaremapWorldGuard;
import xyz.jpenilla.squaremap.addon.worldguard.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapHook {
    private static final Key WORLDGUARD_LAYER_KEY = key("worldguard");

    private final Map<WorldIdentifier, SquaremapTask> providers = new HashMap<>();
    private final SquaremapWorldGuard plugin;
    private final Squaremap squaremap;

    public SquaremapHook(final SquaremapWorldGuard plugin) {
        this.plugin = plugin;
        this.squaremap = SquaremapProvider.get();
        this.squaremap.mapWorlds().forEach(this::addWorld);
    }

    public void addWorld(final World world) {
        this.squaremap.getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).ifPresent(this::addWorld);
    }

    private void addWorld(final MapWorld world) {
        this.providers.computeIfAbsent(world.identifier(), id -> {
            SimpleLayerProvider provider = SimpleLayerProvider.builder(plugin.config().controlLabel)
                .showControls(this.plugin.config().controlShow)
                .defaultHidden(this.plugin.config().controlHide)
                .build();
            world.layerRegistry().register(WORLDGUARD_LAYER_KEY, provider);
            SquaremapTask task = new SquaremapTask(this.plugin, id, provider);
            task.runTaskTimerAsynchronously(this.plugin, 0, 20L * this.plugin.config().updateInterval);
            return task;
        });
    }

    public void removeWorld(final World world) {
        this.removeWorld(BukkitAdapter.worldIdentifier(world));
    }

    public void removeWorld(final WorldIdentifier world) {
        final @Nullable SquaremapTask remove = this.providers.remove(world);
        if (remove != null) {
            remove.disable();
        }
        this.squaremap.getWorldIfEnabled(world).ifPresent(mapWorld -> {
            if (mapWorld.layerRegistry().hasEntry(WORLDGUARD_LAYER_KEY)) {
                mapWorld.layerRegistry().unregister(WORLDGUARD_LAYER_KEY);
            }
        });
    }

    public void disable() {
        Map.copyOf(this.providers).keySet().forEach(this::removeWorld);
    }
}
