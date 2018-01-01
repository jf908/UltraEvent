package net.xyfe.ultraplugin;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UltraEventPotionArrow implements Listener {
    private UltraEvent ultraEvent;
    private Random random;

    private Set<Block> activeBlocks = new HashSet<>();

    private final HashMap<Color, DyeColor> arrowBlockMap = new HashMap<>();

    private final DyeColor[] colors = {
            DyeColor.BLUE, DyeColor.SILVER, DyeColor.LIME, DyeColor.ORANGE,
            DyeColor.LIGHT_BLUE, DyeColor.BLUE, DyeColor.RED, DyeColor.GREEN,
            DyeColor.PINK, DyeColor.GRAY
    };

    public UltraEventPotionArrow(JavaPlugin plugin, UltraEvent ultraEvent) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.ultraEvent = ultraEvent;

        random = new Random();

        arrowBlockMap.put(Color.fromRGB(0x1F1FA1), DyeColor.BLUE);
        arrowBlockMap.put(Color.fromRGB(0x7F8392), DyeColor.SILVER);
        arrowBlockMap.put(Color.fromRGB(0x22FF4C), DyeColor.LIME);
        arrowBlockMap.put(Color.fromRGB(0xE49A3A), DyeColor.ORANGE);
        arrowBlockMap.put(Color.fromRGB(0x7CAFC6), DyeColor.LIGHT_BLUE);
        arrowBlockMap.put(Color.fromRGB(0x2E5299), DyeColor.BLUE);
        arrowBlockMap.put(Color.fromRGB(0xF82423), DyeColor.RED);
        arrowBlockMap.put(Color.fromRGB(0x4E9331), DyeColor.GREEN);
        arrowBlockMap.put(Color.fromRGB(0xCD5CAB), DyeColor.PINK);
        arrowBlockMap.put(Color.fromRGB(0x484D48), DyeColor.GRAY);

        for(int i=0; i<5; i++) {
            spawnNextBlock();
        }
    }

    public void deactivate() {
        for(Block block : activeBlocks) {
            block.setType(Material.AIR);
        }
        HandlerList.unregisterAll(this);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if(event.getHitBlock() != null && event.getEntity() instanceof TippedArrow) {
            TippedArrow arrow = (TippedArrow) event.getEntity();

            DyeColor color = arrowBlockMap.get(arrow.getColor());
            if(color != null && activeBlocks.contains(event.getHitBlock()) && DyeColor.getByWoolData(event.getHitBlock().getData()).equals(color)) {
                successfulHit(arrow, event.getHitBlock(), color);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(activeBlocks.contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    private void spawnNextBlock() {
        Location loc = ultraEvent.getLocation();

        double angle = (Math.PI * 2) * random.nextDouble();
        double radius = (ultraEvent.getRadius()-15) * random.nextDouble() + 10;

        int x = (int)(Math.floor(radius*Math.cos(angle)) + loc.getX());
        int y = (int)(loc.getY());
        int z = (int)(Math.floor(radius*Math.sin(angle)) + loc.getZ());
        Block block = loc.getWorld().getBlockAt(x, y, z);
        block.setType(Material.CONCRETE);
        DyeColor color = colors[random.nextInt(colors.length)];

        block.setData(color.getWoolData());

        activeBlocks.add(block);
    }

    private void successfulHit(TippedArrow arrow, Block block, DyeColor color) {
        activeBlocks.remove(block);

        arrow.remove();
        block.setType(Material.AIR);

        World world = block.getWorld();

        world.createExplosion(block.getLocation(), 1f);
        world.spawnParticle(Particle.TOTEM, block.getLocation(), 500);

        spawnNextBlock();

        if(ultraEvent.damaged()) {
            for(Block b : activeBlocks) {
                b.setType(Material.AIR);
            }
            activeBlocks.clear();
        }
    }
}
