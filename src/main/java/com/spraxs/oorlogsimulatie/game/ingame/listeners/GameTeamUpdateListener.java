package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.ingame.GameManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team;
import com.spraxs.oorlogsimulatie.utils.events.TeamUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class GameTeamUpdateListener implements Listener {

    public GameTeamUpdateListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);

        this.gameManager = GameManager.getInstance();
    }
    private GameManager gameManager;

    /**
     * Dit event zorgt ervoor dat als de laatste speler van een team leaved dat het spel stopt
     */

    @EventHandler
    public void onTeamUpdate(TeamUpdateEvent event) {

        if (GameServer.getInstance().getGameState() != GameState.BUSY) return;

        for (Team team : TeamManager.getInstance().teams) {
            if (team.getSize() <= 0) {
                this.gameManager.stop();

                return;
            }
        }
    }
}
