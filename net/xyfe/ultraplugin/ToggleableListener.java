package net.xyfe.ultraplugin;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ToggleableListener implements Listener {
    public void activate(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void deactivate() {
        HandlerList.unregisterAll(this);
    }
}
