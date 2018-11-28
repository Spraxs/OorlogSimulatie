package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.events.game.OSPlayerPreSaveASyncEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class GamePreSavePlayerOnEndListener implements Listener {

    public GamePreSavePlayerOnEndListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);

        this.playerManager = PlayerManager.getInstance();
    }

    private PlayerManager playerManager;

    @EventHandler
    public void onSendAway(OSPlayerPreSaveASyncEvent event) {

        OSPlayer player = event.getPlayer();

        this.playerManager.saveOSPlayerStats(player);
    }
}
