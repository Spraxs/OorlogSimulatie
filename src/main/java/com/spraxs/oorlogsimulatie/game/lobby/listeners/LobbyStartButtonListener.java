package com.spraxs.oorlogsimulatie.game.lobby.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.lobby.LobbyManager;
import com.spraxs.rank.api.RankAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbyStartButtonListener implements Listener {

    public LobbyStartButtonListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
        this.rankAPI = RankAPI.getAPI();
        this.lobbyManager = LobbyManager.getInstance();
        this.gameServer = GameServer.getInstance();
    }

    private RankAPI rankAPI;
    private LobbyManager lobbyManager;
    private GameServer gameServer;

    @EventHandler
    public void onClickButton(PlayerInteractEvent event) {
       /*if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getClickedBlock().getType() != Material.STONE_BUTTON) return;

        Player player = event.getPlayer();

        if (!this.rankAPI.getRank(player).hasPermission(Type.VIP)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Je hebt minimaal de rank " + Type.VIP.getDisplayName() + ChatColor.RED + " nodig om het spel te kunnen starten!");
            return;
        }

        if (this.gameServer.getGameState() != GameState.OPEN) return;
        this.lobbyManager.startCountdown();*/

    }
}
