package com.spraxs.oorlogsimulatie.data;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class OSFile {

    private @Getter String name;
    private File file;
    private FileConfiguration config;

    public OSFile(String name) {
        Configuration.getInstance().getFiles().add(this);
        this.name = name;

        this.file = new File(OorlogSimulatiePlugin.getInstance().getDataFolder(), name);

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createConfigPath(String path, ArrayList<String> value) {

        if (!config.contains(path)) {
            config.set(path, value);
        }
    }

    public void createConfigPath(String path, boolean value) {

        if (!config.contains(path)) {
            config.set(path, value);
        }
    }

    public void createConfigPath(String path, int value) {

        if (!config.contains(path)) {
            config.set(path, value);
        }
    }

    public void createConfigPath(String path, String value) {

        if (!config.contains(path)) {
            config.set(path, value);
        }
    }

    public void createConfigPath(String path, Vector value) {

        if (!config.contains(path)) {
            config.set(path, value);
        }
    }
}
