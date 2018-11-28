package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.data.LeaderboardKills;
import com.spraxs.oorlogsimulatie.utils.Message;
import com.spraxs.oorlogsimulatie.utils.events.leaderboard.LeaderboardKillsProcessedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

/**
 * Author: Spraxs.
 * Date: 3-3-2018.
 */

public class LeaderboardKillsListener implements Listener {

    public LeaderboardKillsListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onProcessed(LeaderboardKillsProcessedEvent event) {

        //GameManager.getInstance().setCanStop(true);

        if (event.getPlayerKillsInfo() != null) {

            List<LeaderboardKills> topList = event.getTopKillsList();

            for (int i = 0; i < topList.size(); i++) {
                LeaderboardKills info = topList.get(i);

                Bukkit.broadcastMessage(Message.highlightColor + "#" + (i + 1) + ChatColor.GREEN + " " + info.getName() + Message.highlightColor + " " + info.getKills() + " kills");
            }
        } else if (event.getPlayerKillsInfo() != null) {
            LeaderboardKills info = event.getPlayerKillsInfo();

            Bukkit.broadcastMessage(Message.highlightColor + "#" + info.getPosition() + ChatColor.GREEN + " "
                    + info.getName() + Message.highlightColor + " " + info.getKills() + " kills");
        }
    }
}
