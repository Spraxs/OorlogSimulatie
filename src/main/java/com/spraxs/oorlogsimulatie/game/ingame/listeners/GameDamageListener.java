package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Author: Spraxs.
 * Date: 2-3-2018.
 */

public class GameDamageListener implements Listener {

    public GameDamageListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);

        this.playerManager = PlayerManager.getInstance();
        this.gameServer = GameServer.getInstance();
    }

    private PlayerManager playerManager;
    private GameServer gameServer;

    private boolean debugEnabled = false;

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {

        GameState gameState = this.gameServer.getGameState();

        // Kijkt of de game begonnen is
        if (gameState != GameState.BUSY) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) return;

        OSPlayer osPlayer = this.playerManager.getPlayer((Player) event.getEntity());



        // Kijkt of spelers bij elkaar in het team zitten
        if (event instanceof EntityDamageByEntityEvent) {
            OSPlayer damager = null;
            if (!(((EntityDamageByEntityEvent) event).getDamager() instanceof Player)) {
                if (((EntityDamageByEntityEvent) event).getDamager() instanceof Projectile) {
                    if (((Projectile) ((EntityDamageByEntityEvent) event).getDamager()).getShooter() instanceof Player) {
                        damager = this.playerManager.getPlayer((Player) ((Projectile) ((EntityDamageByEntityEvent) event).getDamager()).getShooter());
                    }
                }
            } else {
                damager = this.playerManager.getPlayer((Player) ((EntityDamageByEntityEvent) event).getDamager());
            }

            if (damager != null) {
                if (osPlayer.getTeamID().equals(damager.getTeamID())) {
                    event.setCancelled(true);

                    return;
                }

                osPlayer.setLastDamaged(damager);
            }
        }

        Player player = (Player) event.getEntity();
        double damage = event.getDamage();

        if (!this.canBeBlocked(event)) {
            event.setDamage(damage);

            this.debug(player, "Deze damage kon niet geblokkeerd worden door je perm armor!");


            if (((Player) event.getEntity()).getHealth() - damage <= 0) {
                this.playerDie(event, osPlayer);

                return;
            }

            return;
        }

        int armorPoints = 23; // Default on 20

        this.debug(player, " ");
        this.debug(player, " ");
        this.debug(player, " ");
        this.debug(player, "True damage: " + damage);

        if (player.isBlocking()) {
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.5f, 4.0f);

            // Omdat de speler blocked met een sword reduced hij de damage
            damage = (damage + 1) / 2;


            this.debug(player, "Blocked damage: " + damage);

        }

        // Als ik dit tussen haakjes doe in de formule hieronder returned hij 0.
        // Ik weet niet waarom, maar java rekent het niet goed uit als ik het niet zo doe.
        double a = 25 - armorPoints;
        damage = a / 25 * damage; // Dit rekent uit hoeveel damage er word gereduced afhankelijk van je armor points


        this.debug(player, "Armored damage: " + damage);


        //TODO Maak damage reduce voor resistance potion

        /*
        double resistanceValue = resistanceLevel * 5;


        // Dit rekent uit hoeveel damage er word gereduced als de speler een resistance potion heeft.
        damage = (25 - resistanceValue) / 25 * damage;
        */

        // TODO reken maybe ook de damage reduction uit voor protection enchantments & Feather falling.

        EntityLiving entityLiving = ((CraftPlayer)player.getPlayer()).getHandle();


        if (damage > 0) {
            float aborptionHearts = entityLiving.getAbsorptionHearts();
            float updatedAbsorption = aborptionHearts - (float) damage;
            damage -= aborptionHearts;
            if (updatedAbsorption > 0) {
                entityLiving.setAbsorptionHearts(updatedAbsorption);
            } else {
                entityLiving.setAbsorptionHearts(0);
            }

            this.debug(player, "Absorption: " + aborptionHearts);

            this.debug(player, "Absorption damage: " + damage);
        }

        this.debug(player, "Result damage: " + damage);

        if (damage < 0) { // Als de damage een negatief getal is, maak ik het gewoon 0. Dus doet het geen damage ipv dat hij misschien health terug krijgt.
            this.debug(player, "Damage correction because it is negative: " + damage + " -> " + 0);
            damage = 0;
        }

        this.debug(player, " ");

        this.debug(player, "Final damage: " + damage);

        if (((Player) event.getEntity()).getHealth() - damage <= 0) {
            this.playerDie(event, osPlayer);

            return;
        }

        event.setDamage(0);

        player.setHealth(player.getHealth() - damage);
    }

    private void playerDie(EntityDamageEvent event, OSPlayer osPlayer) {
        event.setDamage(0);
        TeamManager.getInstance().getTeam(osPlayer.getTeamID()).getAliveSize();

        OSPlayer killer = null;

        if (event instanceof EntityDamageByEntityEvent) {
            if (!(((EntityDamageByEntityEvent) event).getDamager() instanceof Player)) {
                if (!(((EntityDamageByEntityEvent) event).getDamager() instanceof Projectile)) return;
                if (!(((Projectile) ((EntityDamageByEntityEvent) event).getDamager()).getShooter() instanceof  Player)) return;
                killer = this.playerManager.getPlayer((Player) ((Projectile) ((EntityDamageByEntityEvent) event).getDamager()).getShooter());
            } else {
                killer = this.playerManager.getPlayer((Player) ((EntityDamageByEntityEvent) event).getDamager());
            }
        }

        osPlayer.die(killer);
    }


    private void debug(Player player, String message) {
        if (!this.debugEnabled) return;
        player.sendMessage(message);
    }

    private boolean canBeBlocked(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE) return false;
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) return false;
        if (event.getCause() == EntityDamageEvent.DamageCause.DROWNING) return false;
        if (event.getCause() == EntityDamageEvent.DamageCause.STARVATION) return false;
        //if (event.getCause() == EntityDamageEvent.DamageCause.FALL) return false;
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) return false;
        if (event.getCause() == EntityDamageEvent.DamageCause.POISON) return false;
        if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) return false;

        return true;
    }
}
