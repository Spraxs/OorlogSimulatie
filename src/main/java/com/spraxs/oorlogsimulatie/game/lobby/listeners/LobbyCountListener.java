package com.spraxs.oorlogsimulatie.game.lobby.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.LobbyManager;
import com.spraxs.oorlogsimulatie.utils.events.OSPlayerCountUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class LobbyCountListener implements Listener {

    public LobbyCountListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onCountUpdate(OSPlayerCountUpdateEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.OPEN && GameServer.getInstance().getGameState() != GameState.STARTING) return;

        if (event.getCount() < 3) {
            if (LobbyManager.getInstance().isCounting()) {
                LobbyManager.getInstance().setCountdownCancelled(true);

                GameServer.getInstance().setGameState(GameState.OPEN);
            }
        }
    }
}
