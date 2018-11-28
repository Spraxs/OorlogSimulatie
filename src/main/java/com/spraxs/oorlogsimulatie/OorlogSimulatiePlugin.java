package com.spraxs.oorlogsimulatie;

import com.spraxs.oorlogsimulatie.command.commands.*;
import com.spraxs.oorlogsimulatie.data.Configuration;
import com.spraxs.oorlogsimulatie.database.Database;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.GameType;
import com.spraxs.oorlogsimulatie.game.ingame.GameManager;
import com.spraxs.oorlogsimulatie.game.ingame.listeners.*;
import com.spraxs.oorlogsimulatie.game.listeners.PlayerChatListener;
import com.spraxs.oorlogsimulatie.game.lobby.LobbyManager;
import com.spraxs.oorlogsimulatie.game.lobby.listeners.*;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.listeners.TeamSelectListener;
import com.spraxs.oorlogsimulatie.mongo.MongoManager;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.listeners.PlayerJoinQuit;
import com.spraxs.oorlogsimulatie.scoreboard.ScoreboardManager;
import com.spraxs.oorlogsimulatie.scoreboard.listeners.GameBoardListener;
import com.spraxs.oorlogsimulatie.scoreboard.listeners.LobbyBoardListener;
import com.spraxs.oorlogsimulatie.socket.listeners.SocketUpdateListener;
import com.spraxs.oorlogsimulatie.utils.Gamedata;
import com.spraxs.oorlogsimulatie.utils.GearItems;
import com.spraxs.oorlogsimulatie.utils.WorldManager;
import com.spraxs.oorlogsimulatie.utils.logger.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class OorlogSimulatiePlugin extends JavaPlugin {

    private static @Getter
    OorlogSimulatiePlugin instance;

    //TODO Maak een mechanic dat spelers "verstijven" als ze te lang in water zijn, maybe ook nog kleine damage over een lange duur.


    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        this.setupWorldManager();

        WorldManager.getInstance().loadWorld();

        new Configuration();

        this.setupServer();

        this.setupInstances();

        this.registerListeners();

        this.registerCommands();

        //this.onChangeBorder();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Logger.INFO.log("GAME IS OPEN!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        WorldManager.getInstance().deleteWorld();

        GameServer gameServer = GameServer.getInstance();

        gameServer.setPlayers(0);
        gameServer.setGameState(GameState.CLOSED);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void setupWorldManager() {
        new WorldManager();
    }

    private void setupServer() {
        int id = Configuration.getInstance().getFile(Configuration.SERVER).getConfig().getInt("id");

        GameServer gameServer = new GameServer(id);

        gameServer.setGameState(GameState.OPEN);
        gameServer.setPlayers(0);
        gameServer.setMap("JenavaCastle");
        gameServer.setGameType(GameType.CLASSIC);

        // --- Sockets ---
        new SocketUpdateListener();
    }

    private void setupInstances() {

        // --- Database ---
        new Database();
        new MongoManager();

        // --- Items ---
        new GearItems();

        // --- Player ---
        new PlayerManager();

        // --- Lobby ---
        new LobbyManager();

        // --- Team ---
        new TeamManager();

        // --- InGame ---
        new GameManager();

        // --- Game Data ---
        new Gamedata();

        // --- Scoreboard ---
        new ScoreboardManager();

    }

    private void registerListeners() {
        new PlayerJoinQuit();
        new PlayerChatListener();

        // --- Lobby ---
        new LobbyPlayerDataLoaded();
        new LobbyBuilding();
        new LobbyDamage();
        new LobbyHunger();
        new LobbyCountListener();
        new LobbyItems();
        new LobbyForceStart();
        new TeamSelectListener();

        // --- Scoreboard ---
        new LobbyBoardListener();
        new GameBoardListener();

        // --- Game ---
        new GameDamageListener();
        new GamePlayerDataLoaded();
        new GamePreSavePlayerOnEndListener();
        new LeaderboardKillsListener();
        new LeaderboardWinsListener();
        new GameTeamUpdateListener();
        new GameArmorEditListener();
        new GameDestroyingListener();
        // new GameColdWaterListener(); //TODO Deze class is disabled omdat het momenteel niet gebruikt zal worden.
        new GameArrowHitListener();
        new GameCraftEditListener();
        new GameDropListener();

        // new GameCompassListener();
        new GameGetPlayerDataKills();
        new GameCompassUseListener();
    }

    private void registerCommands() {
        new CommandSpawn("spawn", true);
        new CommandMap("map", true);
        new CommandTest("test", true);
        new CommandHub("lobby", false);
        new CommandChatSpy("chatspy", false);
        new CommandGameType("gametype", true);
    }

    public void onChangeBorder() {
        if (GameServer.getInstance().getGameState() == GameState.OPEN) {
            Bukkit.getWorld("game_map").getWorldBorder().setSize(10000000);
        }
    }
}
