package com.spraxs.oorlogsimulatie.utils.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

@AllArgsConstructor @Getter
public class OSPlayerCountUpdateEvent extends Event {

    private int count;

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
