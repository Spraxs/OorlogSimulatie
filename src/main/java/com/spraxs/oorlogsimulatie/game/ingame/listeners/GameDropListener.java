package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameDropListener implements Listener {


    public GameDropListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.BUSY && GameServer.getInstance().getGameState() != GameState.STOPPING && GameServer.getInstance().getGameState() != GameState.STOPPING) {
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onCraft(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (event.getClickedBlock() == null) return;
            if (event.getClickedBlock().getType() == Material.WORKBENCH) {
                event.setCancelled(true);
            }
        }

    }
}