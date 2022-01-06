package xyz.jpenilla.squaremap.addon.vanish;

import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.jpenilla.squaremap.api.Squaremap;

public record SuperVanish(Squaremap squaremap) implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void hide(final PlayerHideEvent event) {
        this.squaremap.playerManager().hide(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void show(final PlayerShowEvent event) {
        this.squaremap.playerManager().show(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void join(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (VanishAPI.isInvisible(player)) {
            this.squaremap.playerManager().hide(player.getUniqueId());
        }
    }
}
