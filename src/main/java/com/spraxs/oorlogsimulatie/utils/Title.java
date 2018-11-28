package com.spraxs.oorlogsimulatie.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Title {

    public Title(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {

        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");
        IChatBaseComponent chatSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subTitle + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle title1 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle title2 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSub);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title1);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    public Title(Player player, String message){

        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
