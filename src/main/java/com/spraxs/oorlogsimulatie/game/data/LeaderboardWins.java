package com.spraxs.oorlogsimulatie.game.data;

import lombok.Getter;

import java.util.UUID;

public class LeaderboardWins {

    public LeaderboardWins(int position, UUID uuid, String name, int wins) {
        this.position = position;
        this.uuid = uuid;
        this.name = name;
        this.wins = wins;
    }

    private @Getter
    int position;
    private @Getter UUID uuid;
    private @Getter String name;
    private @Getter int wins;
}
