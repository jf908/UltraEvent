package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UltraEventBarrier {
    private double radius;

    TrapPlayer trapPlayer;
    Set<FallingBlock> blocks = new HashSet<>();

    BukkitRunnable spawner;
    BukkitRunnable velocitySetter;

    public UltraEventBarrier(JavaPlugin plugin, UltraEvent ultraEvent) {
        Location loc = ultraEvent.getLocation();
        radius = ultraEvent.getRadius();

        // Activate player "trap" to keep players in area
        trapPlayer = new TrapPlayer(loc, radius);
        trapPlayer.activate(plugin);
        // Teleport players outside of event area to inside
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            if(!trapPlayer.inRange(player.getLocation())) {
                player.teleport(loc);
            }
        }

        World world = loc.getWorld();

        spawner = new BukkitRunnable() {
            @Override
            public void run() {
                for(int i=0; i<50; i++) {
                    double theta = ((double)i)/50 * Math.PI * 2;
                    FallingBlock block = world.spawnFallingBlock(new Location(world,
                            loc.getX() + radius*Math.cos(theta),
                            128,
                            loc.getZ() + radius*Math.sin(theta)
                    ), new MaterialData(Material.MAGMA));
                    block.setDropItem(false);
                    blocks.add(block);
                }
            }
        };
        spawner.runTaskTimer(plugin, 0,5);

        velocitySetter = new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<FallingBlock> itr = blocks.iterator();
                while(itr.hasNext()) {
                    FallingBlock block = itr.next();
                    if(!block.isValid()) {
                        itr.remove();
                    } else {
                        block.setVelocity(new Vector(0,-1f,0));
                    }
                }
            }
        };
        velocitySetter.runTaskTimer(plugin, 0,1);
    }

    public void deactivate() {
        trapPlayer.deactivate();
        velocitySetter.cancel();
        spawner.cancel();

        // Kill blocks
        for(FallingBlock block : blocks) {
            block.remove();
        }
        blocks.clear();
    }
}
