package com.spraxs.oorlogsimulatie.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


@AllArgsConstructor
@Getter
public enum WeaponType {

    SOLDIER_SWORD("soldier_sword", "Soldier's Sword", new ItemStack(Material.DIAMOND_SWORD)),
    SILVER_SWORD("silver_sword", "Silver Sword", new ItemStack(Material.IRON_SWORD)),
    SILVER_SHOVEL("silver_shovel", "Silver Shovel", new ItemStack(Material.IRON_SPADE)),
    KING_SWORD("king_sword", "King's Sword", new ItemStack(Material.GOLD_SWORD)),
    KING_AXE("king_axe", "King's Axe", new ItemStack(Material.GOLD_AXE));

    private String name;
    private String displayName;
    private ItemStack item;
}

