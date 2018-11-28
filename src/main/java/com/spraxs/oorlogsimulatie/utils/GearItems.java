package com.spraxs.oorlogsimulatie.utils;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * Author: Spraxs.
 * Date: 12-3-2018.
 */

public class GearItems {

    private static @Getter GearItems instance;

    public GearItems() {
        instance = this;
    }

    public ItemStack coloredLeather(Material material, Color color) {
        ItemStack i = new ItemStack(material, 1);
        LeatherArmorMeta im = (LeatherArmorMeta) i.getItemMeta();

        im.setColor(color);

        i.setItemMeta(im);

        return i;
    }
}
