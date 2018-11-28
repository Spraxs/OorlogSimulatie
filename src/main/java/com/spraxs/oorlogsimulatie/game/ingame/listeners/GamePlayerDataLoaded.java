package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.scoreboard.ScoreboardManager;
import com.spraxs.oorlogsimulatie.utils.events.OSPlayerLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class GamePlayerDataLoaded implements Listener {

    public GamePlayerDataLoaded() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onDataLoaded(OSPlayerLoadedEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.BUSY) return;
        OSPlayer player = event.getPlayer();

        player.loadGameBoard();

        ScoreboardManager.getInstance().addAllPlayersToScoreboard(player.getOSBoard().getBoard());

        for (OSPlayer p : PlayerManager.getInstance().players.values()) {
            if (p.getUuid() != player.getUuid()) {
                p.getPlayer().hidePlayer(player.getPlayer());
            }

            if (p.isLoaded() && p.getOSBoard() != null) {
                ScoreboardManager.getInstance().addPlayerToScoreboard(player, p.getOSBoard().getBoard());
            }
        }
    }
}
