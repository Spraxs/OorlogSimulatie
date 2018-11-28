package com.spraxs.oorlogsimulatie.game.lobby.teams.framework;

import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Spraxs
 * Date: 4-11-2018
 */

@Data
public class Team {

    private final ChatColor color;
    private final String name;
    private final UUID uuid;
    private final Material material;
    private final int m_data;

    public List<UUID> players;

    public Team(String name, ChatColor color, Material material, int m_data) {
        this.name = name;
        this.color = color;
        this.players = new ArrayList<>();
        this.uuid = UUID.randomUUID();

        this.material = material;
        this.m_data = m_data;
    }

    public int getSize() {
        return this.players.size();
    }


    public int getAliveSize() {

        int size = getSize();

        for (UUID uuid : players) {
            OSPlayer player = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(uuid));

            if (player.isDead()) {
                size -= 1;
            }
        }

        return size;
    }

}
