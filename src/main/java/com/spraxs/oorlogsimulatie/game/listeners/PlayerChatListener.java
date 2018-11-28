package com.spraxs.oorlogsimulatie.game.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.rank.api.RankAPI;
import com.spraxs.rank.player.profiles.Rank;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class PlayerChatListener implements Listener {

    public PlayerChatListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        OSPlayer player = PlayerManager.getInstance().getPlayer(event.getPlayer());
        Rank rank = RankAPI.getAPI().getRank(event.getPlayer());

        if (player.isRankLoaded()) {
            player.chat(event, rank);
            return;
        }

        event.setFormat(ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + event.getMessage());
    }
}
