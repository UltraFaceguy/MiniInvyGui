package me.faceguy.mini.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PacketManager {

  private final WeakHashMap<Player, ItemStack> skullMap = new WeakHashMap<>();
  private final ItemManager itemManager;
  private final ProtocolManager protocolManager;
  private final Logger logger;

  public PacketManager(ItemManager itemManager, Logger logger, ProtocolManager protocolManager) {
    this.itemManager = itemManager;
    this.protocolManager = protocolManager;
    this.logger = logger;
  }

  public void sendCraftGridPackets(Player player) {
    if (!player.isOnline() || player.getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    try {
      protocolManager.sendServerPacket(player, buildPacket(player, 1, itemManager.getTopLeft().getItemIcon()));
      protocolManager.sendServerPacket(player, buildPacket(player, 2, itemManager.getTopRight().getItemIcon()));
      protocolManager.sendServerPacket(player, buildPacket(player, 3, itemManager.getBottomLeft().getItemIcon()));
      protocolManager.sendServerPacket(player, buildPacket(player, 4, itemManager.getBottomRight().getItemIcon()));
      protocolManager.sendServerPacket(player, buildPacket(player, 0, itemManager.getAuxItem().getItemIcon()));
    } catch (InvocationTargetException exception) {
      logger.log(Level.WARNING, String.format("Unable to send craft grid packets to %s", player.getName()), exception);
    }
  }

  public void sendBlankGridPackets(Player player) {
    try {
      protocolManager.sendServerPacket(player, buildPacket(player, 1, null));
      protocolManager.sendServerPacket(player, buildPacket(player, 2, null));
      protocolManager.sendServerPacket(player, buildPacket(player, 3, null));
      protocolManager.sendServerPacket(player, buildPacket(player, 4, null));
      protocolManager.sendServerPacket(player, buildPacket(player, 0, null));
    } catch (InvocationTargetException exception) {
      logger.log(Level.WARNING, String.format("Unable to send craft grid packets to %s", player.getName()), exception);
    }
  }

  private PacketContainer buildPacket(Player player, int slot, @Nullable ItemStack stack) {
    ItemStack sentStack = null;
    if (stack != null) {
      sentStack = stack.clone();
      if (sentStack.getType() == Material.PLAYER_WALL_HEAD) {
        if (skullMap.containsKey(player)) {
          sentStack = skullMap.get(player).clone();
        } else {
          sentStack.setType(Material.PLAYER_HEAD);
          SkullMeta meta = (SkullMeta) sentStack.getItemMeta();
          assert meta != null;
          meta.setOwningPlayer(player);
          sentStack.setItemMeta(meta);
          skullMap.put(player, sentStack.clone());
        }
      }
      List<String> lore = ItemStackExtensionsKt.getLore(sentStack);
      lore = PlaceholderAPI.setPlaceholders(player, lore);
      ItemStackExtensionsKt.setLore(sentStack, lore);
    }
    PacketContainer setSlotPacket = new PacketContainer(PacketType.Play.Server.SET_SLOT);
    setSlotPacket.getIntegers().write(0, 0);
    setSlotPacket.getIntegers().write(1, slot);
    setSlotPacket.getItemModifier().write(0, sentStack);
    return setSlotPacket;
  }
}
