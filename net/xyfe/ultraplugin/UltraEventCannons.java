package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class UltraEventCannons implements Listener {
    private JavaPlugin plugin;
    private UltraEvent ultraEvent;

    private World world;
    private final int cannonNum = 3;

    private BukkitRunnable laserRunnable;
    private Set<Cannon> cannons = new HashSet<>();

    public UltraEventCannons(JavaPlugin plugin, UltraEvent ultraEvent) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        int radius = ultraEvent.getRadius() - 10;
        Location loc = ultraEvent.getLocation();

        for(int i=0; i<cannonNum; i++) {
            double angle = ((double)i)/cannonNum * Math.PI * 2;
            cannons.add(new Cannon(new Location(
                    loc.getWorld(),
                    loc.getX() + radius * Math.cos(angle),
                    loc.getY() - 5,
                    loc.getZ() + radius * Math.sin(angle)
            )));
        }

        this.plugin = plugin;
        this.ultraEvent = ultraEvent;
        this.world = ultraEvent.getLocation().getWorld();
    }

    private void activateLasers() {
        laserRunnable = new BukkitRunnable() {
            private int counter = 0;

            @Override
            public void run() {
                for(Cannon cannon : cannons) {
                    Location loc = cannon.getLocation();
                    Vector vector = ultraEvent.getLocation().toVector().subtract(loc.toVector()).multiply(0.02);
                    for(int i=0; i<50; i++) {
                        Vector particleVec = vector.clone().multiply(i);
                        world.spawnParticle(Particle.LAVA, new Location(world,
                                loc.getX() + particleVec.getX(),
                                loc.getY() + particleVec.getY(),
                                loc.getZ() + particleVec.getZ()
                        ), 1,0, 0, 0, 0);
                    }

                    Vector hitPos = vector.clone().multiply(40);
                    world.spawnParticle(Particle.TOTEM, loc.getX() + hitPos.getX(), loc.getY() + hitPos.getY(), loc.getZ() + hitPos.getZ(), 100);
                }

                counter++;

                if(counter == 250) {
                    ultraEvent.end();
                }
            }
        };
        laserRunnable.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if(block != null && block.getType().equals(Material.STONE_BUTTON)) {
            for(Cannon cannon : cannons) {
                Location cloned = cannon.getLocation().clone();
                cloned.setY(cloned.getY() + 3);
                if(block.equals(cloned.getBlock())) {
                    cannon.activated = true;

                    boolean allActivated = true;

                    for(Cannon c : cannons) {
                        if(!c.activated) {
                            allActivated = false;
                            break;
                        }
                    }

                    if(allActivated) {
                        plugin.getServer().broadcastMessage("ALL CANNONS ACTIVATED");
                        activateLasers();
                    }

                    break;
                }
            }
        }
    }

    public void deactivate() {
        if(laserRunnable != null) {
            laserRunnable.cancel();
        }

        HandlerList.unregisterAll(this);
    }
}
