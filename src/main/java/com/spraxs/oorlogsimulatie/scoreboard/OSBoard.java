package com.spraxs.oorlogsimulatie.scoreboard;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class OSBoard {

    private Scoreboard board;
    private String name;
    private @Getter HashMap<Integer, String> lineId = new HashMap<>();
    private String blank = "";
    private int slot;

    public OSBoard(int lines) {

        this.slot = lines;

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("Scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        String name = ChatColor.GREEN + "" + ChatColor.BOLD + "OorlogSimulatiePlugin";

        objective.setDisplayName(name);

        this.name = name;
        this.board = board;
    }

    public void addLine(String line) {
        Score score = board.getObjective(DisplaySlot.SIDEBAR).getScore(line);
        score.setScore(this.slot);
        lineId.put(this.slot, line);

        this.slot -= 1;
    }

    public void addBlankLine() {
        Score score = board.getObjective(DisplaySlot.SIDEBAR).getScore(blank);
        score.setScore(this.slot);
        lineId.put(this.slot, blank);
        blank = blank + " ";

        this.slot -= 1;
    }

    public void setName(String newName) {
        board.getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + newName);
    }

    public Scoreboard getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }
}
