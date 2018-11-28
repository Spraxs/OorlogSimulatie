package com.spraxs.oorlogsimulatie.game;

import com.spraxs.oorlogsimulatie.data.Configuration;
import com.spraxs.oorlogsimulatie.socket.SocketThread;
import com.spraxs.oorlogsimulatie.socket.UpdateType;
import com.spraxs.oorlogsimulatie.utils.events.GameStateUpdateEvent;
import lombok.Getter;
import org.bukkit.Bukkit;

/**
 * Author: Spraxs.
 * Date: 14-4-2018.
 */

public final class GameServer {

    private static @Getter GameServer instance;

    public GameServer(int id) {
        instance = this;

        this.id = id;
        this.maxPlayers = Configuration.getInstance().getFile(Configuration.SERVER).getConfig().getInt("max_players");
        new SocketThread(UpdateType.MAX_PLAYERS, this);

        this.gameType = GameType.CUSTOM;
    }

    private @Getter int id;
    private @Getter ServerState serverState;
    private @Getter String map;
    private @Getter int players;
    private @Getter int maxPlayers;
    private @Getter GameType gameType;

    private @Getter GameState gameState;

    public void setGameType(GameType gameType) {
        this.gameType = gameType;

        new SocketThread(UpdateType.GAME_TYPE, this);
    }

    public void sendMaxPlayerSocket() {
        new SocketThread(UpdateType.MAX_PLAYERS, this);
    }

    public void setGameState(GameState state) {
        this.gameState = state;

        if (this.isGameStateImportant(state)) {

            this.serverState = state.getServerState();

            new SocketThread(UpdateType.STATE, this);
        }

        Bukkit.getPluginManager().callEvent(new GameStateUpdateEvent(this.gameState));
    }

    public void setMap(String map) {
        this.map = map;

        new SocketThread(UpdateType.MAP, this);
    }

    public void setPlayers(int players) {
        this.players = players;

        new SocketThread(UpdateType.PLAYERS, this);
    }

    private boolean isGameStateImportant(GameState state) {

        return state == GameState.OPEN || state == GameState.BUSY || state == GameState.CLOSED;
    }
}
