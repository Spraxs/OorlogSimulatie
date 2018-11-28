package com.spraxs.oorlogsimulatie.database;

import net.zoutepopcorn.core.database.mysql.MySQLType;
import net.zoutepopcorn.core.database.mysql.tables.module.PlayerColumn;
import net.zoutepopcorn.core.database.mysql.tables.module.Table;
import net.zoutepopcorn.core.database.mysql.tables.variables.DatabaseVariable;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public final class Database {

    public Database() {
        Table player_stats = new Table(MySQLType.OORLOGSIMULATIE, "player_stats", true);

        player_stats.addColumn("kills", DatabaseVariable.MEDIUMINT);
        player_stats.addColumn("deaths", DatabaseVariable.MEDIUMINT);
        player_stats.addColumn("wins", DatabaseVariable.SMALLINT);
        player_stats.addColumn("loses", DatabaseVariable.SMALLINT);
        player_stats.addColumn("kill_streak", DatabaseVariable.TINYINT);

        player_stats.create();

        Table leaderboard_kills = new Table(MySQLType.OORLOGSIMULATIE, "leaderboard_kills");

        leaderboard_kills.addColumn(PlayerColumn.UUID.getName(), DatabaseVariable.VARCHAR, 36);
        leaderboard_kills.addColumn(PlayerColumn.NAME.getName(), DatabaseVariable.VARCHAR, 16);
        leaderboard_kills.addColumn("kills", DatabaseVariable.MEDIUMINT);

        leaderboard_kills.create();

        Table leaderboard_wins = new Table(MySQLType.OORLOGSIMULATIE, "leaderboard_wins");

        leaderboard_wins.addColumn(PlayerColumn.UUID.getName(), DatabaseVariable.VARCHAR, 36);
        leaderboard_wins.addColumn(PlayerColumn.NAME.getName(), DatabaseVariable.VARCHAR, 16);
        leaderboard_wins.addColumn("wins", DatabaseVariable.MEDIUMINT);

        leaderboard_wins.create();
    }
}
