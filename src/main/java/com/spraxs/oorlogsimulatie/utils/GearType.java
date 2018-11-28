package com.spraxs.oorlogsimulatie.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Author: Spraxs.
 * Date: 12-3-2018.
 */

@AllArgsConstructor @Getter
public enum GearType {

    SOLDIER_HELMET("soldier_helmet", "Soldier's Helmet", new ItemStack(Material.DIAMOND_HELMET)),
    SOLDIER_CHESTPLATE("soldier_chestplate", "Soldier's Chestplate", new ItemStack(Material.DIAMOND_CHESTPLATE)),
    SOLDIER_LEGGINGS("soldier_leggings", "Soldier's Leggings", new ItemStack(Material.DIAMOND_LEGGINGS)),
    SOLDIER_BOOTS("soldier_boots", "Soldier's Boots", new ItemStack(Material.DIAMOND_BOOTS)),

    SILVER_HELMET("silver_helmet", "Silver Helmet", new ItemStack(Material.IRON_HELMET)),
    SILVER_CHESTPLATE("silver_chestplate", "Silver Chestplate", new ItemStack(Material.IRON_CHESTPLATE)),
    SILVER_LEGGINGS("silver_leggings", "Silver Leggings", new ItemStack(Material.IRON_LEGGINGS)),
    SILVER_BOOTS("silver_boots", "Silver Boots", new ItemStack(Material.IRON_BOOTS)),

    GUARD_HELMET("guard_helmet", "Guard's Helmet", new ItemStack(Material.CHAINMAIL_HELMET)),
    GUARD_CHESTPLATE("guard_chestplate", "Guard's Chestplate", new ItemStack(Material.CHAINMAIL_CHESTPLATE)),
    GUARD_LEGGINGS("guard_leggings", "Guard's Leggings", new ItemStack(Material.CHAINMAIL_LEGGINGS)),
    GUARD_BOOTS("guard_boots", "Guard's Boots", new ItemStack(Material.CHAINMAIL_BOOTS)),

    KING_HELMET("king_helmet", "King's Helmet", new ItemStack(Material.GOLD_HELMET)),
    KING_CHESTPLATE("king_chestplate", "King's Chestplate", new ItemStack(Material.GOLD_CHESTPLATE)),
    KING_LEGGINGS("king_leggings", "King's Leggings", new ItemStack(Material.GOLD_LEGGINGS)),
    KING_BOOTS("king_boots", "King's Boots", new ItemStack(Material.GOLD_BOOTS)),

    ASSASSIN_HELMET("assassin_helmet", "Assassin's Cap", GearItems.getInstance().coloredLeather(Material.LEATHER_HELMET, Color.BLACK)),
    ASSASSIN_CHESTPLATE("assassin_chestplate", "Assassin's Chestplate", GearItems.getInstance().coloredLeather(Material.LEATHER_CHESTPLATE, Color.BLACK)),
    ASSASSIN_LEGGINGS("assassin_leggings", "Assassin's Leggings", GearItems.getInstance().coloredLeather(Material.LEATHER_LEGGINGS, Color.BLACK)),
    ASSASSIN_BOOTS("assassin_boots", "Assassin's Boots", GearItems.getInstance().coloredLeather(Material.LEATHER_BOOTS, Color.BLACK)),

    MASTER_HELMET("master_helmet", "Invisible Cap", null),
    MASTER_CHESTPLATE("master_chestplate", "Invisible Chestplate", null),
    MASTER_LEGGINGS("master_leggings", "Invisible Leggings", null),
    MASTER_BOOTS("master_boots", "Invisible Boots", null), ;

    private String name;
    private String displayName;
    private ItemStack item;
}
