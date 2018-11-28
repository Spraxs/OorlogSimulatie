package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import org.bukkit.event.Listener;

public class GameColdWaterListener implements Listener {

//    public GameColdWaterListener() {
//        OorlogSimulatiePlugin.getInstance().registerListener(this);
//
//        this.coldPlayer = new HashMap<>();
//        this.runnable = null;
//        this.playerManager = PlayerManager.getInstance();
//        this.gameServer = GameServer.getInstance();
//
//    }
//
//    private HashMap<Player, Integer> coldPlayer;
//    private BukkitTask runnable;
//    private PlayerManager playerManager;
//    private GameServer gameServer;
//
//    @EventHandler
//    public void onMove(PlayerMoveEvent event) {
//        if (this.gameServer.getGameState() != GameState.BUSY) return;
//
//        OSPlayer osPlayer = this.playerManager.getPlayer(event.getPlayer());
//
//        if (osPlayer == null) return;
//        if (osPlayer.isSpectating()) {
//
//            this.coldPlayer.remove(event.getPlayer());
//
//            return;
//        }
//
//        if (event.getFrom() == event.getTo()) return;
//
//        Block from = event.getFrom().getBlock();
//        Block to = event.getTo().getBlock();
//
//        Material fromMat = from.getType();
//        Material toMat = to.getType();
//
//        if (fromMat == Material.WATER || fromMat == Material.STATIONARY_WATER) {
//
//            if (!(toMat == Material.WATER || toMat == Material.STATIONARY_WATER)) { // Gaat uit water
//
//                this.coldTask(event.getPlayer());
//                this.coldPlayer.remove(event.getPlayer()); // <--- Testen of dit gaat werken
//            }
//
//        } else {
//
//            if (toMat == Material.WATER || toMat == Material.STATIONARY_WATER) { // Gaat in water
//
//                this.coldTask(event.getPlayer());
//            }
//
//        }
//    }
//
//    @EventHandler
//    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
//        Player p = event.getPlayer();
//
//        Material bucket = event.getBucket();
//
//        if (bucket.toString().contains("WATER")) {
//            this.coldPlayer.remove(event.getPlayer());
//            p.sendMessage(ChatColor.YELLOW + "[DEBUG] > Je water bucket is leeg. (GCWL CLASS)");
//        }
//
//        if (bucket.toString().contains("LAVA")) {
//            this.coldPlayer.remove(event.getPlayer());
//            p.sendMessage(ChatColor.YELLOW + "[DEBUG] > Je lava bucket is leeg. (GCWL CLASS)");
//        }
//    }
//
//
//    @EventHandler()
//    public void onBlockPlace(BlockPlaceEvent event) {
//        Player player = event.getPlayer();
//
//        if (event.getBlockPlaced().getType().equals(Material.WATER)) {
//            this.coldPlayer.remove(player);
//            return;
//        }
//    }
//
//
//    private void coldTask(Player player) {
//
//        if (player.isInsideVehicle()) {
//            if (player.getVehicle() instanceof Boat) {
//
//                this.coldPlayer.remove(player);
//
//                return;
//            }
//        }
//
//        if (this.coldPlayer.containsKey(player)) {
//            this.coldPlayer.remove(player);
//        } else {
//            this.coldPlayer.put(player, 0);
//        }
//
//        if (this.runnable == null) {
//            this.run();
//        }
//    }
//
//    private void run() {
//
//        this.runnable = new BukkitRunnable() {
//
//            @Override
//            public void run() {
//                if (coldPlayer.isEmpty()) {
//                    this.cancel();
//                    runnable = null;
//                }
//
//                for (Player player : coldPlayer.keySet()) {
//
//                    int value = coldPlayer.get(player);
//
//                    if (value >= 10) {
//                        double damage = 2;
//
//                        OSPlayer osPlayer = playerManager.getPlayer(player);
//
//                        if (osPlayer == null) continue;
//
//                        player.damage(0);
//
//                        if (player.getHealth() - damage <= 0) {
//
//                            Bukkit.getScheduler().runTask(OorlogSimulatiePlugin.getInstance(), () -> osPlayer.die(null));
//
//                            coldPlayer.remove(player);
//
//                        } else {
//                            player.setHealth(player.getHealth() - damage);
//                        }
//
//                    } else {
//                        if (value >= 5) {
//                            Bukkit.getScheduler().runTask(OorlogSimulatiePlugin.getInstance(), () ->
//                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 1)));
//                        }
//
//                        coldPlayer.put(player, (value + 1));
//                    }
//                }
//            }
//        }.runTaskTimerAsynchronously(OorlogSimulatiePlugin.getInstance(), 0L, 20L);
//    }
}
