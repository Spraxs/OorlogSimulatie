package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.data.LeaderboardWins;
import com.spraxs.oorlogsimulatie.utils.Message;
import com.spraxs.oorlogsimulatie.utils.events.leaderboard.LeaderboardWinsProcessedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class LeaderboardWinsListener implements Listener {

    public LeaderboardWinsListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onProcessed(LeaderboardWinsProcessedEvent event) {

        //GameManager.getInstance().setCanStop(true);

        if (event.getPlayerWinsInfo() != null) {

            List<LeaderboardWins> topList = event.getTopWinsList();

            for (int i = 0; i < topList.size(); i++) {
                LeaderboardWins info = topList.get(i);

                Bukkit.broadcastMessage(Message.highlightColor + "#" + (i + 1) + ChatColor.GREEN + " " + info.getName() + Message.highlightColor + " " + info.getWins() + " wins");
            }
        } else if (event.getTopWinsList() != null) {
            LeaderboardWins info = event.getPlayerWinsInfo();

            Bukkit.broadcastMessage(Message.highlightColor + "#" + info.getPosition() + ChatColor.GREEN + " "
                    + info.getName() + Message.highlightColor + " " + info.getWins() + " wins");
        }
    }
}
