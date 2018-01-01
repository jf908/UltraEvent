package net.xyfe.ultraplugin;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public class UltraPlugin extends JavaPlugin {
    private HashSet<UltraEvent> ultraEvents = new HashSet<>();

    public TrainHandler trainHandler;

    public void addUltraEvent(UltraEvent ultraEvent) {
        ultraEvents.add(ultraEvent);
    }

    public void removeUltraEvent(UltraEvent ultraEvent) {
        ultraEvents.remove(ultraEvent);
    }

    @Override
    public void onEnable() {
        new FlyListener(this);
        trainHandler = new TrainHandler(this);

        this.getCommand("ultraevent").setExecutor(new CommandUltraEvent(this));
        this.getCommand("train").setExecutor(new CommandTrain(this));
    }

    @Override
    public void onDisable() {
        for(UltraEvent event : ultraEvents) {
            event.cancel();
        }

        trainHandler.disable();

        HandlerList.unregisterAll(this);
    }
}
