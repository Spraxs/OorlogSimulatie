package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GameDestroyingListener implements Listener {

    public GameDestroyingListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

    }
}