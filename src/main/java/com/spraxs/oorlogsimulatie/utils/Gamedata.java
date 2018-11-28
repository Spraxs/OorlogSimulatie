package com.spraxs.oorlogsimulatie.utils;

import lombok.Getter;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class Gamedata {

    private static @Getter Gamedata instance;

    public Gamedata() {
        instance = this;
    }

    private @Getter int playersToStart = 16;
}
