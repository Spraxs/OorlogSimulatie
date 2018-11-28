package com.spraxs.oorlogsimulatie.player.data;

import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.player.profiles.StatsProfile;
import com.spraxs.oorlogsimulatie.utils.logger.Logger;
import net.zoutepopcorn.core.api.CoreAPI;
import net.zoutepopcorn.core.database.mysql.MySQLType;
import net.zoutepopcorn.core.database.mysql.tables.module.PlayerColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class OSPlayerLoader extends DataProcessor {

    /**
     * Hier slaan we de queries op.
     */

    private String SELECT_player_stats;
    private String INSERT_player_stats;

    public OSPlayerLoader(OSPlayer player) {
        super(player);
    }

    @Override
    public void queries() {
        this.SELECT_player_stats = "SELECT kills,deaths,wins,loses,kill_streak FROM player_stats WHERE " + PlayerColumn.UUID.getName() + "='" + player.getUuid().toString() + "'";

        this.INSERT_player_stats = "INSERT INTO player_stats(" + PlayerColumn.UUID.getName() + "," + PlayerColumn.NAME.getName() + "," + "kills" + "," + "deaths" + "," + "wins"
                + "," + "loses" + "," + "kill_streak" + ") " +
                "VALUES ('" + player.getUuid() +
                "','" + player.getName() +
                "','" + 0 +
                "','" + 0 +
                "','" + 0 +
                "','" + 0 +
                "','" + 0 +
                "') ON DUPLICATE KEY UPDATE " + PlayerColumn.NAME.getName() + "='" + player.getName() + "'";
    }

    @Override
    public void process() {

        boolean loaded = true;

        Connection connection = null;
        PreparedStatement pStatement = null; /** Zorg dat je altijd PreparedStatements gebruikt, anders kunnen mensen MySQL injections uitvoeren */
        try {
            connection = CoreAPI.getAPI().getMySQLDataSource(MySQLType.OORLOGSIMULATIE).getConnection();

            // --- Manage Player Profile ---

            pStatement = connection.prepareStatement(this.INSERT_player_stats); /** Dit voert de INSERT_player_stats query uit. */
            pStatement.execute();


            pStatement = connection.prepareStatement(this.SELECT_player_stats); /** Dit voert de SELECT_player_stats query uit. */
            ResultSet rs = pStatement.executeQuery();

            /** Hier maken wij variables om de data in op te slaan die we uit de database halen */

            int kills = -1;
            int deaths = -1;
            int wins = -1;
            int loses = -1;
            int kill_streak = -1;

            if (rs.next()) { /** Hier loopen wij door alle data die we hebben opgevraagd in de SELECT_player_stats query */

                /** Hier vullen wij de variables in met de data die we uit de database hebben getrokken */

                kills = rs.getInt("kills");
                deaths = rs.getInt("deaths");
                wins = rs.getInt("wins");
                loses = rs.getInt("loses");
                kill_streak = rs.getInt("kill_streak");
            }
            StatsProfile stats;
            if (kills != -1 && deaths != -1 && wins != -1 && kill_streak != -1) {

                stats = new StatsProfile(this.player, kills, deaths, wins, loses, kill_streak);

                this.player.setStatsProfile(stats);
            } else {
                loaded = false;
            }

            rs.close();

            this.player.setStatsLoaded(loaded);

            if (!loaded) {
                this.kickPlayer("Uw data kon niet worden ingeladen!");
            }
        } catch (Exception e) { /** Deze code word uitgevoerd als er iets mis gaat in de try { } (De code hierboven) */

            Logger.ERROR.log("Failed to load " + this.player.getName());
            Logger.ERROR.log("Stack trace ---------------------------------");
            e.printStackTrace();
            Logger.ERROR.log("Stack trace ---------------------------------");

            this.kickPlayer("Uw data kon niet worden ingeladen!");

        } finally { /** Deze code word uiteindelijk uitgevoerd, dus als de try of catch over is. */
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

        if (this.player.isStatsLoaded())
            Logger.INFO.log("Loaded '" + this.player.getName() + "' his stats!");
    }
}
