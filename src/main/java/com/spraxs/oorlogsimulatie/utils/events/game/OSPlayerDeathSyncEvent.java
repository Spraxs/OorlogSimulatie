package com.spraxs.oorlogsimulatie.utils.events.game;

import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

@AllArgsConstructor @Getter
public class OSPlayerDeathSyncEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private OSPlayer player;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
