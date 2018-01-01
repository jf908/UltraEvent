package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.Material;

public class Cannon {

    public boolean activated;
    private Location location;

    public Cannon(Location loc) {
        location = loc;
        build();
    }

    private void build() {
        for(int k=0; k<=1; k++) {
            for(int i=-2; i<=2; i++) {
                for(int j=-2; j<=2; j++) {
                    if(Math.abs(i) == 2 && Math.abs(j) == 2) continue;
                    Location cloned = location.clone();
                    cloned.setX(location.getX() + i);
                    cloned.setY(location.getY() + k);
                    cloned.setZ(location.getZ() + j);
                    cloned.getBlock().setType(Material.OBSIDIAN);
                }
            }
        }

        for(int i=-1; i<=1; i++) {
            for(int j=-1; j<=1; j++) {
                Location cloned = location.clone();
                cloned.setX(location.getX() + i);
                cloned.setY(location.getY() + 2);
                cloned.setZ(location.getZ() + j);
                cloned.getBlock().setType(Material.OBSIDIAN);
            }
        }
    }

    public Location getLocation() {
        return location;
    }
}
