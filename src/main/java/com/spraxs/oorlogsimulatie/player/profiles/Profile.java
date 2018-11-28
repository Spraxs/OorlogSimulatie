package com.spraxs.oorlogsimulatie.player.profiles;

import lombok.Getter;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class Profile {

    public Profile(OSPlayer player) {
        this.player = player;
    }

    @Getter
    protected OSPlayer player;
}
