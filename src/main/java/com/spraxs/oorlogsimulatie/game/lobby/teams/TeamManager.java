package com.spraxs.oorlogsimulatie.game.lobby.teams;

import com.spraxs.oorlogsimulatie.data.Configuration;
import com.spraxs.oorlogsimulatie.data.OSFile;
import com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Spraxs
 * Date: 4-11-2018
 */

public class TeamManager {

    private static @Getter TeamManager instance;

    public List<Team> teams;

    public TeamManager() {
        instance = this;

        this.teams = new ArrayList<>();

        setupTeams();
    }

    public Team getTeam(UUID uuid) {

        for (Team team : this.teams) {
            if (uuid.equals(team.getUuid())) {
                return team;
            }
        }

        return null;
    }

    private void setupTeams() {

        OSFile file = Configuration.getInstance().getFile(Configuration.TEAM);
        FileConfiguration config = file.getConfig();

        this.teams.add(new Team(config.getString("Team.A.Name"), ChatColor.valueOf(config.getString("Team.A.Color")),
                Material.valueOf(config.getString("Team.A.Item.Material")), config.getInt("Team.A.Item.Data")));

        this.teams.add(new Team(config.getString("Team.B.Name"), ChatColor.valueOf(config.getString("Team.B.Color")),
                Material.valueOf(config.getString("Team.B.Item.Material")), config.getInt("Team.B.Item.Data")));
    }

    public void tryJoin(OSPlayer player, Team team) { // TODO Laat boolean returnen om te weten of hij kon joinen of niet

        boolean full = true;

        for (Team t : teams) {
            if (t.getUuid().equals(team.getUuid())) continue;

            if (team.getSize() <= t.getSize()) {
                full = false;
                break;
            }
        }

        if (full) {
            player.sendInfo("Dit team zit vol!");
            return;
        }

        player.setTeam(team);
    }
}
