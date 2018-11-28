package com.spraxs.oorlogsimulatie.game.lobby.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.LobbyManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.scoreboard.ScoreboardManager;
import com.spraxs.oorlogsimulatie.utils.Gamedata;
import com.spraxs.oorlogsimulatie.utils.events.OSPlayerLoadedEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class LobbyPlayerDataLoaded implements Listener {

    public LobbyPlayerDataLoaded() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);

        this.scoreboardManager = ScoreboardManager.getInstance();
        this.playerManager = PlayerManager.getInstance();
        this.lobbyManager = LobbyManager.getInstance();
    }
    
    private ScoreboardManager scoreboardManager;
    private PlayerManager playerManager;
    private LobbyManager lobbyManager;

    @EventHandler
    public void onLoaded(OSPlayerLoadedEvent event) {

        if (GameServer.getInstance().getGameState() != GameState.OPEN && GameServer.getInstance().getGameState() != GameState.STARTING) return;

        OSPlayer player = event.getPlayer();

        this.resetPlayer(player.getPlayer());

        player.loadLobbyBoard();

        scoreboardManager.addAllPlayersToScoreboard(player.getOSBoard().getBoard());

        for (OSPlayer p : this.playerManager.players.values()) {

            if (p.getUuid() != event.getPlayer().getUuid()) {

                if (p.isLoaded() && p.getOSBoard() != null) {
                    scoreboardManager.addPlayerToScoreboard(player, p.getOSBoard().getBoard());
                }
            }
        }

        if (this.playerManager.players.size() >= Gamedata.getInstance().getPlayersToStart()) {
            this.lobbyManager.startCountdown();
        }
    }

    private void resetPlayer(Player player) {

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);

        player.getInventory().clear();

        TeamManager.getInstance().teams.forEach(team ->  {
            ItemStack i = new ItemStack(team.getMaterial(), 1, (byte) team.getM_data());
            ItemMeta im = i.getItemMeta();

            im.setDisplayName(team.getColor() + team.getName());

            i.setItemMeta(im);

            player.getInventory().addItem(i);
        });

        for (PotionEffectType potionEffect : PotionEffectType.values()) {
            if (potionEffect != null) {
                if (player.hasPotionEffect(potionEffect)) {
                    player.removePotionEffect(potionEffect);
                }
            }
        }
    }
}