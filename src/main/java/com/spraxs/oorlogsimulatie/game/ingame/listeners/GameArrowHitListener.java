package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.utils.ParticleEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class GameArrowHitListener implements Listener {

    public GameArrowHitListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onLand(ProjectileHitEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.BUSY) return;
        if (event.getEntity().getShooter() == null) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        ParticleEffect.SMOKE_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 4, event.getEntity().getLocation(), 100);
        event.getEntity().remove();
    }
}