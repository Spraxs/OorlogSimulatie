package com.spraxs.oorlogsimulatie.command.commands;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.command.PluginCommand;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.utils.logger.Logger;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommandHub extends PluginCommand {

    public CommandHub(String name, boolean op) {
        super(name, op);

        this.playerManager = PlayerManager.getInstance();
        this.gameServer = GameServer.getInstance();
    }

    public PlayerManager playerManager;
    public GameServer gameServer;

    @Override
    public void execute() {
        if (this.sender instanceof Player) {
            Player p = (Player) this.sender;

            if (this.args == null) {
                Logger.INFO.log("Je word naar de lobby verzonden..");
                this.teleportServer(p, "oslobby");
            }
        }
    }

    private void teleportServer(Player p, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }

        p.sendPluginMessage(OorlogSimulatiePlugin.getInstance(), "BungeeCord", b.toByteArray());
    }
}
