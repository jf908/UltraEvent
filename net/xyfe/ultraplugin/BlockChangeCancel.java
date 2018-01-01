package net.xyfe.ultraplugin;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockChangeCancel extends ToggleableListener {
    private Material material;

    public BlockChangeCancel(Material material) {
        this.material = material;
    }

    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event) {
        if(event.getEntityType().equals(EntityType.FALLING_BLOCK) && event.getTo().equals(material)) {
            event.setCancelled(true);
        }
    }
}
