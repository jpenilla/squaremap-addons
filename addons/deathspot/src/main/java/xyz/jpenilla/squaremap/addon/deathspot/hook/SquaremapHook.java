package xyz.jpenilla.squaremap.addon.deathspot.hook;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import xyz.jpenilla.squaremap.addon.deathspot.DeathSpots;
import xyz.jpenilla.squaremap.addon.deathspot.config.DeathSpotWorldConfig;
import xyz.jpenilla.squaremap.addon.deathspot.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapHook {
    public static final Key DEATH_SPOT_ICON_KEY = key("deathspot_icon");
    private static final Key DEATH_SPOTS_LAYER_KEY = key("deathspots");

    private final Map<WorldIdentifier, SquaremapTask> tasks = new HashMap<>();

    public SquaremapHook(final DeathSpots plugin) {
        try {
            final BufferedImage image = ImageIO.read(new File(plugin.getDataFolder(), "icon.png"));
            SquaremapProvider.get().iconRegistry().register(DEATH_SPOT_ICON_KEY, image);
        } catch (final IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to register deathspot icon", e);
        }

        for (final MapWorld world : SquaremapProvider.get().mapWorlds()) {
            final DeathSpotWorldConfig worldConfig = plugin.config().worldConfig(world.identifier());
            if (!worldConfig.enabled) {
                continue;
            }

            final SimpleLayerProvider provider = SimpleLayerProvider.builder("DeathSpots")
                .showControls(worldConfig.enableControls)
                .defaultHidden(worldConfig.controlsHiddenByDefault)
                .build();
            world.layerRegistry().register(DEATH_SPOTS_LAYER_KEY, provider);

            final SquaremapTask task = new SquaremapTask(plugin, world, provider);
            task.runTaskTimerAsynchronously(plugin, 0, 20L * worldConfig.updateInterval);

            this.tasks.put(world.identifier(), task);
        }
    }

    public void disable() {
        this.tasks.values().forEach(SquaremapTask::disable);
        this.tasks.clear();
    }
}
