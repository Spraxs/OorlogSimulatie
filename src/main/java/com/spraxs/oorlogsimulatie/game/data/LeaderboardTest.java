package com.spraxs.oorlogsimulatie.game.data;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.events.leaderboard.LeaderboardKillsProcessedEvent;
import com.spraxs.oorlogsimulatie.utils.logger.Logger;
import net.zoutepopcorn.core.database.mysql.MySQL;
import net.zoutepopcorn.core.database.mysql.MySQLType;
import net.zoutepopcorn.core.database.mysql.tables.module.PlayerColumn;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Author: Spraxs.
 * Date: 3-3-2018.
 */

public class LeaderboardTest {

    private String table_name;
    private String QUERY_TOP;
    private String QUERY_PLAYER;
    private String QUERY_INSERT;
    private String QUERY_UPDATE;
    private String objective;
    private ArrayList<LeaderboardKills> leaderboardKillsList;
    private LeaderboardKills leaderboardKills;
    private Player player;
    private OSPlayer osPlayer;

    public LeaderboardTest() {
        this.table_name = "leaderboard_kills";
        this.objective = "kills";
        this.leaderboardKillsList = new ArrayList<>();

        this.prepareQuery();

        Bukkit.getScheduler().runTaskAsynchronously(OorlogSimulatiePlugin.getInstance(), this::getDataTop10);
    }

    public LeaderboardTest(Player player) {
        this.table_name = "leaderboard_kills";
        this.objective = "kills";
        this.leaderboardKillsList = new ArrayList<>();
        this.player = player;

        this.prepareQuery();

        Bukkit.getScheduler().runTaskAsynchronously(OorlogSimulatiePlugin.getInstance(), this::getDataPlayer);
    }

    public LeaderboardTest(OSPlayer player) {
        this.table_name = "leaderboard_kills";
        this.objective = "kills";
        this.leaderboardKillsList = new ArrayList<>();
        this.osPlayer = player;

        this.prepareQuery();

        Bukkit.getScheduler().runTaskAsynchronously(OorlogSimulatiePlugin.getInstance(), this::updateDataPlayer);
    }

    private void prepareQuery() {
        this.QUERY_TOP = "SELECT * FROM " + this.table_name + " ORDER BY " + this.objective + " DESC LIMIT 10";

        this.QUERY_PLAYER = "SELECT * FROM " + this.table_name + " ORDER BY " + this.objective + " DESC";

        this.QUERY_INSERT = "INSERT INTO " + this.table_name + "(" + PlayerColumn.UUID.getName() + "," + PlayerColumn.NAME.getName() + "," + this.objective + ") " +
                "VALUES ('" + player.getUniqueId().toString() +
                "','" + player.getName() +
                "','" + 0 +
                "') ON DUPLICATE KEY UPDATE " + PlayerColumn.NAME.getName() + "='" + player.getName() + "'";

        this.QUERY_UPDATE = "UPDATE " + this.table_name + " SET " + PlayerColumn.NAME.getName() + " = "
                + "'" + player.getName()
                + "'," + this.objective + " = '" + this.osPlayer.getStatsProfile().getKills()
                + "' " + "WHERE " + PlayerColumn.UUID.getName() + " = '" + player.getUniqueId().toString() + "'";
    }

    private void updateDataPlayer() {
        Connection connection = null;
        PreparedStatement pStatement = null;

        try {
            connection = MySQL.getInstance().getDataSource(MySQLType.OORLOGSIMULATIE).getConnection();

            // --- Manage Leaderboard ---

            pStatement = connection.prepareStatement(this.QUERY_INSERT);
            pStatement.execute();

            pStatement = connection.prepareStatement(this.QUERY_UPDATE);
            pStatement.execute();

        } catch (Exception e) {

            Logger.ERROR.log("Failed to update player in " + this.table_name);
            Logger.ERROR.log("Stack trace ---------------------------------");
            e.printStackTrace();
            Logger.ERROR.log("Stack trace ---------------------------------");

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.WARNING.log("Kon de DB Connection niet sluiten @ " + this.getClass().getName() + ":process()");
            }
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
            } catch (SQLException ex) {
                Logger.WARNING.log("Kon de DB PreparedStatement niet sluiten @ " + this.getClass().getName() + ":process()");
            }
        }
/*
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new LeaderboardKillsProcessedEvent(null, leaderboardKills));
            }
        }.runTask(OorlogSimulatiePlugin.getInstance()); */
    }

    private void getDataPlayer() {

        Connection connection = null;
        PreparedStatement pStatement = null;

        try {
            connection = MySQL.getInstance().getDataSource(MySQLType.OORLOGSIMULATIE).getConnection();

            // --- Manage Leaderboard ---

            pStatement = connection.prepareStatement(this.QUERY_PLAYER);
            ResultSet rs = pStatement.executeQuery();

            int kills;
            UUID uuid;
            String name;

            while (rs.next()) {
                uuid = UUID.fromString(rs.getString(PlayerColumn.UUID.getName()));

                if (!uuid.equals(this.player.getUniqueId())) continue;

                name = rs.getString(PlayerColumn.NAME.getName());
                kills = rs.getInt("kills");

                leaderboardKills = new LeaderboardKills(rs.getRow(), uuid, name, kills);

                break;
            }

            rs.close();
        } catch (Exception e) {

            Logger.ERROR.log("Failed to load player from " + this.table_name);
            Logger.ERROR.log("Stack trace ---------------------------------");
            e.printStackTrace();
            Logger.ERROR.log("Stack trace ---------------------------------");

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.WARNING.log("Kon de DB Connection niet sluiten @ " + this.getClass().getName() + ":process()");
            }
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
            } catch (SQLException ex) {
                Logger.WARNING.log("Kon de DB PreparedStatement niet sluiten @ " + this.getClass().getName() + ":process()");
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new LeaderboardKillsProcessedEvent(null, leaderboardKills));
            }
        }.runTask(OorlogSimulatiePlugin.getInstance());
    }

    private void getDataTop10() {

        Connection connection = null;
        PreparedStatement pStatement = null;

        try {
            connection = MySQL.getInstance().getDataSource(MySQLType.OORLOGSIMULATIE).getConnection();

            // --- Manage Leaderboard ---

            pStatement = connection.prepareStatement(this.QUERY_TOP);
            ResultSet rs = pStatement.executeQuery();

            int id = 0;

            int kills;
            UUID uuid;
            String name;

            while (rs.next()) {
                uuid = UUID.fromString(rs.getString(PlayerColumn.UUID.getName()));
                name = rs.getString(PlayerColumn.NAME.getName());
                kills = rs.getInt("kills");

                id++;

                Bukkit.broadcastMessage("Number " + id + " has kills: " + kills + ". Row number: " + rs.getRow());
                this.leaderboardKillsList.add(new LeaderboardKills(rs.getRow(), uuid, name, kills));
            }

            rs.close();
        } catch (Exception e) {

            Logger.ERROR.log("Failed to load top 10 from " + this.table_name);
            Logger.ERROR.log("Stack trace ---------------------------------");
            e.printStackTrace();
            Logger.ERROR.log("Stack trace ---------------------------------");

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.WARNING.log("Kon de DB Connection niet sluiten @ " + this.getClass().getName() + ":process()");
            }
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
            } catch (SQLException ex) {
                Logger.WARNING.log("Kon de DB PreparedStatement niet sluiten @ " + this.getClass().getName() + ":process()");
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new LeaderboardKillsProcessedEvent(leaderboardKillsList, null));
            }
        }.runTask(OorlogSimulatiePlugin.getInstance());
    }
}
