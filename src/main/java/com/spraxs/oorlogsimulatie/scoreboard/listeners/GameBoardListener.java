package com.spraxs.oorlogsimulatie.scoreboard.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.Message;
import com.spraxs.oorlogsimulatie.utils.events.game.GameTimerUpdateASyncEvent;
import com.spraxs.oorlogsimulatie.utils.events.game.OSPlayerDeathASyncEvent;
import com.spraxs.oorlogsimulatie.utils.events.game.OSPlayerDeathSyncEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class GameBoardListener implements Listener {

    public GameBoardListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onGameTimerUpdate(GameTimerUpdateASyncEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.BUSY) return;

        for (OSPlayer player : PlayerManager.getInstance().players.values()) {
            if (player.getOSBoard() != null) {
                Team team = player.getOSBoard().getBoard().getTeam("gameTimer");
                if (team != null) {
                    team.setSuffix("" + Message.highlightColor + time(event.getGameTime()));
                }
            }
        }
    }

    @EventHandler
    public void onOSPlayerDeath(OSPlayerDeathSyncEvent event) {
        deathEventHandler();
    }

    @EventHandler
    public void onOSPlayerDeathASync(OSPlayerDeathASyncEvent event) {
        deathEventHandler();
    }

    private void deathEventHandler() {
        if (GameServer.getInstance().getGameState() != GameState.BUSY) return;

        for (OSPlayer player : PlayerManager.getInstance().players.values()) {
            if (player.getOSBoard() != null) {
                if (player.getTeamID() == null) return;
                Team team = null;
                com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team osTeam = TeamManager.getInstance().getTeam(player.getTeamID());

                int size = -10;

                if (osTeam != null) {
                    team = player.getOSBoard().getBoard().getTeam(osTeam.getName());

                    size = osTeam.getAliveSize();
                }


                if (team != null && size != -10) {

                    team.setSuffix("" + Message.highlightColor + size);
                }
            }
        }
    }

    private String time(int seconds) {
        int second =  Math.round(seconds);
        int minute = Math.round(second/60);
        int hour =  Math.round(minute/60);

        int s = second - minute * 60;
        int m = minute - hour * 60;

        int length = (int)(Math.log10(s)+1);

        if (length == 1) {
            String time = "" + m + ":0" + s;
            return time;
        } else if (s == 0) {
            String time = "" + m + ":00";
            return time;
        }

        String time = "" + m + ":" + s;
        return time;
    }
}
