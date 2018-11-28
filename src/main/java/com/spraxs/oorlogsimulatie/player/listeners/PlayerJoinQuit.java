package com.spraxs.oorlogsimulatie.player.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.data.Configuration;
import com.spraxs.oorlogsimulatie.data.OSFile;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.rank.player.profiles.Rank;
import com.spraxs.rank.utils.event.RankUpdateEvent;
import net.zoutepopcorn.core.utils.events.MainProfileLoadedEvent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class PlayerJoinQuit implements Listener {

    public PlayerJoinQuit() {

        OorlogSimulatiePlugin.getInstance().registerListener(this);

        this.playerManager = PlayerManager.getInstance();

        OSFile file = Configuration.getInstance().getFile(Configuration.LOCATIONS);
        FileConfiguration config = file.getConfig();

        double xA = config.getDouble("Spawn.Lobby.X");
        double yA = config.getDouble("Spawn.Lobby.Y");
        double zA = config.getDouble("Spawn.Lobby.Z");
        Vector vA = config.getVector("Spawn.Lobby.V");
        World wA = Bukkit.getWorld(config.getString("Spawn.Lobby.W"));

        this.spawnLobby = new Location(wA, xA, yA, zA);
        this.spawnLobby.setDirection(vA);

        double xS = config.getDouble("Spawn.Spectator.X");
        double yS = config.getDouble("Spawn.Spectator.Y");
        double zS = config.getDouble("Spawn.Spectator.Z");
        Vector vS = config.getVector("Spawn.Spectator.V");
        World wS = Bukkit.getWorld(config.getString("Spawn.Spectator.W"));

        this.spectator = new Location(wS, xS, yS, zS);
        this.spectator.setDirection(vS);
    }

    private Location spawnLobby;
    private Location spectator;
    private PlayerManager playerManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        event.getPlayer().getInventory().setHelmet(null);
        event.getPlayer().getInventory().setChestplate(null);
        event.getPlayer().getInventory().setLeggings(null);
        event.getPlayer().getInventory().setBoots(null);

        event.getPlayer().getInventory().clear();

        event.getPlayer().getActivePotionEffects().clear();

        event.getPlayer().setWalkSpeed((float) 0.2);

        if (GameServer.getInstance().getGameState() != GameState.STOPPING) {

            Player player = event.getPlayer();

            if (GameServer.getInstance().getGameState() == GameState.BUSY) {
                player.teleport(this.spectator);
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getUniqueId() == player.getUniqueId()) continue;

                p.showPlayer(player);
                player.showPlayer(p);
            }

            player.teleport(this.spawnLobby);

            this.playerManager.loadOSPlayerStats(player);

            player.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=[ OS Tip ]=-=-=-=-=-=-");
            player.sendMessage(ChatColor.YELLOW + "Voor de verdedigende partij:");
            player.sendMessage(ChatColor.GRAY + "Verdedig het fort en verdien 150% van");
            player.sendMessage(ChatColor.GRAY + "het gold dat je ontvangt.");
            player.sendMessage(ChatColor.GREEN + "Voor de aanvallende partij:");
            player.sendMessage(ChatColor.GRAY + "Verover het fort en verdien 150% van");
            player.sendMessage(ChatColor.GRAY + "het gold dat je ontvangt.");
            player.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=[ OS Tip ]=-=-=-=-=-=-");

        } else {
            //TODO SEND PLAYER TO HUB

            event.getPlayer().kickPlayer(ChatColor.RED + "Dit spel is aan het afronden.\nHet is dus niet mogelijk om te joinen!");

        }
    }

    @EventHandler
    public void onLoaded(MainProfileLoadedEvent event) {
        this.playerManager.loadPlayerData(event.getPlayer(), event.getMainProfile().getMainUserEntity());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
       // if (GameServer.getInstance().getGameState() != GameState.BUSY) return;

        event.setQuitMessage(null);

        event.getPlayer().getInventory().setHelmet(null);
        event.getPlayer().getInventory().setChestplate(null);
        event.getPlayer().getInventory().setLeggings(null);
        event.getPlayer().getInventory().setBoots(null);

        event.getPlayer().getInventory().clear();

        OSPlayer player = this.playerManager.getPlayer(event.getPlayer());

        this.playerManager.saveAndRemoveOSPlayerStats(player);
    }

    @EventHandler
    public void onRank(RankUpdateEvent event) {
        Rank rank = event.getRank();
        OSPlayer player = this.playerManager.getPlayer(rank.getPlayer().getPlayer());

        player.setRankLoaded(true);
    }
}
