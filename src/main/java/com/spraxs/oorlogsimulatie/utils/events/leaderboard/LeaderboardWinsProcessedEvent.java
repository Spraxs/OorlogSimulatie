package com.spraxs.oorlogsimulatie.utils.events.leaderboard;

import com.spraxs.oorlogsimulatie.game.data.LeaderboardWins;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

@AllArgsConstructor
@Getter
public class LeaderboardWinsProcessedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private List<LeaderboardWins> topWinsList;
    private LeaderboardWins playerWinsInfo;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
