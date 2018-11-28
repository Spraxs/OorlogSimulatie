package com.spraxs.oorlogsimulatie.game.lobby.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class LobbyHunger implements Listener {

    public LobbyHunger() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.OPEN && GameServer.getInstance().getGameState() != GameState.STARTING) return;

        if (event.getFoodLevel() < 20) {
            event.setFoodLevel(20);
        }

        event.setCancelled(true);
    }
}
