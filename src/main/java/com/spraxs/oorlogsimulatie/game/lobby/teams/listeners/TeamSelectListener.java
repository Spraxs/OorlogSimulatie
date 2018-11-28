package com.spraxs.oorlogsimulatie.game.lobby.teams.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Spraxs
 * Date: 4-11-2018
 */

public class TeamSelectListener implements Listener {

    public TeamSelectListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.OPEN && GameServer.getInstance().getGameState() != GameState.STARTING)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() == null) return;

            Team team = null;

            for (Team t : TeamManager.getInstance().teams) {
                if (event.getItem().getType() == t.getMaterial()) {
                    if (ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase(t.getName())) {
                        team = t;
                    }
                }
            }

            if (team == null) return;

            TeamManager.getInstance().tryJoin(PlayerManager.getInstance().getPlayer(event.getPlayer()), team);
        }
    }
}
