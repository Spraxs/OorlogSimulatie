package com.spraxs.oorlogsimulatie.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author: Spraxs.
 * Date: 14-3-2018.
 */

@AllArgsConstructor @Getter
public enum ServerState {

    OPEN("open", "open"),
    BUSY("busy", "bezig"),
    CLOSED("closed", "gesloten");

    private String name;
    private String displayName;
}
