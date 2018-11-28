package com.spraxs.oorlogsimulatie.utils.events.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

@AllArgsConstructor @Getter
public class GameTimerUpdateASyncEvent extends Event {

    private int gameTime;

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
