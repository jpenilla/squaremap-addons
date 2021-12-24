package xyz.jpenilla.squaremap.addon.deathspot.listener;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.addon.deathspot.DeathSpots;
import xyz.jpenilla.squaremap.addon.deathspot.config.DeathSpotWorldConfig;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Pair;

public record PlayerListener(DeathSpots plugin) implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final UUID uuid = player.getUniqueId();
        final Location location = player.getLocation();

        final DeathSpotWorldConfig worldConfig = this.plugin.config().worldConfig(BukkitAdapter.worldIdentifier(location.getWorld()));
        if (!worldConfig.enabled) {
            return;
        }

        this.plugin.getDeathSpots().put(uuid, Pair.of(player.getName(), location));

        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerListener.this.plugin.getDeathSpots().remove(uuid);
            }
        }.runTaskLater(this.plugin, 20L * worldConfig.removeMarkerAfter);
    }
}
