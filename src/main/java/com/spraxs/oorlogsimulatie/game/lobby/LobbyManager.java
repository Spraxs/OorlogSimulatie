package com.spraxs.oorlogsimulatie.game.lobby;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.data.Configuration;
import com.spraxs.oorlogsimulatie.data.OSFile;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.ingame.GameManager;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.Message;
import com.spraxs.oorlogsimulatie.utils.events.lobby.LobbyCountdownEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class LobbyManager {

    private static @Getter LobbyManager instance;

    public LobbyManager() {
        instance = this;

        this.countdown = 60;

        OSFile file = Configuration.getInstance().getFile(Configuration.LOCATIONS);
        FileConfiguration config = file.getConfig();

        double xA = config.getDouble("Spawn.A.X");
        double yA = config.getDouble("Spawn.A.Y");
        double zA = config.getDouble("Spawn.A.Z");
        Vector vA = config.getVector("Spawn.A.V");
        World wA = Bukkit.getWorld(config.getString("Spawn.A.W"));

        this.spawnA = new Location(wA, xA, yA, zA);
        this.spawnA.setDirection(vA);

        double xB = config.getDouble("Spawn.B.X");
        double yB = config.getDouble("Spawn.B.Y");
        double zB = config.getDouble("Spawn.B.Z");
        Vector vB = config.getVector("Spawn.B.V");
        World wB = Bukkit.getWorld(config.getString("Spawn.B.W"));

        this.spawnB = new Location(wB, xB, yB, zB);
        this.spawnB.setDirection(vB);

        double xS = config.getDouble("Spawn.Spectator.X");
        double yS = config.getDouble("Spawn.Spectator.Y");
        double zS = config.getDouble("Spawn.Spectator.Z");
        Vector vS = config.getVector("Spawn.Spectator.V");
        World wS = Bukkit.getWorld(config.getString("Spawn.Spectator.W"));

        this.spectator = new Location(wS, xS, yS, zS);
        this.spectator.setDirection(vS);

        this.scoreboardAnimation();

        this.countdownCancelled = false;
        this.isCounting = false;

    }

    private @Getter Location spawnA;
    private @Getter Location spawnB;
    private @Getter Location spectator;

    private int countdown;

    private @Getter boolean isCounting;

    private @Setter boolean countdownCancelled;

    // --- Countdown ---

    public int getCountdown() {
        return this.countdown;
    }

    private void setCountdown(int countdown) {
        this.countdown = countdown;

        Bukkit.getPluginManager().callEvent(new LobbyCountdownEvent(this.countdown));
    }

    public void startCountdown() {

        if (GameServer.getInstance().getGameState() != GameState.STARTING) {

            //this.onChangeBorder();

            GameServer.getInstance().setGameState(GameState.STARTING);

            this.isCounting = true;

            this.countdown += 1;

            new BukkitRunnable() {

                @Override
                public void run() {

                    if (countdownCancelled) {
                        cancel();

                        countdownCancelled = false;

                        for (OSPlayer player : PlayerManager.getInstance().players.values()) {
                            if (player.isLoaded() && player.getOSBoard() != null) {
                                org.bukkit.scoreboard.Team team = player.getOSBoard().getBoard().getTeam("state");

                                team.setPrefix(ChatColor.GRAY + " Status: ");
                            }
                        }

                        countdown = 60;

                        GameServer.getInstance().setGameState(GameState.OPEN);

                        OorlogSimulatiePlugin.getInstance().onChangeBorder();

                        return;
                    }

                    int count = countdown - 1;

                    setCountdown(count);

                    for (OSPlayer player : PlayerManager.getInstance().players.values()) {
                        if (player.isLoaded() && player.getOSBoard() != null) {
                            org.bukkit.scoreboard.Team team = player.getOSBoard().getBoard().getTeam("state");

                            team.setPrefix(ChatColor.GRAY + " Start over: ");
                        }
                    }

                    if (getCountdown() <= 0) {
                        cancel();

                        Bukkit.getScheduler().runTask(OorlogSimulatiePlugin.getInstance(), () -> startGameSetup());
                    }
                }
            }.runTaskTimerAsynchronously(OorlogSimulatiePlugin.getInstance(), 0L, 20L);
        }
    }

    public void scoreboardAnimation() {

        new BukkitRunnable() {
            int phase = 0;
            @Override
            public void run() {
                if (GameServer.getInstance().getGameState() != GameState.OPEN) {
                    cancel();
                    return;
                }

                for (OSPlayer player : PlayerManager.getInstance().players.values()) {
                    if (player.isLoaded() && player.getOSBoard() != null) {
                        org.bukkit.scoreboard.Team team = player.getOSBoard().getBoard().getTeam("state");


                        switch (phase) {
                            case 0:
                                team.setSuffix(Message.highlightColor + "wachten");
                                break;
                            case 1:
                                team.setSuffix(Message.highlightColor + "wachten.");
                                break;
                            case 2:
                                team.setSuffix(Message.highlightColor + "wachten..");
                                break;
                            case 3:
                                team.setSuffix(Message.highlightColor + "wachten...");
                                break;
                        }
                    }
                }

                if (phase == 3) {
                    phase = 0;
                } else {
                    phase++;
                }
            }
        }.runTaskTimerAsynchronously(OorlogSimulatiePlugin.getInstance(), 0L, 20L);
    }

    private void startGameSetup() {

        GameManager.getInstance().start();
    }

    private void onChangeBorder() {
        Bukkit.getWorld("game_map").getWorldBorder().setSize(450);
    }
}
