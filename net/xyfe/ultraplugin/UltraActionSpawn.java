package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.Random;

public class UltraActionSpawn implements IUltraAction {
    private EntityType type;
    private Random random;
    private int randMin;
    private int randMax;
    private double randForceMin;
    private double randForceMax;

    public UltraActionSpawn(EntityType type, int randMin, int randMax, double randForceMin, double randForceMax) {
        this.type = type;
        this.randMin = randMin;
        this.randMax = randMax;
        random = new Random();
    }

    public void action(Location loc) {
        World world = loc.getWorld();

        int n = random.nextInt(randMax + 1 - randMin) + randMin;

        double force = random.nextDouble() * (randForceMax + 1 - randForceMin) + randForceMin;

        for(int i=0; i<n; i++) {
            double angle = ((double)i)/n * Math.PI * 2;

            Location entLoc = loc.clone();
            entLoc.setX(entLoc.getX() + 5 * Math.cos(angle));
            entLoc.setZ(entLoc.getZ() + 5 * Math.sin(angle));

            Entity entity = world.spawnEntity(entLoc, type);

            entity.setVelocity(new Vector(
                    force*Math.cos(angle),
                    0,
                    force*Math.sin(angle)
            ));
        }
    }
}
