package com.spraxs.oorlogsimulatie.game.data;

import lombok.Getter;

import java.util.UUID;

/**
 * Author: Spraxs.
 * Date: 3-3-2018.
 */

public class LeaderboardKills {

    public LeaderboardKills(int position, UUID uuid, String name, int kills) {
        this.position = position;
        this.uuid = uuid;
        this.name = name;
        this.kills = kills;
    }

    private @Getter int position;
    private @Getter UUID uuid;
    private @Getter String name;
    private @Getter int kills;
}
