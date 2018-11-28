package com.spraxs.oorlogsimulatie.utils.events.leaderboard;

import com.spraxs.oorlogsimulatie.game.data.LeaderboardKills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Author: Spraxs.
 * Date: 3-3-2018.
 */

@AllArgsConstructor @Getter
public class LeaderboardKillsProcessedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private List<LeaderboardKills> topKillsList;
    private LeaderboardKills playerKillsInfo;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
