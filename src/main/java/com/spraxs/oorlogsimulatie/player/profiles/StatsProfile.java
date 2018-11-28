package com.spraxs.oorlogsimulatie.player.profiles;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class StatsProfile extends Profile {

    public StatsProfile(OSPlayer player, int kills, int deaths, int wins, int loses, int kill_streak) {
        super(player);

        this.kills = kills;
        this.deaths = deaths;
        this.wins = wins;
        this.loses = loses;
        this.kill_streak = kill_streak;
    }

    private int kills;
    private int deaths;
    private int wins;
    private int loses;
    private int kill_streak;

    // --- Kills ---

    public int getKills() {
        return this.kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    // --- Deaths ---

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    // --- Wins ---

    public int getWins() {
        return this.wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    // --- Wins ---

    public int getLoses() {
        return this.loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    // --- Kill Streak ---

    public int getKillStreak() {
        return this.kill_streak;
    }

    public void setKillStreak(int kill_streak) {
        this.kill_streak = kill_streak;
    }

}
