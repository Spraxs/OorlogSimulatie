package com.spraxs.oorlogsimulatie.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author: Spraxs.
 * Date: 14-4-2018.
 */

@AllArgsConstructor @Getter
public enum GameState {

    OPEN(ServerState.OPEN),
    STARTING(ServerState.OPEN),
    BUSY(ServerState.BUSY),
    STOPPING(ServerState.CLOSED),
    CLOSED(ServerState.CLOSED);

    private ServerState serverState;
}
