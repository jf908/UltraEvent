package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class TrainHandler extends AutoListener {
    private JavaPlugin plugin;

    private Map<Minecart, Train> trains = new HashMap<>();
    private Set<Train> activeTrains = new HashSet<>();

    private BukkitRunnable trainRunnable;

    public TrainHandler(JavaPlugin plugin) {
        super(plugin);
        this.plugin = plugin;

        trainRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                for(Train train : activeTrains) {
                    Player player = train.getController();
                    Vector direction = player.getLocation().getDirection();

                    train.getControllerCart().setVelocity(direction);

                    setTrainPosition(train);

                    Iterator<Minecart> itr = train.minecartIterator();
                    while(itr.hasNext()) {
                        Minecart cart = itr.next();
                        cart.getWorld().spawnParticle(Particle.CLOUD, cart.getLocation(), 0, 0, 0, 0, 1);
                    }
                }
            }
        };
        trainRunnable.runTaskTimer(this.plugin, 0,1);
    }

    public void addTrain(List<Minecart> minecarts) {
        Train train = new Train(minecarts);
        for(Minecart cart : minecarts) {
            trains.put(cart, train);
        }

        setTrainPosition(train);
    }

    private void setTrainPosition(Train train) {
        Iterator<Minecart> itr = train.minecartIterator();
        Minecart main = itr.next();
        Location mainLoc = main.getLocation();

        int i = 1;
        boolean flag = false;

        while(itr.hasNext()) {
            Minecart minecart = itr.next();
            Vector newLoc;
            if(flag) {
                newLoc = mainLoc.getDirection().multiply(2f * i++).add(mainLoc.toVector());
            } else {
                newLoc = mainLoc.getDirection().multiply(-2f * i).add(mainLoc.toVector());
            }
            flag = !flag;
            minecart.teleport(new Location(main.getWorld(),
                    newLoc.getX(),
                    newLoc.getY(),
                    newLoc.getZ(),
                    mainLoc.getYaw(),
                    mainLoc.getPitch()
            ));
            minecart.setVelocity(main.getVelocity());
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if(event.getEntered() instanceof Player && event.getVehicle() instanceof Minecart) {
            Minecart cart = (Minecart) event.getVehicle();
            if(trains.containsKey(cart)) {
                Train train = trains.get(cart);
                if(train.getControllerCart() != cart) return;
                train.setController((Player) event.getEntered());
                activeTrains.add(train);
            }
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if(event.getExited() instanceof Player && event.getVehicle() instanceof Minecart) {
            Minecart cart = (Minecart) event.getVehicle();
            if(trains.containsKey(cart)) {
                Train train = trains.get(cart);
                if(train.getControllerCart() != cart) return;
                activeTrains.remove(train);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if(event.getVehicle() instanceof Minecart) {
            Minecart cart = (Minecart) event.getVehicle();
            if(trains.containsKey(cart)) {
                event.setCancelled(true);
            }
        }
    }

    public void disable() {
        trainRunnable.cancel();
        for(Map.Entry<Minecart, Train> trainEntry : trains.entrySet()) {
            Train train = trainEntry.getValue();
            Iterator<Minecart> itr = train.minecartIterator();
            while(itr.hasNext()) {
                Minecart minecart = itr.next();
                if(minecart.isValid()) {
                    minecart.remove();
                }
            }
        }
    }
}
