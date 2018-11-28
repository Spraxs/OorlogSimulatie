package com.spraxs.oorlogsimulatie.scoreboard.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.LobbyManager;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.Message;
import com.spraxs.oorlogsimulatie.utils.events.GameStateUpdateEvent;
import com.spraxs.oorlogsimulatie.utils.events.OSPlayerCountUpdateEvent;
import com.spraxs.oorlogsimulatie.utils.events.lobby.LobbyCountdownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class LobbyBoardListener implements Listener {

    public LobbyBoardListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onUpdateGameState(GameStateUpdateEvent event) {
        GameState gameState = event.getGameState();

        if (gameState != GameState.OPEN && gameState != GameState.STARTING) return;


        if (gameState == GameState.STARTING) {
            for (OSPlayer player : PlayerManager.getInstance().players.values()) {
                if (player.getOSBoard() != null) {
                    player.getOSBoard().getBoard().getTeam("state").setSuffix("" + Message.highlightColor + LobbyManager.getInstance().getCountdown());
                }
            }
        } else {
            for (OSPlayer player : PlayerManager.getInstance().players.values()) {
                player.getOSBoard().getBoard().getTeam("state").setSuffix(Message.highlightColor + "wachten");
            }

            LobbyManager.getInstance().scoreboardAnimation();
        }

    }

    @EventHandler
    public void onCount(LobbyCountdownEvent event) {
        GameState gameState = GameServer.getInstance().getGameState();

        if (gameState != GameState.OPEN && gameState != GameState.STARTING) return;

        int count = event.getCount();

        if (GameServer.getInstance().getGameState() != GameState.STARTING) return;

        for (OSPlayer player : PlayerManager.getInstance().players.values()) {
            if (player.getOSBoard() != null) {
                player.getOSBoard().getBoard().getTeam("state").setSuffix("" + Message.highlightColor + count);
            }
        }
    }

    @EventHandler
    public void onJoin(OSPlayerCountUpdateEvent event) {
        GameState gameState = GameServer.getInstance().getGameState();

        if (gameState != GameState.OPEN && gameState != GameState.STARTING) return;

        for (OSPlayer player : PlayerManager.getInstance().players.values()) {
            if (player.getOSBoard() != null) {
                player.getOSBoard().getBoard().getTeam("playercount").setSuffix("" + Message.highlightColor + event.getCount());
            }
        }
    }
}
