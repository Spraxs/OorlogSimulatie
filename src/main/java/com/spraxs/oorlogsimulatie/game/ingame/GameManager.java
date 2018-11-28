package com.spraxs.oorlogsimulatie.game.ingame;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import com.spraxs.oorlogsimulatie.game.GameServer;
import com.spraxs.oorlogsimulatie.game.GameState;
import com.spraxs.oorlogsimulatie.game.lobby.LobbyManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.TeamManager;
import com.spraxs.oorlogsimulatie.game.lobby.teams.framework.Team;
import com.spraxs.oorlogsimulatie.mongo.collections.GearUserEntity;
import com.spraxs.oorlogsimulatie.player.PlayerManager;
import com.spraxs.oorlogsimulatie.player.profiles.OSPlayer;
import com.spraxs.oorlogsimulatie.player.profiles.StatsProfile;
import com.spraxs.oorlogsimulatie.scoreboard.ScoreboardManager;
import com.spraxs.oorlogsimulatie.utils.GearType;
import com.spraxs.oorlogsimulatie.utils.Title;
import com.spraxs.oorlogsimulatie.utils.WeaponType;
import com.spraxs.oorlogsimulatie.utils.events.game.GameTimerUpdateASyncEvent;
import com.spraxs.oorlogsimulatie.utils.events.game.OSPlayerPreSaveASyncEvent;
import com.spraxs.oorlogsimulatie.utils.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.zoutepopcorn.core.api.CoreAPI;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class GameManager {

    private static @Getter
    GameManager instance;

    public GameManager() {
        instance = this;

        this.gameTime = 2 * 60;
        this.stopFirework = false;
        this.canStop = false;

        this.coreAPI = CoreAPI.getAPI();
    }

    private CoreAPI coreAPI;

    private boolean stopFirework;
    private @Setter
    boolean canStop;

    private Random random = new Random();

    private @Getter
    int gameTime;

    // TODO Zorg dat iedereen uit het spel gekicked word en dat hun gegevens worden opgeslagen in de database.

    private int winPrize = 350;
    private int losePrize = 50;

    private int xpWinPrize = 25;
    private int xpLosePrize = 5;

    private OSPlayer first = null;
    private OSPlayer second = null;
    private OSPlayer third = null;

    private void setGameTime(int time) {
        this.gameTime = time;

        Bukkit.getPluginManager().callEvent(new GameTimerUpdateASyncEvent(this.gameTime));
    }

    public void start() {

        for (OSPlayer player : PlayerManager.getInstance().players.values()) {

            if (player.getTeamID() != null) continue;

            if (TeamManager.getInstance().teams.get(0).getSize() > TeamManager.getInstance().teams.get(1).getSize()) {
                TeamManager.getInstance().tryJoin(PlayerManager.getInstance().getPlayer(player.getPlayer()), TeamManager.getInstance().teams.get(1));
                player.getPlayer().teleport(LobbyManager.getInstance().getSpawnB());
            } else {
                TeamManager.getInstance().tryJoin(PlayerManager.getInstance().getPlayer(player.getPlayer()), TeamManager.getInstance().teams.get(0));
                player.getPlayer().teleport(LobbyManager.getInstance().getSpawnA());
            }
        }

        if (GameServer.getInstance().getGameState() != GameState.STARTING && GameServer.getInstance().getGameState() != GameState.OPEN)
            return;

        GameServer.getInstance().setGameState(GameState.BUSY);

        for (OSPlayer player : PlayerManager.getInstance().players.values()) {

            if (TeamManager.getInstance().teams.get(0).getUuid().equals(player.getTeamID())) {
                player.getPlayer().teleport(LobbyManager.getInstance().getSpawnA());
            } else if (TeamManager.getInstance().teams.get(1).getUuid().equals(player.getTeamID())) {
                player.getPlayer().teleport(LobbyManager.getInstance().getSpawnB());
            }

            player.loadGameBoard();

            ScoreboardManager.getInstance().addAllPlayersToScoreboard(player.getOSBoard().getBoard());

            player.getPlayer().getInventory().clear();

            if (player.getTeamID() != null) {
                player.getPlayer().setGameMode(GameMode.SURVIVAL);
            }

            this.defaultKit(player);
        }

        this.startGameTimer();
    }

    private void startGameTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameTime <= 0 || GameServer.getInstance().getGameState() != GameState.BUSY) {
                    cancel();

                    stop();

                    return;
                }

                setGameTime(gameTime - 1);
            }
        }.runTaskTimerAsynchronously(OorlogSimulatiePlugin.getInstance(), 0L, 20L);
    }

    public void stop() {
        if (GameServer.getInstance().getGameState() != GameState.BUSY) return;

        GameServer.getInstance().setGameState(GameState.STOPPING);

        Team loseTeam = null;
        boolean tie = false;

        for (Team team : TeamManager.getInstance().teams) {

            if (loseTeam == null) {

                loseTeam = team;
                continue;
            }

            Bukkit.broadcastMessage(loseTeam.getName() + ": " + loseTeam.getAliveSize());
            Bukkit.broadcastMessage(team.getName() + ": " + team.getAliveSize());

            if (loseTeam.getAliveSize() == team.getAliveSize()) {
                tie = true;
                continue;
            }

            if (loseTeam.getAliveSize() > team.getAliveSize()) {
                loseTeam = team;
            }
        }

        System.out.println("Losing team is " + loseTeam);

        initTopPlayers();

        Bukkit.broadcastMessage(ChatColor.GOLD + "-=-=-=-=-=-=[ Top 3 spelers met kills ]=-=-=-=-=-=-");
        Bukkit.broadcastMessage(ChatColor.GOLD + "1ste " + ChatColor.GREEN + (first == null ? "{NONE}" : first.getName()) + " " + ChatColor.YELLOW);
        Bukkit.broadcastMessage(ChatColor.GOLD + "2de " + ChatColor.GREEN + (second == null ? "{NONE}" : second.getName()) + " " + ChatColor.YELLOW);
        Bukkit.broadcastMessage(ChatColor.GOLD + "3de " + ChatColor.GREEN + (third == null ? "{NONE}" : third.getName()) + " " + ChatColor.YELLOW);

        for (OSPlayer player : PlayerManager.getInstance().players.values()) {

            if (player.getTeamID() == null || player.isNewJoin()) {
                teleportServer(player.getPlayer(), "oslobby");
                continue;
            }

            if (tie) {
                tie(player);
                continue;
            }

            if (loseTeam != null) {
                if (player.getTeamID().equals(loseTeam.getUuid())) {
                    lose(player);
                } else {
                    win(player);
                }
            }
        }

        this.savePlayersASync();

        //new LeaderboardUpdate();

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {

                if (timer == 10) {
                    stopFirework = true;
                } else if (timer >= 13 && canStop) {

                    cancel();

                    shutdownGame();
                }

                timer++;
            }
        }.runTaskTimer(OorlogSimulatiePlugin.getInstance(), 0L, 20L);
    }

    private void shutdownGame() {

        Logger.INFO.log("Shutdown.");

        for (Player p : Bukkit.getOnlinePlayers()) {
            this.teleportServer(p, "oslobby");
        }

        Bukkit.getScheduler().runTaskLater(OorlogSimulatiePlugin.getInstance(), () -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
        }, 20 * 5);
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

    private void savePlayersASync() {
        Bukkit.getScheduler().runTaskAsynchronously(OorlogSimulatiePlugin.getInstance(), () -> {

            for (OSPlayer player : PlayerManager.getInstance().players.values()) {
                Logger.INFO.log("Saving '" + player.getName() + "'...");
                Bukkit.getPluginManager().callEvent(new OSPlayerPreSaveASyncEvent(player));
            }

            Logger.INFO.log("All players saved!");

            this.canStop = true;

        });
    }

    public boolean isGameOver() {
        for (Team team : TeamManager.getInstance().teams) {
            if (team.getAliveSize() == 0) {
                return true;
            }
        }

        return false;
    }

    private void win(OSPlayer player) {

        int fade = 20;
        int stay = 6 * 20;

        new Title(player.getPlayer(), ChatColor.GREEN + "Gefeliciteerd", ChatColor.GRAY + "Jullie hebben de oorlog gewonnen!", fade, stay, fade);

        this.giveGold(player, this.winPrize);
        this.giveXP(player, this.xpWinPrize);

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-=-=-=-=-=-=[" + ChatColor.GREEN + "" + ChatColor.BOLD + "OorlogSimulatiePlugin" + ChatColor.GOLD + "" + ChatColor.BOLD + "]=-=-=-=-=-=-");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Ontvangen: " + ChatColor.YELLOW + this.winPrize + " gold" + ChatColor.GRAY + "!");
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-=-=-=-=-=-=[" + ChatColor.GREEN + "" + ChatColor.BOLD + "OorlogSimulatiePlugin" + ChatColor.GOLD + "" + ChatColor.BOLD + "]=-=-=-=-=-=-");


        StatsProfile statsProfile = player.getStatsProfile();

        statsProfile.setWins(statsProfile.getWins() + 1);

        if (!player.isDead()) {

            new BukkitRunnable() {

                @Override
                public void run() {
                    if (stopFirework) {

                        cancel();

                        return;
                    }
                    launchFirework(player.getPlayer());
                }
            }.runTaskTimer(OorlogSimulatiePlugin.getInstance(), 0L, 10L);
        }
    }

    private void lose(OSPlayer player) {

        int fade = 20;
        int stay = 6 * 20;

        new Title(player.getPlayer(), ChatColor.RED + "Helaas", ChatColor.GRAY + "Jullie hebben de oorlog verloren!", fade, stay, fade);

        this.giveGold(player, this.losePrize);
        this.giveXP(player, this.xpLosePrize);

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-=-=-=-=-=-=[" + ChatColor.GREEN + "" + ChatColor.BOLD + "OorlogSimulatiePlugin" + ChatColor.GOLD + "" + ChatColor.BOLD + "]=-=-=-=-=-=-");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Ontvangen: " + ChatColor.YELLOW + this.losePrize + " gold" + ChatColor.GRAY + "!");
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-=-=-=-=-=-=[" + ChatColor.GREEN + "" + ChatColor.BOLD + "OorlogSimulatiePlugin" + ChatColor.GOLD + "" + ChatColor.BOLD + "]=-=-=-=-=-=-");

        StatsProfile statsProfile = player.getStatsProfile();

        statsProfile.setLoses(statsProfile.getLoses() + 1);
    }

    private void tie(OSPlayer player) {

        int fade = 20;
        int stay = 6 * 20;

        new Title(player.getPlayer(), ChatColor.YELLOW + "Gelijk Spel", ChatColor.GRAY + "De oorlog is gelijk geëindigd!", fade, stay, fade);

        this.giveGold(player, this.winPrize);

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-=-=-=-=-=-=[" + ChatColor.GREEN + "" + ChatColor.BOLD + "OorlogSimulatiePlugin" + ChatColor.GOLD + "" + ChatColor.BOLD + "]=-=-=-=-=-=-");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Ontvangen: " + ChatColor.YELLOW + this.winPrize + " gold" + ChatColor.GRAY + "!");
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-=-=-=-=-=-=[" + ChatColor.GREEN + "" + ChatColor.BOLD + "OorlogSimulatiePlugin" + ChatColor.GOLD + "" + ChatColor.BOLD + "]=-=-=-=-=-=-");


        StatsProfile statsProfile = player.getStatsProfile();

        statsProfile.setWins(statsProfile.getWins() + 1);

        if (!player.isDead()) {

            new BukkitRunnable() {

                @Override
                public void run() {
                    if (stopFirework) {

                        cancel();

                        return;
                    }
                    launchFirework(player.getPlayer());
                }
            }.runTaskTimer(OorlogSimulatiePlugin.getInstance(), 0L, 10L);
        }

    }

    private void launchFirework(Player player) {

        //Spawn the Firework, get the FireworkMeta.
        Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        //Get the type
        int rt = this.random.nextInt(4) + 1;

        FireworkEffect.Type type = FireworkEffect.Type.BALL;

        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;

        //Get our random colours
        int r1i = this.random.nextInt(17) + 1;
        int r2i = this.random.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);

        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(this.random.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(this.random.nextBoolean()).build();

        //Then apply the effect to the meta
        fwm.addEffect(effect);

        //Generate some random power and set it
        int rp = this.random.nextInt(2) + 1;
        fwm.setPower(rp);

        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);
    }

    private Color getColor(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }

    private void defaultKit(OSPlayer osPlayer) {
        Player player = osPlayer.getPlayer();

        player.getInventory().clear();

        player.setFoodLevel(20);
        player.setHealth(20);

        player.setFireTicks(0);

        // player.sendMessage(Message.prefix + "Jouw kit kon niet ingeladen worden...");

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();

        sword.setItemMeta(swordMeta);
        sword.getItemMeta().spigot().setUnbreakable(true);

        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.DURABILITY, 3);

        ItemStack knockSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta knockSwordMeta = knockSword.getItemMeta();

        knockSword.setItemMeta(knockSwordMeta);
        knockSword.getItemMeta().spigot().setUnbreakable(true);

        knockSword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        knockSword.addEnchantment(Enchantment.DURABILITY, 3);
        knockSword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        knockSword.addEnchantment(Enchantment.KNOCKBACK, 1);

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();

        compass.setItemMeta(compassMeta);

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowMeta = bow.getItemMeta();

        bow.setItemMeta(bowMeta);
        bow.getItemMeta().spigot().setUnbreakable(true);

        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
        bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
        bow.addEnchantment(Enchantment.DURABILITY, 3);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        ItemStack water = new ItemStack(Material.WATER_BUCKET);
        ItemMeta waterMeta = water.getItemMeta();

        water.setItemMeta(waterMeta);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();

        arrow.setItemMeta(arrowMeta);

        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 20);
        ItemMeta appleMeta = apple.getItemMeta();

        apple.setItemMeta(appleMeta);

        ItemStack pork = new ItemStack(Material.GRILLED_PORK, 64);
        ItemMeta porkMeta = pork.getItemMeta();

        pork.setItemMeta(porkMeta);

        ItemStack blocks = new ItemStack(Material.SMOOTH_BRICK, 64);
        ItemMeta blocksMeta = blocks.getItemMeta();

        blocks.setItemMeta(blocksMeta);

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();

        pickaxe.setItemMeta(pickaxeMeta);
        pickaxe.getItemMeta().spigot().setUnbreakable(true);

        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 3);
        pickaxe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta axeMeta = axe.getItemMeta();

        axe.setItemMeta(axeMeta);
        axe.getItemMeta().spigot().setUnbreakable(true);

        axe.addEnchantment(Enchantment.DIG_SPEED, 5);
        axe.addEnchantment(Enchantment.DURABILITY, 3);
        axe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);

        ItemStack lava = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta lavaMeta = lava.getItemMeta();

        lava.setItemMeta(lavaMeta);

        ItemStack beef = new ItemStack(Material.COOKED_BEEF, 64);
        ItemMeta beefMeta = beef.getItemMeta();

        beef.setItemMeta(beefMeta);

        ItemStack tnt = new ItemStack(Material.TNT, 3);
        ItemMeta tntMeta = tnt.getItemMeta();

        tnt.setItemMeta(tntMeta);

        ItemStack fire = new ItemStack(Material.FLINT_AND_STEEL, 1);
        ItemMeta fireMeta = fire.getItemMeta();

        fire.setItemMeta(fireMeta);

        ItemStack fishingrod = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta fishingrodMeta = fishingrod.getItemMeta();

        fishingrod.setItemMeta(fishingrodMeta);

        ItemStack work = new ItemStack(Material.WORKBENCH, 3);
        ItemMeta workMeta = work.getItemMeta();

        work.setItemMeta(workMeta);

        ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
        ItemMeta cobbleMeta = cobble.getItemMeta();

        cobble.setItemMeta(cobbleMeta);

        ItemStack wood = new ItemStack(Material.WOOD, 64);
        ItemMeta woodMeta = cobble.getItemMeta();

        wood.setItemMeta(woodMeta);

        ItemStack boat = new ItemStack(Material.BOAT, 3);
        ItemMeta boatMeta = boat.getItemMeta();

        boat.setItemMeta(boatMeta);

        player.getInventory().addItem(sword);
        player.getInventory().addItem(bow);
        player.getInventory().addItem(water);
        player.getInventory().addItem(apple);
        player.getInventory().addItem(beef);
        player.getInventory().addItem(boat);
        player.getInventory().addItem(cobble);
        player.getInventory().addItem(wood);
        player.getInventory().addItem(compass);
        player.getInventory().addItem(tnt);
        player.getInventory().addItem(fire);
        player.getInventory().addItem(pork);
        player.getInventory().addItem(blocks);
        player.getInventory().addItem(pickaxe);
        player.getInventory().addItem(axe);
        player.getInventory().addItem(lava);
        player.getInventory().addItem(lava);
        player.getInventory().addItem(work);
        player.getInventory().addItem(arrow);
        player.getInventory().addItem(water);
        player.getInventory().addItem(water);
        player.getInventory().addItem(knockSword);


        ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta helmMeta = helm.getItemMeta();

        helm.setItemMeta(helmMeta);

        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        helm.addEnchantment(Enchantment.OXYGEN, 1);
        helm.addEnchantment(Enchantment.WATER_WORKER, 1);

        ItemStack harnas = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta harnasMeta = harnas.getItemMeta();

        harnas.setItemMeta(harnasMeta);

        harnas.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

        ItemStack broek = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta broekMeta = broek.getItemMeta();

        broek.setItemMeta(broekMeta);

        broek.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

        ItemStack laarzen = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta laarzenMeta = laarzen.getItemMeta();

        laarzen.setItemMeta(laarzenMeta);

        laarzen.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        laarzen.addEnchantment(Enchantment.DEPTH_STRIDER, 3);
        laarzen.addEnchantment(Enchantment.PROTECTION_FALL, 4);

        player.getInventory().setHelmet(helm);
        player.getInventory().setChestplate(harnas);
        player.getInventory().setLeggings(broek);
        player.getInventory().setBoots(laarzen);

        this.putPlayerArmor(osPlayer);

        player.updateInventory();
    }

    private void putPlayerArmor(OSPlayer osPlayer) {

        Player player = osPlayer.getPlayer();

        GearUserEntity gearUserEntity = osPlayer.getGearUserEntity();


        ItemStack helmet;

        if (gearUserEntity.getHelmet() != null) {
            helmet = this.armor(gearUserEntity.getHelmet());
        } else {
            helmet = this.armor(GearType.SOLDIER_HELMET);
        }

        player.getInventory().setHelmet(helmet);

        ItemStack chestplate;

        if (gearUserEntity.getChestplate() != null) {
            chestplate = this.armor(gearUserEntity.getChestplate());
        } else {
            chestplate = this.armor(GearType.SOLDIER_CHESTPLATE);
        }

        player.getInventory().setChestplate(chestplate);

        ItemStack leggings;

        if (gearUserEntity.getLeggings() != null) {
            leggings = this.armor(gearUserEntity.getLeggings());
        } else {
            leggings = this.armor(GearType.SOLDIER_LEGGINGS);
        }

        player.getInventory().setLeggings(leggings);

        ItemStack boots;

        if (gearUserEntity.getBoots() != null) {
            boots = this.armor(gearUserEntity.getBoots());
        } else {
            boots = this.armor(GearType.SOLDIER_BOOTS);
        }

        player.getInventory().setBoots(boots);

        ItemStack weapon;

        if (gearUserEntity.getWeapon() != null) {
            weapon = this.weapon(gearUserEntity.getWeapon());
        } else {
            weapon = this.weapon(WeaponType.SOLDIER_SWORD);
        }

        player.getInventory().setItem(0, weapon);

    }

    private ItemStack weapon(WeaponType weaponType) {

        if (weaponType == null) return null;

        if (weaponType.getItem() == null) return null;

        boolean leather = weaponType == WeaponType.SOLDIER_SWORD || weaponType == WeaponType.SILVER_SWORD
                || weaponType == WeaponType.SILVER_SHOVEL || weaponType == WeaponType.KING_SWORD ||
                weaponType == WeaponType.KING_AXE;

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(weaponType.getItem());
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Unbreakable", true);
        nmsItem.setTag(tag);
        ItemStack i = CraftItemStack.asCraftMirror(nmsItem);

        if (leather) {

            ItemMeta im = i.getItemMeta();

            im.setDisplayName(weaponType.getDisplayName());

            im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            if (isItemWeapon(weaponType)) {
                im.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
                im.addEnchant(Enchantment.DURABILITY, 3, true);
            }

            i.setItemMeta(im);
        }

        return i;
    }

    private ItemStack armor(GearType gearType) {

        if (gearType == null) return null;

        if (gearType.getItem() == null) return null;

        boolean leather = gearType == GearType.ASSASSIN_HELMET || gearType == GearType.ASSASSIN_CHESTPLATE
                || gearType == GearType.ASSASSIN_LEGGINGS || gearType == GearType.ASSASSIN_BOOTS;

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(gearType.getItem());
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Unbreakable", true);
        nmsItem.setTag(tag);
        ItemStack i = CraftItemStack.asCraftMirror(nmsItem);

        if (leather) {

            Color color = ((LeatherArmorMeta) gearType.getItem().getItemMeta()).getColor();

            LeatherArmorMeta im = (LeatherArmorMeta) i.getItemMeta();

            if (color != null)
                im.setColor(color);

            im.setDisplayName(gearType.getDisplayName());

            im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            if (isItemHelmet(gearType)) {
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
                im.addEnchant(Enchantment.OXYGEN, 1, true);
                im.addEnchant(Enchantment.WATER_WORKER, 1, true);
            }

            if (isItemChestplate(gearType)) {
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
            }

            if (isItemLeggings(gearType)) {
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
            }

            if (isItemBoots(gearType)) { // TODO Deze enchantment weghalen, als damage van water is ge implementeerd
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
                im.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
                im.addEnchant(Enchantment.PROTECTION_FALL, 4, true);
            }

            i.setItemMeta(im);

        } else {
            ItemMeta im = i.getItemMeta();

            im.setDisplayName(gearType.getDisplayName());

            im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            if (isItemHelmet(gearType)) {
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
                im.addEnchant(Enchantment.OXYGEN, 1, true);
                im.addEnchant(Enchantment.WATER_WORKER, 1, true);
            }

            if (isItemChestplate(gearType)) {
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
            }

            if (isItemLeggings(gearType)) {
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
            }

            if (isItemBoots(gearType)) { // TODO Deze enchantment weghalen, als damage van water is ge implementeerd
                im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
                im.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
                im.addEnchant(Enchantment.PROTECTION_FALL, 4, true);
            }


            i.setItemMeta(im);
        }

        return i;
    }

    private boolean isItemHelmet(GearType gearType) {
        return gearType == GearType.SOLDIER_HELMET || gearType == GearType.SILVER_HELMET || gearType == GearType.GUARD_HELMET
                || gearType == GearType.KING_HELMET || gearType == GearType.ASSASSIN_HELMET || gearType == GearType.MASTER_HELMET;
    }

    private boolean isItemChestplate(GearType gearType) {
        return gearType == GearType.SOLDIER_CHESTPLATE || gearType == GearType.SILVER_CHESTPLATE || gearType == GearType.GUARD_CHESTPLATE
                || gearType == GearType.KING_CHESTPLATE || gearType == GearType.ASSASSIN_CHESTPLATE || gearType == GearType.MASTER_CHESTPLATE;
    }

    private boolean isItemLeggings(GearType gearType) {
        return gearType == GearType.SOLDIER_LEGGINGS || gearType == GearType.SILVER_LEGGINGS || gearType == GearType.GUARD_LEGGINGS
                || gearType == GearType.KING_LEGGINGS || gearType == GearType.ASSASSIN_LEGGINGS || gearType == GearType.MASTER_LEGGINGS;
    }

    private boolean isItemBoots(GearType gearType) {
        return gearType == GearType.SOLDIER_BOOTS || gearType == GearType.SILVER_BOOTS || gearType == GearType.GUARD_BOOTS
                || gearType == GearType.KING_BOOTS || gearType == GearType.ASSASSIN_BOOTS || gearType == GearType.MASTER_BOOTS;
    }

    private boolean isItemWeapon(WeaponType weaponType) {
        return weaponType == WeaponType.SOLDIER_SWORD || weaponType == WeaponType.SILVER_SWORD || weaponType == WeaponType.SILVER_SHOVEL
                || weaponType == WeaponType.KING_SWORD || weaponType == WeaponType.KING_AXE;
    }

    private void giveGold(OSPlayer player, int gold) {
        player.getMainUserEntity().gold += gold;

        Bukkit.getScheduler().runTaskAsynchronously(OorlogSimulatiePlugin.getInstance(), () -> this.coreAPI.saveMainProfile(player.getMainUserEntity()));
    }

    private void giveXP(OSPlayer player, int xp) {
        player.getMainUserEntity().xp += xp;

        Bukkit.getScheduler().runTaskAsynchronously(OorlogSimulatiePlugin.getInstance(), () -> this.coreAPI.saveMainProfile(player.getMainUserEntity()));
    }

    private void initTopPlayers() {
        for (OSPlayer player : PlayerManager.getInstance().players.values()) {
            if (first == null || player.getStatsProfile().getKills() > first.getStatsProfile().getKills()) {
                first = player;
                continue;
            }

            if (second == null || player.getStatsProfile().getKills() > second.getStatsProfile().getKills()) {
                second = player;
                continue;
            }

            if (third == null || player.getStatsProfile().getKills() > third.getStatsProfile().getKills()) {
                third = player;
            }
        }
    }
}
