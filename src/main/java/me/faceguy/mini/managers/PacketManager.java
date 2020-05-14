package me.faceguy.mini.managers;

import io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_15_R1.PacketPlayOutSetSlot;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PacketManager {

  private final ItemManager itemManager;

  public PacketManager(ItemManager itemManager) {
    this.itemManager = itemManager;
  }

  public void sendCraftGridPackets(Player player) {
    if (!player.isOnline() || player.getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    ((CraftPlayer) player).getHandle().playerConnection
        .sendPacket(buildPacket(player, 1, itemManager.getTopLeft().getItemIcon()));
    ((CraftPlayer) player).getHandle().playerConnection
        .sendPacket(buildPacket(player, 2, itemManager.getTopRight().getItemIcon()));
    ((CraftPlayer) player).getHandle().playerConnection
        .sendPacket(buildPacket(player, 3, itemManager.getBottomLeft().getItemIcon()));
    ((CraftPlayer) player).getHandle().playerConnection
        .sendPacket(buildPacket(player, 4, itemManager.getBottomRight().getItemIcon()));
    ((CraftPlayer) player).getHandle().playerConnection
        .sendPacket(buildPacket(player, 0, itemManager.getAuxItem().getItemIcon()));
  }

  public void sendBlankGridPackets(Player player) {
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
        new PacketPlayOutSetSlot(0, 0, CraftItemStack.asNMSCopy(null)));
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
        new PacketPlayOutSetSlot(0, 1, CraftItemStack.asNMSCopy(null)));
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
        new PacketPlayOutSetSlot(0, 2, CraftItemStack.asNMSCopy(null)));
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
        new PacketPlayOutSetSlot(0, 3, CraftItemStack.asNMSCopy(null)));
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
        new PacketPlayOutSetSlot(0, 4, CraftItemStack.asNMSCopy(null)));
  }

  private PacketPlayOutSetSlot buildPacket(Player player, int slot, ItemStack stack) {
    ItemStack sentStack = stack.clone();
    List<String> lore = ItemStackExtensionsKt.getLore(sentStack);
    lore = PlaceholderAPI.setPlaceholders(player, lore);
    ItemStackExtensionsKt.setLore(sentStack, lore);
    return new PacketPlayOutSetSlot(0, slot, CraftItemStack.asNMSCopy(sentStack));
  }
}
