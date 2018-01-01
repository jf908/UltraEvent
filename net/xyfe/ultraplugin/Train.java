package net.xyfe.ultraplugin;

import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class Train {
    private Player controller;
    private List<Minecart> minecarts;

    public Train(List<Minecart> minecarts) {
        this.minecarts = minecarts;
    }

    public Minecart getControllerCart() {
        return minecarts.get(0);
    }

    public Player getController() {
        return controller;
    }

    public void setController(Player controller) {
        this.controller = controller;
    }

    public Iterator<Minecart> minecartIterator() {
        return minecarts.iterator();
    }
}
