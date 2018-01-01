package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class TrapPlayer extends ToggleableListener {
    Location loc;
    double maxDistance;

    public TrapPlayer(Location loc, double maxDistance) {
        this.loc = loc;
        this.maxDistance = maxDistance;
    }

    public boolean inRange(Location goingTo) {
        double xdif = goingTo.getX() - loc.getX();
        double zdif = goingTo.getZ() - loc.getZ();
        return Math.sqrt(xdif*xdif + zdif*zdif) < maxDistance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!inRange(event.getTo())) {
            event.setCancelled(true);
        }
    }
}
