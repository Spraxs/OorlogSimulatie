package com.spraxs.oorlogsimulatie.command.commands;

import com.spraxs.oorlogsimulatie.command.PluginCommand;
import com.spraxs.oorlogsimulatie.utils.Message;
import com.spraxs.oorlogsimulatie.utils.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class CommandMap extends PluginCommand {

    public CommandMap(String name, boolean op) {
        super(name, op);
    }

    @Override
    public void execute() {
        if (this.sender instanceof Player) {
            Player player = (Player) this.sender;

            if (this.args == null) {
                this.sender.sendMessage(Message.prefix + "Gebruik van het commando /map:");
                this.sender.sendMessage(" ");
                this.sender.sendMessage(Message.prefix + "/map create || Maak een back-up van de 'world' map.");
                this.sender.sendMessage(Message.prefix + "/map tp || Teleport naar de 'GameMap'.");
            } else {

                if (this.args.length > 0) {

                    if (this.args[0].equalsIgnoreCase("create")) {

                        WorldManager.getInstance().makeBackUp(player);

                    } else if (args[0].equalsIgnoreCase("tp")) {

                        player.teleport(Bukkit.getWorld(WorldManager.getInstance().getGameMapName()).getSpawnLocation().add(0, 0, 0));

                    } else {
                        this.sender.sendMessage(Message.prefix + "Ik snap niet wat u bedoelt. Gebruik: '/map' om alle commando's te bekijken.");
                    }
                }
            }
        }
    }
}
