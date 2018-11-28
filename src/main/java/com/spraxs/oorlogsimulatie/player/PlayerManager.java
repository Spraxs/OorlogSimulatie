package com.spraxs.oorlogsimulatie.player;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.mongo.MongoManager;
import com.spraxs.oorlogsimulatie.mongo.collections.GearUserEntity;
import com.spraxs.oorlogsimulatie.player.data.OSPlayerLoader;
import com.spraxs.oorlogsimulatie.player.data.OSPlayerSaver;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.utils.events.OSPlayerCountUpdateEvent;
import com.spraxs.oorlogsimulatie.utils.events.OSPlayerLoadedEvent;
import com.spraxs.oorlogsimulatie.utils.logger.Logger;
import lombok.Getter;
import net.zoutepopcorn.core.database.mongo.collections.MainUserEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public final class PlayerManager {

    private static @Getter PlayerManager instance;

    public Map<UUID, OSPlayer> players;

    public PlayerManager() {
        instance = this;

        this.mongoManager = MongoManager.getInstance();

        this.players = new HashMap<>();
    }

    private MongoManager mongoManager;

    public OSPlayer setupPlayer(Player player) {
        OSPlayer p;

        p = this.players.get(player.getUniqueId());

        if (p != null)
            return p;


        p = new OSPlayer(player);

        this.players.put(player.getUniqueId(), p);
        return p;
    }

    public OSPlayer getPlayer(Player player) {
        OSPlayer p;

        p = this.players.get(player.getUniqueId());

        return p;
    }

    public void loadPlayerData(Player player, MainUserEntity mainUserEntity) {
        OSPlayer osPlayer = this.setupPlayer(player);

        Bukkit.getScheduler().runTaskAsynchronously(OorlogSimulatiePlugin.getInstance(), () -> {

            GearUserEntity gearUserEntity = this.mongoManager.getGearUser(mainUserEntity);

            osPlayer.setMainUserEntity(mainUserEntity);

            osPlayer.setGearUserEntity(gearUserEntity);

            osPlayer.setLoaded(true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new OSPlayerLoadedEvent(osPlayer));
                }
            }.runTask(OorlogSimulatiePlugin.getInstance());
        });
    }

    // TODO Maak een functie die de speler zijn wands saved en verdiende gold.

    public void loadOSPlayerStats(Player player) {
        OSPlayer osPlayer = this.setupPlayer(player);

        if (GameServer.getInstance().getGameState() == GameState.BUSY || GameServer.getInstance().getGameState() == GameState.STOPPING) osPlayer.setNewJoin(true);

        new OSPlayerLoader(osPlayer);

        this.updatePlayerSize();
    }

    public void saveOSPlayerStats(OSPlayer player) {

        new OSPlayerSaver(player);
    }

    public void saveAndRemoveOSPlayerStats(OSPlayer player) {

        Logger.INFO.log("Saving '" + player.getName() + "' his stats...");

        new OSPlayerSaver(player);

        player.setTeam(null);

        this.players.remove(player.getUuid(), player);

        this.updatePlayerSize(); // TODO Dit werkt nog niet helemaal optimaal
    }

    private void updatePlayerSize() {
        if (Bukkit.isPrimaryThread()) {
            Logger.INFO.log("CALLING EVENT");
            int count = this.players.size();
            Bukkit.getPluginManager().callEvent(new OSPlayerCountUpdateEvent(count));
            Logger.INFO.log("EVENT CALLED");
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    int count = players.size();
                    Bukkit.getPluginManager().callEvent(new OSPlayerCountUpdateEvent(count));
                }
            }.runTask(OorlogSimulatiePlugin.getInstance());
        }
    }
}
