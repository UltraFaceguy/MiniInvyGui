package me.faceguy.mini.utils;

import me.faceguy.mini.MiniInvyGui;
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PacketUtil {

  private final PacketPlayOutSetSlot topLeftItemPacket;
  private final PacketPlayOutSetSlot topRightItemPacket;
  private final PacketPlayOutSetSlot bottomLeftItemPacket;
  private final PacketPlayOutSetSlot bottomRightItemPacket;
  private final PacketPlayOutSetSlot auxItemPacket;

  public PacketUtil(MiniInvyGui plugin) {
    this.topLeftItemPacket = buildPacket(1, plugin.getItemManager().getTopLeft().getItemIcon());
    this.topRightItemPacket = buildPacket(2, plugin.getItemManager().getTopRight().getItemIcon());
    this.bottomLeftItemPacket = buildPacket(3, plugin.getItemManager().getBottomLeft().getItemIcon());
    this.bottomRightItemPacket = buildPacket(4, plugin.getItemManager().getBottomRight().getItemIcon());
    this.auxItemPacket = buildPacket(0, plugin.getItemManager().getAuxItem().getItemIcon());
  }

  public void sendCraftGridPackets(Player player) {
    if (!player.isOnline() || player.getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(topLeftItemPacket);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(topRightItemPacket);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bottomLeftItemPacket);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bottomRightItemPacket);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(auxItemPacket);
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

  private PacketPlayOutSetSlot buildPacket(int slot, ItemStack stack) {
    return new PacketPlayOutSetSlot(0, slot, CraftItemStack.asNMSCopy(stack));
  }
}
