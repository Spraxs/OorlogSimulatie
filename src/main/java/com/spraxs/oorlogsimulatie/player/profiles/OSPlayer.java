package com.spraxs.oorlogsimulatie.player.profiles;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.ingame.GameManager;
import com.spraxs.oorlogsimulatie.game.lobby.LobbyManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team;
import com.spraxs.oorlogsimulatie.mongo.collections.GearUserEntity;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.scoreboard.OSBoard;
import com.spraxs.oorlogsimulatie.scoreboard.ScoreboardManager;
import com.spraxs.oorlogsimulatie.utils.Message;
import com.spraxs.oorlogsimulatie.utils.Title;
import com.spraxs.oorlogsimulatie.utils.events.TeamUpdateEvent;
import com.spraxs.oorlogsimulatie.utils.events.game.OSPlayerDeathASyncEvent;
import com.spraxs.oorlogsimulatie.utils.events.game.OSPlayerDeathSyncEvent;
import com.spraxs.rank.player.profiles.Rank;
import lombok.Getter;
import lombok.Setter;
import net.zoutepopcorn.core.database.mongo.collections.MainUserEntity;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class OSPlayer {

    private Player player;
    private UUID uuid;
    private String name;
    private @Getter @Setter boolean chatspy;
    private @Getter @Setter boolean newJoin;

    private @Getter @Setter boolean loaded;
    private @Getter @Setter boolean statsLoaded;

    private StatsProfile statsProfile;

    private @Getter @Setter GearUserEntity gearUserEntity;
    private @Getter @Setter MainUserEntity mainUserEntity;

    private OSBoard osBoard;

    private boolean rankLoaded;

    private @Getter OSPlayer lastDamaged;

    private long lastDamagedTime;

    private @Getter UUID teamID; // TODO Sla dit later op als het team object zelf.

    private @Getter long gold;

    private @Setter @Getter OSPlayer compassTarget;

    private @Getter @Setter boolean dead;

    public OSPlayer(Player player) {
        this.player = player;
        this.uuid = this.player.getUniqueId();
        this.name = this.player.getName();
        this.loaded = false;
        this.statsLoaded = false;

        this.dead = false;
        this.lastDamaged = null;
        this.teamID = null;
    }

    // --- Last Damaged ---

    public void setLastDamaged(OSPlayer osPlayer) {
        lastDamaged = osPlayer;
        lastDamagedTime = System.currentTimeMillis();
    }

    // --- Gold ---

    public void setGold(long gold) {
        this.gold = gold;
    }

    public String addGold(long gold) {

        double value = gold;

        if (isPlayerInRed()) {
            value = (value / 100) * 110;
        }

        value = Math.round(value);

        this.gold += value;

        return "" + gold + " (+" + Math.round((value / 110) * 10) + ")";
    }

    public void removeGold(long gold) {
        this.gold -= gold;
    }

    // --- Rank ---

    public boolean isRankLoaded() {
        return rankLoaded;
    }

    public void setRankLoaded(boolean isRankLoaded) {
        this.rankLoaded = isRankLoaded;
    }

    // --- Team ---

    public void setTeam(Team team) {

        if (team == null) {

            if (this.teamID != null) {

                TeamManager.getInstance().getTeam(this.teamID).players.remove(this.uuid);

                this.teamID = null;

                Bukkit.getPluginManager().callEvent(new TeamUpdateEvent(this));
            }

            return;
        }

        if (this.teamID != null) {
            if (this.teamID == team.getUuid()) return;

            TeamManager.getInstance().getTeam(this.teamID).players.remove(this.uuid);
        }

        team.players.add(this.uuid);
        this.teamID = team.getUuid();

        sendInfo("Je zit nu in team '" + team.getColor() + team.getName() + ChatColor.GRAY + "'!");

        Bukkit.getPluginManager().callEvent(new TeamUpdateEvent(this));
    }

    // --- StatsProfile ---

    public StatsProfile getStatsProfile() {
        return this.statsProfile;
    }

    public void setStatsProfile(StatsProfile statsProfile) {
        this.statsProfile = statsProfile;
    }

    // --- UUID ---

    public UUID getUuid() {
        return this.uuid;
    }

    // --- Player ---

    public Player getPlayer() {
        return this.player;
    }

    // --- Name ---

    public String getName() {
        return this.name;
    }

    // --- Message ---

    public void sendInfo(String msg) {
        this.player.sendMessage(Message.prefix + msg);
    }

    public void sendMessage(String msg) {
        this.player.sendMessage(msg);
    }

    // --- OS Scoreboard ---

    public void setOSBoard(OSBoard osBoard) {
        this.osBoard = osBoard;

        player.setScoreboard(this.osBoard.getBoard());
    }

    public OSBoard getOSBoard() {
        return this.osBoard;
    }

    public void loadLobbyBoard() {
        ScoreboardManager.getInstance().setupLobbyScoreboard(this);
    }

    public void loadGameBoard() {
        ScoreboardManager.getInstance().setupGameScoreboard(this);
    }

    // --- Mechanics ---

    public void die(OSPlayer killer) {
        this.dead = true;

        if (killer == null) {

            // Hier is de tijd van de last hit cooldown
            long time = lastDamagedTime + (1000 * 60);

            //if (System.currentTimeMillis() > time) {
                killer = lastDamaged;
            //}
        }

        if (this.teamID != null) {
            for (Team t2 : TeamManager.getInstance().teams) {
                if (t2.getUuid().equals(this.teamID)) continue;
                t2.getPlayers().stream().filter(uuid1 -> {
                    OSPlayer osPlayer = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(uuid1));
                    return osPlayer.compassTarget.getUuid().equals(osPlayer.getUuid());
                });
            }
            Team t = TeamManager.getInstance().getTeam(this.teamID);
            if (killer != null && killer.getTeamID() != null) {
                Team kTeam = TeamManager.getInstance().getTeam(killer.getTeamID());

                Bukkit.broadcastMessage(t.getColor() + this.getName() + Message.defaultColor + " is vermoord door " + kTeam.getColor() + killer.getName() + Message.defaultColor + ".");
            } else {
                Bukkit.broadcastMessage(t.getColor() + this.getName() + Message.defaultColor + " is overleden.");
            }
        } else {
            if (killer != null) {
                Bukkit.broadcastMessage(Message.highlightColor + this.getName() + Message.defaultColor + " is vermoord door " + ChatColor.RED + killer.getName() + Message.defaultColor + ".");
            } else {
                Bukkit.broadcastMessage(Message.highlightColor + this.getName() + Message.defaultColor + " is overleden.");
            }
        }

        if (killer != null) {
            StatsProfile statsProfile = killer.getStatsProfile();
            statsProfile.setKills(statsProfile.getKills() + 1);
        }

        StatsProfile statsProfile = this.getStatsProfile();

        statsProfile.setDeaths(statsProfile.getDeaths() + 1);

        GameManager gameManager = GameManager.getInstance();

        this.getPlayer().setHealth(20);

        for (OSPlayer player : PlayerManager.getInstance().players.values()) {
            if (player.getUuid() != this.getUuid()) {
                player.getPlayer().hidePlayer(this.getPlayer());
            }
        }

        this.getPlayer().setGameMode(GameMode.SPECTATOR);
        this.player.getPlayer().teleport(LobbyManager.getInstance().getSpectator());

        this.getPlayer().setVelocity(new Vector().zero());

        if (gameManager.isGameOver()) {

            /**
             * Update het scoreboard voordat we het spel op stop zetten, anders is de GameState STOPPED en we willen dat deze code afgaat als de GaneState nog BUSY is.
             */
            Bukkit.getPluginManager().callEvent(new OSPlayerDeathSyncEvent(this));

            gameManager.stop();
        } else {

            /**
             * Omdat het spel toch nog niet is afgelopen, spelen we het event ASync af. Hierdoor kunnen andere dingen die in de game gebeuren meteen hun ding doen.
             * We doen dit event ook het liefst ASync, omdat hij door alle spelers looped in de GameBoardListener.
             */

            // TODO Test of dit wel werkt als 2 mensen te gelijk doodgaan.
            Bukkit.getScheduler().runTaskAsynchronously(OorlogSimulatiePlugin.getInstance(), () -> Bukkit.getPluginManager().callEvent(new OSPlayerDeathASyncEvent(this)));
        }
    }

    private boolean isPlayerInRed() {
        Location loc = this.player.getLocation().clone();
        loc.setY(0);

        return (loc.getBlock().getType() == Material.WOOL) && (loc.getBlock().getData() == 3);
    }

    public void sendActionBar(String message) {
        new Title(this.player, message);
    }

    public void chat(AsyncPlayerChatEvent event, Rank rank) {
        if (this.teamID == null) {
            if (this.dead) {
                event.setFormat(ChatColor.DARK_GRAY + "{Dood} " + rank.getPrefix() + player.getName() + ChatColor.DARK_GRAY + " » "
                        + rank.getType().getChatColor() + event.getMessage());
                return;
            }
            event.setFormat(ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + event.getMessage());
            List l = event.getRecipients().stream().filter(player1 -> !PlayerManager.getInstance().getPlayer(player1).chatspy).filter(player1 -> PlayerManager.getInstance().getPlayer(player1).getTeamID() != null).collect(Collectors.toList());
            l.forEach(o -> event.getRecipients().remove(o));
            return;
        }

        Team team = TeamManager.getInstance().getTeam(this.teamID);

        if (this.dead) {
            event.setFormat(ChatColor.DARK_GRAY + "{Dood} " + team.getColor() + "[" + team.getName() + "] " + rank.getPrefix() + player.getName() + ChatColor.DARK_GRAY + " » "
                    + rank.getType().getChatColor() + event.getMessage());
            List l = event.getRecipients().stream().filter(player1 -> !PlayerManager.getInstance().getPlayer(player1).chatspy).filter(player1 -> !PlayerManager.getInstance().getPlayer(player1).isDead()).collect(Collectors.toList());
            l.forEach(o -> event.getRecipients().remove(o));
            return;
        }

        event.setFormat(team.getColor() + "[" + team.getName() + "] " + rank.getPrefix() + player.getName() + ChatColor.DARK_GRAY + " » "
                + rank.getType().getChatColor() + event.getMessage());
        List l = event.getRecipients().stream().filter(player1 -> !PlayerManager.getInstance().getPlayer(player1).chatspy).filter(player1 -> PlayerManager.getInstance().getPlayer(player1).getTeamID() != this.teamID).collect(Collectors.toList());
        l.forEach(o -> event.getRecipients().remove(o));
    }
}
