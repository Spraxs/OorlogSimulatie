package com.spraxs.oorlogsimulatie.data;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class Configuration {

    private static @Getter Configuration instance;

    public Configuration() {
        instance = this;
        setupConfig();
    }

    public static final String SERVER = "server.yml";
    public static final String TEAM = "team.yml";
    public static final String LOCATIONS = "locations.yml";
    public static final String TOPGAMEKILLS = "topgamekills.yml";

    private void setupConfig() {
        OSFile server = new OSFile(SERVER);

        server.createConfigPath("id", 0);
        server.createConfigPath("max_players", 30);

        server.save();

        OSFile team = new OSFile(TEAM);


        team.createConfigPath("Team.A.Name", "Groen");
        team.createConfigPath("Team.A.Color", ChatColor.GREEN.name());
        team.createConfigPath("Team.A.Item.Material", Material.WOOL.name());
        team.createConfigPath("Team.A.Item.Data", 5);

        team.createConfigPath("Team.B.Name", "Oranje");
        team.createConfigPath("Team.B.Color", ChatColor.GOLD.name());
        team.createConfigPath("Team.B.Item.Material", Material.WOOL.name());
        team.createConfigPath("Team.B.Item.Data", 1);

        team.save();

        OSFile locations = new OSFile(LOCATIONS);

        locations.createConfigPath("Spawn.A.X", 0);
        locations.createConfigPath("Spawn.A.Y", 0);
        locations.createConfigPath("Spawn.A.Z", 0);
        locations.createConfigPath("Spawn.A.V", new Vector().zero());
        locations.createConfigPath("Spawn.A.W", "world");

        locations.createConfigPath("Spawn.B.X", 0);
        locations.createConfigPath("Spawn.B.Y", 0);
        locations.createConfigPath("Spawn.B.Z", 0);
        locations.createConfigPath("Spawn.B.V", new Vector().zero());
        locations.createConfigPath("Spawn.B.W", "world");

        locations.createConfigPath("Spawn.Spectator.X", 0);
        locations.createConfigPath("Spawn.Spectator.Y", 0);
        locations.createConfigPath("Spawn.Spectator.Z", 0);
        locations.createConfigPath("Spawn.Spectator.V", new Vector().zero());
        locations.createConfigPath("Spawn.Spectator.W", "world");

        locations.createConfigPath("Spawn.Lobby.X", 0);
        locations.createConfigPath("Spawn.Lobby.Y", 0);
        locations.createConfigPath("Spawn.Lobby.Z", 0);
        locations.createConfigPath("Spawn.Lobby.V", new Vector().zero());
        locations.createConfigPath("Spawn.Lobby.W", "world");

        locations.save();

        OSFile topgamekills = new OSFile(TOPGAMEKILLS);

        topgamekills.createConfigPath("Player.Name", "Name");
        topgamekills.createConfigPath("Player.Kills", 0);


        topgamekills.save();
    }


    private @Getter ArrayList<OSFile> files = new ArrayList<>();

    public OSFile getFile(String name) {
        for(OSFile file : files) {
            if(file.getName().equalsIgnoreCase(name)) {
                return file;
            }
        }
        return null;
    }

    public boolean isFile(String name) {
        return getFile(name) != null;
    }
}
