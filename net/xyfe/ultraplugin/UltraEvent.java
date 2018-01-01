package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UltraEvent {
    private UltraPlugin plugin;
    private BlockChangeCancel fallingBlockCancel = new BlockChangeCancel(Material.MAGMA);

    private Location loc;
    private World world;
//    private BlockGroup<Float> blocks = new BlockGroup<>();

    private UltraEventBarrier barrier;
    private UltraEventCannons cannons;
    private UltraEventPotionArrow potArrows;

    private List<IUltraAction> actions = new ArrayList<>();
    private int actionDelay = 0;

    private BukkitRunnable actionRunnable;
    private BukkitRunnable particleRunnable;
    private BukkitRunnable explosionRunnable;

    private int health;

    public UltraEvent(UltraPlugin plugin, Location loc) {
        this.loc = loc;
        this.plugin = plugin;
        world = this.loc.getWorld();

        health = 20;
//        health = 5;

        fallingBlockCancel.activate(plugin);

        barrier = new UltraEventBarrier(plugin, this);
        potArrows = new UltraEventPotionArrow(plugin, this);

        UltraActionBlocks uab = new UltraActionBlocks(25, 50, 0.01, 1);
        actions.add(new UltraActionSpawn(EntityType.CREEPER, 10, 25, 1, 5));
        actions.add(new UltraActionSpawn(EntityType.PRIMED_TNT, 10, 25, 0.1, 1));
        actions.add(uab);
        actions.add(uab);
        actions.add(new UltraActionLightning(getRadius()));

        explosionRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                world.createExplosion(loc, 4f);
                world.spawnParticle(Particle.FLAME, loc, 100);
            }
        };
        explosionRunnable.runTaskTimer(plugin, 0,2);

        actionRunnable = new BukkitRunnable() {
            Random random = new Random();

            @Override
            public void run() {
                if(actionDelay <= 0) {
                    int next = random.nextInt(actions.size());
                    actions.get(next).action(loc);

                    actionDelay = random.nextInt(2) + 2;
                } else {
                    actionDelay--;
                }
            }
        };
        actionRunnable.runTaskTimer(plugin, 0, 10);

        particleRunnable = new BukkitRunnable() {
            private int radius = 8;
            private int count = 100;

            @Override
            public void run() {
                for(int i=0; i<count; i++) {
                    double angle = ((double)i)/count * Math.PI * 2;
                    world.spawnParticle(Particle.SPELL_WITCH, new Location(world,
                            loc.getX() + radius*Math.cos(angle),
                            loc.getY() + radius*Math.cos(angle)*0.5,
                            loc.getZ() + radius*Math.sin(angle)
                    ), 1,0, 0, 0, 0);

                    world.spawnParticle(Particle.SPELL_WITCH, new Location(world,
                            loc.getX() + radius*Math.cos(angle),
                            loc.getY() - radius*Math.cos(angle)*0.5,
                            loc.getZ() + radius*Math.sin(angle)
                    ), 1,0, 0, 0, 0);
                }
            }
        };
        particleRunnable.runTaskTimer(plugin, 0, 5);
    }


    public void cancel() {
        fallingBlockCancel.deactivate();

        // Cancel runnables
        actionRunnable.cancel();
        particleRunnable.cancel();
        explosionRunnable.cancel();

        // Deactivate UltraEvent modules
        barrier.deactivate();
        potArrows.deactivate();
        if(cannons != null) {
            cannons.deactivate();
        }

        actions.clear();

        plugin.removeUltraEvent(this);
    }

    public Location getLocation() {
        return loc;
    }

    public int getRadius() {
        return 50;
    }

    // Returns true if health is at minimum before cannon value
    public boolean damaged() {
        health--;
        plugin.getServer().broadcastMessage("??? AT " + health + " HP");
        if(health == 3) {
            plugin.getServer().broadcastMessage("CANNONS DEPLOYED!!!");
            cannons = new UltraEventCannons(plugin, this);
        }
        return health == 3;
    }

    public void end() {
        cancel();
        world.createExplosion(loc, 200);
        plugin.getServer().broadcastMessage("??? DESTROYED!!!");
    }
}
