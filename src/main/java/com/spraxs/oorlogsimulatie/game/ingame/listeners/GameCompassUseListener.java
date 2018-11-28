package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.events.GameStateUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by Spraxs
 * Date: 22-10-2018
 */

public class GameCompassUseListener implements Listener {

    public GameCompassUseListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onClickCompass(PlayerInteractEvent event) {
        if (GameServer.getInstance().getGameState() != GameState.BUSY) return;
        if (event.getItem() == null) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getItem().getType() != Material.COMPASS) return;

        OSPlayer closest = null;

        for (Team team : TeamManager.getInstance().teams) {
            for (UUID uuid : team.getPlayers()) {
                OSPlayer osPlayer = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(uuid));
                if (osPlayer.getUuid().equals(event.getPlayer().getUniqueId())) continue;
                if (osPlayer.getTeamID().equals(PlayerManager.getInstance().getPlayer(event.getPlayer()).getTeamID()))
                    continue;

                if (closest == null) {
                    closest = osPlayer;
                    continue;
                }

                if (closest.getPlayer().getLocation().distanceSquared(event.getPlayer().getLocation()) > osPlayer.getPlayer().getLocation().distanceSquared(event.getPlayer().getLocation())) {
                    closest = osPlayer;
                    continue;
                }
            }
        }

        if (closest != null) {

            OSPlayer osPlayer = PlayerManager.getInstance().getPlayer(event.getPlayer());

            osPlayer.setCompassTarget(closest);
            osPlayer.getPlayer().setCompassTarget(closest.getPlayer().getLocation());
            String message = ChatColor.YELLOW + closest.getCompassTarget().getPlayer().getDisplayName()
                    + ChatColor.GREEN + " is " + ChatColor.YELLOW + closest.getPlayer().getLocation().distanceSquared(osPlayer.getPlayer().getLocation()) + ChatColor.GREEN + " blocks van je vandaan!";

            osPlayer.sendActionBar(message);
        }
    }

    @EventHandler
    public void onCompassStart(GameStateUpdateEvent event) {
        if (event.getGameState() != GameState.BUSY) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (GameServer.getInstance().getGameState() != GameState.BUSY) {
                    cancel();
                    return;
                }

                for (Team team : TeamManager.getInstance().teams) {
                    for (UUID uuid : team.getPlayers()) {
                        OSPlayer osPlayer = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(uuid));
                        if (osPlayer.getCompassTarget() == null) continue;
                        if (osPlayer.getCompassTarget().isDead() || osPlayer.getCompassTarget().getTeamID() == null || osPlayer.getCompassTarget().isNewJoin()) {
                            OSPlayer closest = null;

                            for (Team team2 : TeamManager.getInstance().teams) {
                                if (osPlayer.getTeamID() != null && osPlayer.getTeamID().equals(team2.getUuid())) continue;
                                for (UUID uuid2 : team.getPlayers()) {
                                    OSPlayer osPlayer2 = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(uuid2));
                                    if (osPlayer.getUuid().equals(osPlayer2.getPlayer().getUniqueId())) continue;
                                    if (osPlayer.getTeamID().equals(PlayerManager.getInstance().getPlayer(osPlayer2.getPlayer()).getTeamID()))
                                        continue;

                                    if (closest == null) {
                                        closest = osPlayer2;
                                        continue;
                                    }

                                    if (closest.getPlayer().getLocation().distanceSquared(osPlayer.getPlayer().getLocation()) > osPlayer.getPlayer().getLocation().distanceSquared(osPlayer.getPlayer().getLocation())) {
                                        closest = osPlayer2;
                                        continue;
                                    }
                                }
                            }
                            osPlayer.setCompassTarget(closest);
                            osPlayer.getPlayer().setCompassTarget(closest.getPlayer().getLocation());
                            String message = ChatColor.YELLOW + closest.getCompassTarget().getPlayer().getDisplayName()
                                    + ChatColor.GREEN + " is " + ChatColor.YELLOW + closest.getPlayer().getLocation().distanceSquared(osPlayer.getPlayer().getLocation()) + ChatColor.GREEN + " blocks van je vandaan!";

                            osPlayer.sendActionBar(message);
                        }
                    }
                }

//                for (OSPlayer osPlayer : PlayerManager.getInstance().players.values()) {
//                    if (osPlayer.isDead()) continue;
//                    if (osPlayer.getCompassTarget() == null) continue;
//                    if (osPlayer.getCompassTarget().isDead() || osPlayer.getPlayer().isDead() || !osPlayer.getPlayer().isOnline()) {
//                        if (osPlayer.getCompassTarget() != null)
//                            osPlayer.setCompassTarget(null);
//                        continue;
//                    }
//
//                    long distance = Math.round(osPlayer.getPlayer().getLocation().distance(osPlayer.getCompassTarget().getPlayer().getLocation()));
//
//                    String message = ChatColor.YELLOW + osPlayer.getCompassTarget().getPlayer().getDisplayName()
//                            + ChatColor.GREEN + " is " + ChatColor.YELLOW + distance + ChatColor.GREEN + " blocks van je vandaan!";
//
//                    osPlayer.sendActionBar(message);
//                }
            }
        }.runTaskTimerAsynchronously(OorlogSimulatiePlugin.getInstance(), 0L, 20L);
    }
}
