package com.spraxs.oorlogsimulatie.game.lobby.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.LobbyManager;
import com.spraxs.rank.api.RankAPI;
import com.spraxs.rank.utils.Message;
import com.spraxs.rank.utils.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbyForceStart implements Listener {

    public LobbyForceStart() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    private LobbyManager lobbyManager;

    private int VIPplayers = 12;
    private int POPCORNplayers = 8;
    private int STAFFplayers = 2;

    @EventHandler
    public void onForceStart(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        if (GameServer.getInstance().getGameState() != GameState.STARTING && GameServer.getInstance().getGameState() != GameState.BUSY && GameServer.getInstance().getGameState() != GameState.STOPPING) {
            if (action == Action.RIGHT_CLICK_BLOCK) {
                if (block.getType() == Material.STONE_BUTTON) {
                    if (GameServer.getInstance().getGameState() == GameState.OPEN) {
                        if ((block.getLocation().add(1, 0, 0).getBlock().getType() == Material.EMERALD_BLOCK) ||
                                (block.getLocation().add(-1, 0, 0).getBlock().getType() == Material.EMERALD_BLOCK) ||
                                (block.getLocation().add(0, 0, 1).getBlock().getType() == Material.EMERALD_BLOCK) ||
                                (block.getLocation().add(0, 0, -1).getBlock().getType() == Material.EMERALD_BLOCK)) {

                            if (RankAPI.getAPI().getRank(player).getType() == Type.VIP) {
                                if (Bukkit.getOnlinePlayers().size() >= VIPplayers) {
                                    LobbyManager.getInstance().startCountdown();
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        players.sendMessage(Message.prefix + RankAPI.getAPI().getRank(player).getPrefix() + player.getName() + ChatColor.YELLOW + " heeft de game eerder gestart!");
                                    }
                                } else {
                                    player.sendMessage(Message.prefix + ChatColor.GRAY + "Je hebt minimaal " + ChatColor.YELLOW + VIPplayers + ChatColor.GRAY + " speler(s) nodig!");
                                }
                            } else if (RankAPI.getAPI().getRank(player).getType() == Type.ELITE) {
                                if (Bukkit.getOnlinePlayers().size() >= VIPplayers) {
                                    LobbyManager.getInstance().startCountdown();
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        players.sendMessage(Message.prefix + RankAPI.getAPI().getRank(player).getPrefix() + player.getName() + ChatColor.YELLOW + " heeft de game eerder gestart!");
                                    }
                                } else {
                                    player.sendMessage(Message.prefix + ChatColor.GRAY + "Je hebt minimaal " + ChatColor.YELLOW + VIPplayers + ChatColor.GRAY + " speler(s) nodig!");
                                }
                            } else if (RankAPI.getAPI().getRank(player).getType() == Type.POPCORN) {
                                if (Bukkit.getOnlinePlayers().size() >= POPCORNplayers) {
                                    LobbyManager.getInstance().startCountdown();
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        players.sendMessage(Message.prefix + RankAPI.getAPI().getRank(player).getPrefix() + player.getName() + ChatColor.YELLOW + " heeft de game eerder gestart!");
                                    }
                                } else {
                                    player.sendMessage(Message.prefix + ChatColor.GRAY + "Je hebt minimaal " + ChatColor.YELLOW + POPCORNplayers + ChatColor.GRAY + " speler(s) nodig!");
                                }
                            } else if (RankAPI.getAPI().getRank(player).getType() == Type.EINDBAAS) {
                                if (Bukkit.getOnlinePlayers().size() >= POPCORNplayers) {
                                    LobbyManager.getInstance().startCountdown();
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        players.sendMessage(Message.prefix + RankAPI.getAPI().getRank(player).getPrefix() + player.getName() + ChatColor.YELLOW + " heeft de game eerder gestart!");
                                    }
                                } else {
                                    player.sendMessage(Message.prefix + ChatColor.GRAY + "Je hebt minimaal " + ChatColor.YELLOW + POPCORNplayers + ChatColor.GRAY + " speler(s) nodig!");
                                }
                            } else if (RankAPI.getAPI().getRank(player).getType() == Type.CUSTOM) {
                                if (Bukkit.getOnlinePlayers().size() >= POPCORNplayers) {
                                    LobbyManager.getInstance().startCountdown();
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        players.sendMessage(Message.prefix + RankAPI.getAPI().getRank(player).getPrefix() + player.getName() + ChatColor.YELLOW + " heeft de game eerder gestart!");
                                    }
                                } else {
                                    player.sendMessage(Message.prefix + ChatColor.GRAY + "Je hebt minimaal " + ChatColor.YELLOW + POPCORNplayers + ChatColor.GRAY + " speler(s) nodig!");
                                }
                            } else if (RankAPI.getAPI().getRank(player).getType() == Type.YOUTUBE) {
                                if (Bukkit.getOnlinePlayers().size() >= POPCORNplayers) {
                                    LobbyManager.getInstance().startCountdown();
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        players.sendMessage(Message.prefix + RankAPI.getAPI().getRank(player).getPrefix() + player.getName() + ChatColor.YELLOW + " heeft de game eerder gestart!");
                                    }
                                } else {
                                    player.sendMessage(Message.prefix + ChatColor.GRAY + "Je hebt minimaal " + ChatColor.YELLOW + POPCORNplayers + ChatColor.GRAY + " speler(s) nodig!");
                                }
                            } else if (RankAPI.getAPI().getRank(player).hasPermission(Type.DEVELOPER)) {
                                if (Bukkit.getOnlinePlayers().size() >= STAFFplayers) {
                                    LobbyManager.getInstance().startCountdown();
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        players.sendMessage(Message.prefix + RankAPI.getAPI().getRank(player).getPrefix() + player.getName() + ChatColor.YELLOW + " heeft de game eerder gestart!");
                                    }
                                } else {
                                    player.sendMessage(Message.prefix + ChatColor.GRAY + "Je hebt minimaal " + ChatColor.YELLOW + STAFFplayers + ChatColor.GRAY + " speler(s) nodig!");
                                }
                            } else {
                                player.sendMessage(Message.prefix + ChatColor.GRAY + "Je moet hiervoor rank " + Type.VIP.getDisplayName() + ChatColor.GRAY + " of hoger zijn!");
                            }
                        }
                    }
                }
            }
        }
    }
}