package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Random;

public class UltraActionBlocks implements IUltraAction {
    private Random random;
    private int randMin;
    private int randMax;
    private double randForceMin;
    private double randForceMax;

    private Material[] materials = {
            Material.ANVIL,
            Material.BEDROCK,
            Material.CAKE_BLOCK,
            Material.CLAY,
            Material.COAL_ORE,
            Material.COAL_BLOCK,
            Material.COBBLESTONE,
            Material.CONCRETE,
            Material.DIRT,
            Material.DIAMOND_BLOCK,
            Material.EMERALD_BLOCK,
            Material.END_BRICKS,
            Material.ENDER_STONE,
            Material.FIRE,
            Material.GLASS,
            Material.GOLD_BLOCK,
            Material.GRASS,
            Material.GRAVEL,
            Material.HAY_BLOCK,
            Material.ICE,
            Material.IRON_BLOCK,
            Material.IRON_ORE,
            Material.JUKEBOX,
            Material.LAVA,
            Material.LEAVES,
            Material.LOG,
            Material.MELON_BLOCK,
            Material.OBSIDIAN,
            Material.PRISMARINE,
            Material.PUMPKIN,
            Material.QUARTZ_BLOCK,
            Material.QUARTZ_ORE,
            Material.REDSTONE_BLOCK,
            Material.SAND,
            Material.SNOW_BLOCK,
            Material.SOUL_SAND,
            Material.SPONGE,
            Material.STONE,
            Material.TNT,
            Material.TORCH,
            Material.WATER,
            Material.WEB,
            Material.WOOL
    };

    public UltraActionBlocks(int randMin, int randMax, double randForceMin, double randForceMax) {
        this.randMin = randMin;
        this.randMax = randMax;
        random = new Random();
    }

    public void action(Location loc) {
        World world = loc.getWorld();

        int n = random.nextInt(randMax + 1 - randMin) + randMin;

        double force = random.nextDouble() * (randForceMax + 1 - randForceMin) + randForceMin;

        for(int i=0; i<n; i++) {
            int mat = random.nextInt(materials.length);
            double angle = ((double)i)/n * Math.PI * 2;

            Location blockLoc = loc.clone();
            blockLoc.setX(blockLoc.getX() + 5 * Math.cos(angle));
            blockLoc.setZ(blockLoc.getZ() + 5 * Math.sin(angle));

            FallingBlock fallingBlock = world.spawnFallingBlock(blockLoc, new MaterialData(materials[mat]));

            fallingBlock.setVelocity(new Vector(
                    force*Math.cos(angle),
                    0,
                    force*Math.sin(angle)
            ));
        }
    }
}
