package com.spraxs.oorlogsimulatie.game.lobby.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class LobbyItems implements Listener {

    public LobbyItems() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.OPEN && GameServer.getInstance().getGameState() != GameState.STARTING)
            return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        event.setCancelled(true);
    }
}