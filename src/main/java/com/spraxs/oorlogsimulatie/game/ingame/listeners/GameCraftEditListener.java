package com.spraxs.oorlogsimulatie.game.ingame.listeners;

import com.spraxs.oorlogsimulatie.OorlogSimulatiePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class GameCraftEditListener implements Listener {

    public GameCraftEditListener() {
        OorlogSimulatiePlugin.getInstance().registerListener(this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != event.getWhoClicked().getInventory()) return;

        if (event.getSlotType() != InventoryType.SlotType.CRAFTING) return;

        event.setCancelled(true);
    }
}
