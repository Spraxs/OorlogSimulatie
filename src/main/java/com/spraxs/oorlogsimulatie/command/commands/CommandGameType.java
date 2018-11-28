package com.spraxs.oorlogsimulatie.command.commands;

import com.spraxs.oorlogsimulatie.command.PluginCommand;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameType;
import com.spraxs.oorlogsimulatie.utils.Message;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class CommandGameType extends PluginCommand {

    public CommandGameType(String name, boolean op) {
        super(name, op);
    }

    @Override
    public void execute() {
        if (this.args == null) {
            this.sender.sendMessage(Message.prefix + "Gebruik van het commando /gametype:");
            this.sender.sendMessage(" ");
            this.sender.sendMessage(Message.prefix + "/gametype classic || Verander de gametype naar classic.");
            this.sender.sendMessage(Message.prefix + "/gametype custom || Verander de gametype naar custom.");
        } else {

            if (this.args.length > 0) {
                if (this.args[0].equalsIgnoreCase("classic")) {
                    GameServer gameServer = GameServer.getInstance();

                    gameServer.setGameType(GameType.CLASSIC);
                    sender.sendMessage(Message.prefix + "De gametype is nu " + Message.highlightColor + "classic");
                } else if (this.args[0].equalsIgnoreCase("custom")) {
                    GameServer gameServer = GameServer.getInstance();

                    gameServer.setGameType(GameType.CUSTOM);
                    sender.sendMessage(Message.prefix + "De gametype is nu " + Message.highlightColor + "custom");
                }
            }
        }
    }
}
