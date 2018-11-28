package com.spraxs.oorlogsimulatie.scoreboard;

import com.spraxs.oorlogsimulatie.game.ingame.GameManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.Message;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class ScoreboardManager {
    private static @Getter ScoreboardManager instance;

    public ScoreboardManager() {
        instance = this;
        //Hub.getInstance().registerListener(new ScoreboardListeners());
    }

    public void setupLobbyScoreboard(OSPlayer player) {
        OSBoard board = new OSBoard(10);

        // --- Player Count ---

        board.addBlankLine();

        Team count = board.getBoard().registerNewTeam("playercount");
        count.addEntry(ChatColor.RED.toString());
        count.setPrefix(ChatColor.GRAY  + " Speler(s): ");
        count.setSuffix("" + Message.highlightColor + PlayerManager.getInstance().players.size());

        board.addLine(ChatColor.RED.toString());

        // --- Team ---

        board.addBlankLine();

        Team team = board.getBoard().registerNewTeam("team");
        team.addEntry(ChatColor.GREEN.toString());
        team.setPrefix(ChatColor.GRAY  + " Team: ");
        team.setSuffix(ChatColor.DARK_GRAY + "geen");

        board.addLine(ChatColor.GREEN.toString());

        // --- Status ---

        board.addBlankLine();

        Team state = board.getBoard().registerNewTeam("state");
        state.addEntry(ChatColor.YELLOW.toString());
        state.setPrefix(ChatColor.GRAY + " Status: ");
        state.setSuffix(Message.highlightColor + "wachten");

        board.addLine(ChatColor.YELLOW.toString());

        board.addBlankLine();

        // TODO Maak dat je ziet op welke dingen gevote zijn.

        // --- Link ---

        board.addLine(ChatColor.YELLOW + " play.zoutepopcorn.net");

        // --- Setup Teams ---

        this.setupTeams(board.getBoard());

        player.setOSBoard(board);
    }

    public void setupGameScoreboard(OSPlayer player) {
        OSBoard board = new OSBoard(7);

        for (com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team team : TeamManager.getInstance().teams) {
            board.addBlankLine();

            Team teamA = board.getBoard().registerNewTeam(team.getName());
            teamA.addEntry(team.getColor().toString());
            teamA.setPrefix(team.getColor() + team.getName() + Message.defaultColor + ": ");
            teamA.setSuffix("" + Message.highlightColor + team.getAliveSize());

            board.addLine(team.getColor().toString());
        }

        // --- Game Time ---

        board.addBlankLine();

        Team gameTimer = board.getBoard().registerNewTeam("gameTimer");
        gameTimer.addEntry(ChatColor.DARK_RED.toString());
        gameTimer.setPrefix(Message.defaultColor + "Speel duur: ");
        gameTimer.setSuffix("" + Message.highlightColor + time(GameManager.getInstance().getGameTime()));

        board.addLine(ChatColor.DARK_RED.toString());

        // --- Link ---

        board.addBlankLine();

        board.addLine(ChatColor.YELLOW + "play.zoutepopcorn.net");

        // --- Setup Teams ---

        this.setupTeams(board.getBoard());

        player.setOSBoard(board);
    }

    public void addAllPlayersToScoreboard(Scoreboard scoreboard) {
        for (OSPlayer player : PlayerManager.getInstance().players.values()) {
            Team team;

            if (player.getTeamID() != null) {
                String name = TeamManager.getInstance().getTeam(player.getTeamID()).getName();
                team = scoreboard.getTeam(name.substring(0, 1) + "-T");
            } else {
                team = scoreboard.getTeam("zz");
            }

            team.addEntry(player.getName());
        }
    }


    public void addPlayerToScoreboard(OSPlayer player, Scoreboard scoreboard) {
        Team team;

        if (player.getTeamID() != null) {
            String name = TeamManager.getInstance().getTeam(player.getTeamID()).getName();
            team = scoreboard.getTeam(name.substring(0, 1) + "-T");
        } else {
            team = scoreboard.getTeam("zz");
        }

        team.addEntry(player.getName());
    }

    public void removePlayerFromScoreboard(OSPlayer player, Scoreboard scoreboard) {
        Team team;

        if (player.getTeamID() != null) {
            String name = TeamManager.getInstance().getTeam(player.getTeamID()).getName();

            team = scoreboard.getTeam(name.substring(0, 1) + "-T");
        } else {
            team = scoreboard.getTeam("zz");
        }

        team.removeEntry(player.getName());
    }

    private void setupTeams(Scoreboard scoreboard) {

        for (com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team team : TeamManager.getInstance().teams) {
            String name = team.getName();
            Team t = scoreboard.registerNewTeam(name.substring(0, 1) + "-T");
            t.setPrefix(team.getColor() + "[" + t.getName().substring(0, 1) + "] ");
        }

        Team c = scoreboard.registerNewTeam("zz");
        c.setPrefix(ChatColor.DARK_GRAY + "");
    }

    private String time(int seconds) {
        int second =  Math.round(seconds);
        int minute = Math.round(second/60);
        int hour =  Math.round(minute/60);

        int s = second - minute * 60;
        int m = minute - hour * 60;

        int length = (int)(Math.log10(s)+1);

        if (length == 1) {
            String time = "" + m + ":0" + s;
            return time;
        } else if (s == 0) {
            String time = "" + m + ":00";
            return time;
        }

        String time = "" + m + ":" + s;
        return time;
    }
}
