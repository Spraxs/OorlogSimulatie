package com.spraxs.oorlogsimulatie.socket.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.utils.events.OSPlayerCountUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SocketUpdateListener implements Listener {

    public SocketUpdateListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);

        gameServer = GameServer.getInstance();
    }

    private GameServer gameServer;

    @EventHandler
    public void onCountUpdate(OSPlayerCountUpdateEvent event) {

        this.gameServer.setPlayers(event.getCount());
    }
}
