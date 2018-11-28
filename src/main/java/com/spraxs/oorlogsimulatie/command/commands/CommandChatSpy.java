package com.spraxs.oorlogsimulatie.command.commands;

import com.spraxs.oorlogsimulatie.command.PluginCommand;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.Message;
import com.spraxs.rank.api.RankAPI;
import com.spraxs.rank.utils.Type;
import org.bukkit.entity.Player;

public class CommandChatSpy extends PluginCommand {

    public CommandChatSpy(String name, boolean op) {
        super(name, op);
    }

    @Override
    public void execute() {
        if (this.args == null && this.sender instanceof Player) {
            Player player = (Player) this.sender;
            OSPlayer osPlayer = PlayerManager.getInstance().getPlayer(player);

            if (RankAPI.getAPI().getRank(((Player) this.sender).getPlayer()).hasPermission(Type.HELPER)) {
                if (osPlayer.isChatspy()) {
                    osPlayer.setChatspy(false);
                    player.sendMessage(Message.prefix + "Chatspy is nu uit");
                    return;
                } else {
                    osPlayer.setChatspy(true);
                    player.sendMessage(Message.prefix + "Chatspy is nu aan");
                    return;
                }
            } else {
                player.sendMessage(Message.prefix + "Dit commando is onbekend.");
            }
        }
    }
}
