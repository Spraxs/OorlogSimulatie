package com.spraxs.oorlogsimulatie.socket;

import com.spraxs.oorlogsimulatie.game.GameServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Author: Spraxs.
 * Date: 14-4-2018.
 */

public class SocketThread {

    private Socket socket;
    private UpdateType updateType;
    private GameServer gameServer;

    public SocketThread(UpdateType updateType, GameServer gameServer) {

        this.updateType = updateType;
        this.gameServer = gameServer;

        new Thread(() -> {
            try {
                this.socket = new Socket("localhost", 5647); /* <- Top secret IP, dus 'ssssst' */

                this.run();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    private void run() throws IOException {
        String value = null;

        switch (this.updateType) {
            case STATE:
                value = this.gameServer.getGameState().getServerState().getName();
                System.out.println(value);
                break;
            case MAP:
                value = this.gameServer.getMap();
                System.out.println(value);
                break;
            case PLAYERS:
                value = "" + this.gameServer.getPlayers();
                System.out.println(value);
                break;
            case MAX_PLAYERS:
                value = "" + this.gameServer.getMaxPlayers();
                System.out.println(value);
                break;
            case GAME_TYPE:
                value = "" + this.gameServer.getGameType();
                System.out.println(value);
                break;
        }

        if (value == null)
            return;

        PrintStream printStream = new PrintStream(socket.getOutputStream());

        printStream.println("" + this.gameServer.getId() + "," + this.updateType.getId() + "," + value);

        System.out.println("Send outgoing socket!");
    }
}
