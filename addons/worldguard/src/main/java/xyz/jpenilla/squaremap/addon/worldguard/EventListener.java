package xyz.jpenilla.squaremap.addon.worldguard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public record EventListener(SquaremapWorldGuard plugin) implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void worldLoad(final WorldLoadEvent event) {
        this.plugin.squaremapHook().addWorld(event.getWorld());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void worldUnload(final WorldUnloadEvent event) {
        this.plugin.squaremapHook().removeWorld(event.getWorld());
    }
}
