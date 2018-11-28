package com.spraxs.oorlogsimulatie.game.lobby.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class LobbyBuilding implements Listener {

    public LobbyBuilding() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.OPEN && GameServer.getInstance().getGameState() != GameState.STARTING) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.OPEN && GameServer.getInstance().getGameState() != GameState.STARTING) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        event.setCancelled(true);
    }
}
