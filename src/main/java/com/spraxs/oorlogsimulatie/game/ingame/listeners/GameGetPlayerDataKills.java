package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.utils.events.OSPlayerLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameGetPlayerDataKills implements Listener {


    public GameGetPlayerDataKills() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void getPlayerTop3Kills(OSPlayerLoadedEvent event) {

    }
}