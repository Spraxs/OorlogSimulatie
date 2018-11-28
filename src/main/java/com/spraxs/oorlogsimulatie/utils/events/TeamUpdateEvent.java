package com.spraxs.oorlogsimulatie.utils.events;

import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

@AllArgsConstructor @Getter
public class TeamUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private OSPlayer player;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
