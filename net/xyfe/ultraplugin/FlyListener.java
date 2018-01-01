package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class FlyListener extends AutoListener {
    protected final JavaPlugin plugin;

    Set<Player> players = new HashSet<>();

    public FlyListener(JavaPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : players) {
                    Location loc = p.getLocation();
                    Vector dir = loc.getDirection();
                    p.setVelocity(dir);
                    p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 0, 0, 0, 0, 1);
                }
            }
        }.runTaskTimer(this.plugin, 0,1);
    }

    @EventHandler
    public void onItemChange(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        PlayerInventory inv = p.getInventory();
        ItemStack item = inv.getItem(event.getNewSlot());
        if(item != null && item.getType().equals(Material.PAPER)) {
            players.add(p);
        } else {
            players.remove(p);
        }
    }
}
