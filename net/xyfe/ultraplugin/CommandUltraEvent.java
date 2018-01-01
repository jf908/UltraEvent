package net.xyfe.ultraplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUltraEvent implements CommandExecutor {
    UltraPlugin plugin;

    public CommandUltraEvent(UltraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length < 3) return false;

            Player player = (Player) sender;

            int x, y, z;
            try {
                x = Integer.parseInt(args[0]);
                y = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                return false;
            }

            plugin.addUltraEvent(new UltraEvent(plugin, new Location(player.getWorld(), x, y, z)));
        }

        return true;
    }
}
