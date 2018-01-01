package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class UltraActionLightning implements IUltraAction {
    private int randMin = 10;
    private int randMax = 50;

    private int maxRadius;

    private Random random;

    public UltraActionLightning(int radius) {
        random = new Random();
        maxRadius = radius;
    }

    public void action(Location loc) {
        World world = loc.getWorld();

        int n = random.nextInt(randMax + 1 - randMin) + randMin;

        for(int i=0; i<n; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = random.nextDouble() * maxRadius;

            world.strikeLightning(new Location(world,
                    loc.getX() + radius*Math.cos(angle),
                    loc.getY(),
                    loc.getZ() + radius*Math.sin(angle)
            ));
        }
    }
}
