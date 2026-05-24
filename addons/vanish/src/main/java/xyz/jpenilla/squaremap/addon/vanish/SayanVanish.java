package xyz.jpenilla.squaremap.addon.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI;
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserUnVanishEvent;
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserVanishEvent;
import xyz.jpenilla.squaremap.api.Squaremap;

public record SayanVanish(Squaremap squaremap) implements VanishAdapter {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void hide(final BukkitUserVanishEvent event) {
        this.squaremap.playerManager().hide(event.getUser().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void show(final BukkitUserUnVanishEvent event) {
        this.squaremap.playerManager().show(event.getUser().getUniqueId());
    }

    @Override
    public boolean isVanished(final Player player) {
        return SayanVanishBukkitAPI.getInstance().isVanished(player.getUniqueId());
    }
}
