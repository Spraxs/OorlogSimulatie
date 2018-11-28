package com.spraxs.oorlogsimulatie.game.lobby.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class LobbyDamage implements Listener {

    public LobbyDamage() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        GameState gameState = GameServer.getInstance().getGameState();
        if (gameState != GameState.OPEN && gameState != GameState.STARTING) return;

        event.setCancelled(true);
    }
}
