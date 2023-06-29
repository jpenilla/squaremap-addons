package xyz.jpenilla.squaremap.addon.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface VanishAdapter extends Listener {

    boolean isVanished(final Player player);
}
