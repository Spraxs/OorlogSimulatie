package com.spraxs.oorlogsimulatie.command.commands;

import com.spraxs.oorlogsimulatie.command.PluginCommand;
import com.spraxs.oorlogsimulatie.data.Configuration;
import com.spraxs.oorlogsimulatie.data.OSFile;
import com.spraxs.oorlogsimulatie.utils.Message;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class CommandSpawn extends PluginCommand {

    public CommandSpawn(String name, boolean op) {
        super(name, op);
    }

    @Override
    public void execute() {
        if (this.sender instanceof Player) {
            Player player = (Player) this.sender;

            if (this.args == null) {
                this.sender.sendMessage(Message.prefix + "Gebruik van het commando /spawn:");
                this.sender.sendMessage(" ");
                this.sender.sendMessage(Message.prefix + "/spawn a || Plaats de spawn voor team A.");
                this.sender.sendMessage(Message.prefix + "/spawn b || Plaats de spawn voor team B.");
                this.sender.sendMessage(Message.prefix + "/spawn spectator || Plaats de spawn voor team B.");
                this.sender.sendMessage(Message.prefix + "/spawn lobby || Plaats de spawn voor team Lobby.");
            } else {

                if (this.args.length > 0) {

                    if (this.args[0].equalsIgnoreCase("a")) {
                        OSFile file = Configuration.getInstance().getFile(Configuration.LOCATIONS);
                        FileConfiguration config = file.getConfig();

                        Location loc = player.getLocation();

                        config.set("Spawn.A.X", loc.getBlockX() + 0.5);
                        config.set("Spawn.A.Y", loc.getBlockY());
                        config.set("Spawn.A.Z", loc.getBlockZ() + 0.5);
                        config.set("Spawn.A.V", loc.getDirection());
                        config.set("Spawn.A.W", loc.getWorld().getName());

                        file.save();

                        player.sendMessage(Message.prefix + "Je hebt spawn " + Message.highlightColor + "A" + Message.defaultColor + " geplaatst.");
                    } else if (this.args[0].equalsIgnoreCase("b")) {
                        OSFile file = Configuration.getInstance().getFile(Configuration.LOCATIONS);
                        FileConfiguration config = file.getConfig();

                        Location loc = player.getLocation();

                        config.set("Spawn.B.X", loc.getBlockX() + 0.5);
                        config.set("Spawn.B.Y", loc.getBlockY());
                        config.set("Spawn.B.Z", loc.getBlockZ() + 0.5);
                        config.set("Spawn.B.V", loc.getDirection());
                        config.set("Spawn.B.W", loc.getWorld().getName());

                        file.save();

                        player.sendMessage(Message.prefix + "Je hebt spawn " + Message.highlightColor + "B" + Message.defaultColor + " geplaatst.");
                    } else if (this.args[0].equalsIgnoreCase("spectator")) {
                        OSFile file = Configuration.getInstance().getFile(Configuration.LOCATIONS);
                        FileConfiguration config = file.getConfig();

                        Location loc = player.getLocation();

                        config.set("Spawn.Spectator.X", loc.getBlockX() + 0.5);
                        config.set("Spawn.Spectator.Y", loc.getBlockY());
                        config.set("Spawn.Spectator.Z", loc.getBlockZ() + 0.5);
                        config.set("Spawn.Spectator.V", loc.getDirection());
                        config.set("Spawn.Spectator.W", loc.getWorld().getName());

                        file.save();

                        player.sendMessage(Message.prefix + "Je hebt spawn " + Message.highlightColor + "Spectator" + Message.defaultColor + " geplaatst.");
                    } else if (this.args[0].equalsIgnoreCase("lobby")) {
                        OSFile file = Configuration.getInstance().getFile(Configuration.LOCATIONS);
                        FileConfiguration config = file.getConfig();

                        Location loc = player.getLocation();

                        config.set("Spawn.Lobby.X", loc.getBlockX() + 0.5);
                        config.set("Spawn.Lobby.Y", loc.getBlockY());
                        config.set("Spawn.Lobby.Z", loc.getBlockZ() + 0.5);
                        config.set("Spawn.Lobby.V", loc.getDirection());
                        config.set("Spawn.Lobby.W", loc.getWorld().getName());

                        file.save();

                        player.sendMessage(Message.prefix + "Je hebt spawn " + Message.highlightColor + "Lobby" + Message.defaultColor + " geplaatst.");
                    }
                }
            }
        }
    }
}
