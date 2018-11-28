package com.spraxs.oorlogsimulatie.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author: Spraxs.
 * Date: 14-4-2018.
 */

@AllArgsConstructor @Getter
public enum UpdateType {

    STATE("state", 0),
    MAP("map", 1),
    PLAYERS("players", 2),
    MAX_PLAYERS("max_players", 3),
    GAME_TYPE("game_type", 4);

    private String name;
    private int id;
}
