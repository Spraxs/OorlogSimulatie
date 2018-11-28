package com.spraxs.oorlogsimulatie.utils;

import com.spraxs.oorlogsimulatie.utils.logger.Logger;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class WorldManager {

    private static @Getter WorldManager instance;

    public WorldManager() {
        instance = this;
    }

    private @Getter String gameMapName = "game_map";
    private String freshworld = "world";
    private String backupworld = "game_map_back-up";

    public void deleteWorld() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("Removing world...");
        }

        Bukkit.unloadWorld(this.gameMapName, false);

        File wFile = new File(backupworld);
        if (wFile.exists()) {
            try {

                File gameFile = new File(gameMapName);

                if (gameFile.exists()) {
                    FileUtils.deleteDirectory(gameFile);

                    Logger.INFO.log("Ik heb de wereld: '" + gameMapName + "' verwijderd!");
                } else {
                    Logger.INFO.log("De wereld: '" + gameMapName + "' was nog niet geladen, dus die hoef ik niet te verwijderen!");
                }

            } catch (IOException e) {
                Logger.INFO.log("Ik kon de wereld: '" + gameMapName + "' niet verwijderen!");

                e.printStackTrace();
            }
        } else {
            Logger.INFO.log("De '" + this.backupworld + "' is nog niet aangemaakt, gebruik: '/map create'!");
        }
    }

    public File makeBackUp(Player player) {

        try {
            File file = new File(backupworld);
            if (file.exists()) {
                FileUtils.deleteDirectory(file);
            }

            FileUtils.copyDirectory(new File(freshworld), new File(backupworld));

            Logger.INFO.log("Ik heb de back-up: '" + backupworld + "' aangemaakt!");

            if (player != null) {
                player.sendMessage(Message.prefix + "Ik heb de back-up: '" + Message.highlightColor + backupworld + Message.defaultColor + "' aangemaakt!");
            }

            return file;

        } catch (IOException e) {

            Logger.INFO.log("Ik kon de back-up: '" + backupworld + "' niet aanmaken!");

            if (player != null) {
                player.sendMessage(Message.prefix + "Ik kon de back-up: '" + Message.highlightColor + backupworld + Message.defaultColor + "' niet aanmaken!");
            }

            e.printStackTrace();
        }

        return null;
    }

    public void loadWorld() {

        File source = this.getWorldBackUpFile();

        if (source == null) {
            source = makeBackUp(null);
        }

        if (source != null) {

            this.deleteWorld();

            File worldFile = new File(Bukkit.getWorldContainer(), this.gameMapName);

            try {
                copy(source, worldFile);
            } finally {
                Logger.INFO.log("Ik ben klaar met het verwerken van de map '" + this.gameMapName + "'!");
            }

            WorldCreator worldCreator = new WorldCreator(this.gameMapName);

            Bukkit.createWorld(worldCreator);
        } else {
            Logger.INFO.log("The 'source' file is 'null' @ " + this.getClass().getName() + ":loadWorld()");
        }
    }

    private void copy(File source, File target) {

        Logger.INFO.log("'" + this.gameMapName + "' aan het verwerken..");

        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (int i = 0; i < files.length; i++) {
                        File srcFile = new File(source, files[i]);
                        File destFile = new File(target, files[i]);
                        copy(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private File getWorldBackUpFile() {
        File file = new File(backupworld);

        if (file.exists()) {
            return file;
        }

        return null;
    }
}
