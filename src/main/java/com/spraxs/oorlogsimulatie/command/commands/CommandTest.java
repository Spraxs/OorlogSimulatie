package com.spraxs.oorlogsimulatie.command.commands;

import com.spraxs.oorlogsimulatie.command.PluginCommand;
import com.spraxs.oorlogsimulatie.game.data.LeaderboardTest;
import com.spraxs.oorlogsimulatie.utils.Message;
import org.bukkit.entity.Player;

/**
 * Author: Spraxs.
 * Date: 3-3-2018.
 */

public class CommandTest extends PluginCommand {

    public CommandTest(String name, boolean op) {
        super(name, op);
    }

    @Override
    public void execute() {
        if (this.sender instanceof Player) {
            Player player = (Player) this.sender;

            if (this.args == null) {
                this.sender.sendMessage(Message.prefix + "Gebruik van het commando /test:");
                this.sender.sendMessage(" ");
                this.sender.sendMessage(Message.prefix + "/test leaderboard top || Get top 10 from leaderboards.");
                this.sender.sendMessage(Message.prefix + "/test leaderboard player || Get player from leaderboards.");
            } else {

                if (this.args.length > 0) {

                    if (this.args[0].equalsIgnoreCase("leaderboard")) {

                        if (this.args[1].equalsIgnoreCase("top")) {

                            new LeaderboardTest();

                        } else if (this.args[1].equalsIgnoreCase("player")) {

                            new LeaderboardTest(player);

                        } else {
                            this.sender.sendMessage(Message.prefix + "Ik snap niet wat u bedoelt. Gebruik: '/test' om alle commando's te bekijken.");
                        }

                    } else if (this.args[0].equalsIgnoreCase("map")) {
                        player.sendMessage(player.getWorld().getName());
                    } else {
                        this.sender.sendMessage(Message.prefix + "Ik snap niet wat u bedoelt. Gebruik: '/test' om alle commando's te bekijken.");
                    }
                }
            }
        }
    }
}
