package net.xyfe.ultraplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.LinkedList;
import java.util.List;

public class CommandTrain implements CommandExecutor {
    UltraPlugin plugin;

    public CommandTrain(UltraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length != 1) return false;

            Player player = (Player) sender;

            int n;
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                return false;
            }

            if(n < 1) return false;

            List<Minecart> minecarts = new LinkedList<>();

            for(int i=0; i<n; i++) {
                Minecart minecart = (Minecart) player.getWorld().spawnEntity(player.getLocation(), EntityType.MINECART);
                minecart.setMetadata("Invulnerable", new FixedMetadataValue(plugin, true));
                minecarts.add(minecart);
            }

            plugin.trainHandler.addTrain(minecarts);
        }

        return true;
    }
}
