package com.spraxs.oorlogsimulatie.player.data;

import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.logger.Logger;
import net.zoutepopcorn.core.api.CoreAPI;
import net.zoutepopcorn.core.database.mysql.MySQLType;
import net.zoutepopcorn.core.database.mysql.tables.module.PlayerColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class OSPlayerSaver extends DataProcessor {

    /**
     * Als je niet begrijpt wat hier staat, bekijk dan even de uitleg in de OSPlayerLoader class.
     */

    private String UPDATE_player_stats;

    private String UPDATE_leaderboard_kills;
    private String INSERT_leaderboard_kills;
    private String UPDATE_leaderboard_wins;
    private String INSERT_leaderboard_wins;

    public OSPlayerSaver(OSPlayer player) {
        super(player);
    }

    @Override
    public void queries() {
        if (!this.player.isLoaded()) return;

        this.UPDATE_player_stats = "UPDATE player_stats SET " + PlayerColumn.NAME.getName() + " = "
                + "'" + player.getName()
                + "'," + "kills = '" + this.player.getStatsProfile().getKills()
                + "'," + "deaths = '" + this.player.getStatsProfile().getDeaths()
                + "'," + "wins = '" + this.player.getStatsProfile().getWins()
                + "'," + "loses = '" + this.player.getStatsProfile().getLoses()
                + "'," + "kill_streak = '" + this.player.getStatsProfile().getKillStreak()
                + "' " + "WHERE " + PlayerColumn.UUID.getName() + " = '" + player.getUuid() + "'";

        this.INSERT_leaderboard_kills = "INSERT INTO leaderboard_kills (" + PlayerColumn.UUID.getName() + "," + PlayerColumn.NAME.getName() + "," + "kills" + ") " +
                "VALUES ('" + player.getUuid() +
                "','" + player.getName() +
                "','" + 0 +
                "') ON DUPLICATE KEY UPDATE " + PlayerColumn.NAME.getName() + "='" + player.getName() + "'";

        this.UPDATE_leaderboard_kills = "UPDATE leaderboard_kills SET " + PlayerColumn.NAME.getName() + " = "
                + "'" + player.getName()
                + "'," + "kills = '" + this.player.getStatsProfile().getKills()
                + "' " + "WHERE " + PlayerColumn.UUID.getName() + " = '" + player.getUuid() + "'";


        this.INSERT_leaderboard_wins = "INSERT INTO leaderboard_wins (" + PlayerColumn.UUID.getName() + "," + PlayerColumn.NAME.getName() + "," + "wins" + ") " +
                "VALUES ('" + player.getUuid() +
                "','" + player.getName() +
                "','" + 0 +
                "') ON DUPLICATE KEY UPDATE " + PlayerColumn.NAME.getName() + "='" + player.getName() + "'";

        this.UPDATE_leaderboard_wins = "UPDATE leaderboard_wins SET " + PlayerColumn.NAME.getName() + " = "
                + "'" + player.getName()
                + "'," + "wins = '" + this.player.getStatsProfile().getWins()
                + "' " + "WHERE " + PlayerColumn.UUID.getName() + " = '" + player.getUuid() + "'";
    }

    @Override
    public void process() {
        Logger.INFO.log("Check if loaded...");
        if (!this.player.isLoaded()) return;
        Logger.INFO.log("Is loaded!");

        Connection connection = null;
        PreparedStatement pStatement = null;

        try {
            connection = CoreAPI.getAPI().getMySQLDataSource(MySQLType.OORLOGSIMULATIE).getConnection();

            pStatement = connection.prepareStatement(this.UPDATE_player_stats);
            pStatement.execute();

            pStatement = connection.prepareStatement(this.INSERT_leaderboard_kills);
            pStatement.execute();

            pStatement = connection.prepareStatement(this.UPDATE_leaderboard_kills);
            pStatement.execute();

            pStatement = connection.prepareStatement(this.INSERT_leaderboard_wins);
            pStatement.execute();

            pStatement = connection.prepareStatement(this.UPDATE_leaderboard_wins);
            pStatement.execute();

        } catch (Exception e) {
            Logger.ERROR.log("Failed to save " + player.getName());
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

        Logger.INFO.log("Saved '" + this.player.getName() + "' his stats!");
    }
}
