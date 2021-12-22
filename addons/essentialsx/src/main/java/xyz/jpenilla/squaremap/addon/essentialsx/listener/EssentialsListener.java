package xyz.jpenilla.squaremap.addon.essentialsx.listener;

import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.jpenilla.squaremap.addon.essentialsx.configuration.Config;
import xyz.jpenilla.squaremap.addon.essentialsx.hook.EssentialsHook;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public final class EssentialsListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanishStatusChange(VanishStatusChangeEvent event) {
        if (!Config.HIDE_VANISHED) {
            return;
        }
        final Player player = event.getAffected().getBase();
        final boolean vanished = event.getValue();
        SquaremapProvider.get().playerManager().hidden(player.getUniqueId(), vanished);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Config.HIDE_VANISHED) {
            return;
        }
        final Player player = event.getPlayer();
        if (EssentialsHook.isVanished(player)) {
            SquaremapProvider.get().playerManager().hide(player.getUniqueId());
        }
    }
}
