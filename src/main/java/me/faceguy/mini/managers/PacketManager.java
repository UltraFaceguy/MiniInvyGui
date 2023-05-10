package me.faceguy.mini.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.tealcube.minecraft.bukkit.facecore.utilities.TextUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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

  private void sendPacket(Player player, PacketContainer container) {
    try {
      protocolManager.sendServerPacket(player, container);
    } catch (InvocationTargetException exception) {
      logger.log(Level.WARNING,
              String.format("Unable to send craft grid packets to %s", player.getName()), exception);
    }
  }

  public void sendCraftGridPackets(Player player) {
    if (!player.isOnline()) {
      return;
    }
    switch (player.getGameMode()) {
      case CREATIVE, SPECTATOR -> sendAir(player);
      case SURVIVAL, ADVENTURE -> sendGrid(player);
    }
  }

  private void sendGrid(Player player) {
    PacketContainer handle = protocolManager.createPacket(PacketType.Play.Server.WINDOW_ITEMS);
    handle.getIntegers().write(0, 0);
    sendPacket(player, handle);
    sendCursor(player);
  }

  private void sendAir(Player player) {
    ItemStack air = new ItemStack(Material.AIR);
    ArrayList<ItemStack> stackArrayList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      stackArrayList.add(air);
    }
    PacketContainer handle = protocolManager.createPacket(PacketType.Play.Server.WINDOW_ITEMS);
    handle.getItemListModifier().write(0, stackArrayList);
    sendPacket(player, handle);
  }

  private void sendCursor(Player player) {
    PacketContainer handle = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
    handle.getIntegers().write(0, -1);
    handle.getIntegers().write(2, -1);
    handle.getItemModifier().write(0, player.getItemOnCursor());
    sendPacket(player, handle);
  }

  public void modifyWindowPacket(Player player, PacketContainer handle) {
    handle.getItemModifier().write(0, player.getItemOnCursor());
    List<ItemStack> stackList = handle.getItemListModifier().read(0);
    boolean empty = stackList.isEmpty();
    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
      for (int i = 0; i < 5; i++) {
        stackList.set(i, new ItemStack(Material.AIR));
      }
    } else {
      for (int i = 0; i < 5; i++) {
        ItemStack stack = itemManager.getFromSlot(i).getItemIcon();
        if (stack != null) {
          stack = stack.clone();
          if (stack.getType() == Material.PLAYER_WALL_HEAD) {
            if (skullMap.containsKey(player)) {
              stack = skullMap.get(player).clone();
            } else {
              stack.setType(Material.PLAYER_HEAD);
              SkullMeta meta = (SkullMeta) stack.getItemMeta();
              assert meta != null;
              meta.setOwningPlayer(player);
              stack.setItemMeta(meta);
              skullMap.put(player, stack.clone());
            }
          }
          List<String> lore = TextUtils.getLore(stack);
          lore = PlaceholderAPI.setPlaceholders(player, lore);
          TextUtils.setLore(stack, lore);
        }
        if (empty)
          stackList.add(stack);
        else
          stackList.set(i, stack);
      }
    }
    handle.getItemListModifier().write(0, stackList);
  }

  public void setCursorToAir(Player player) {
    PacketContainer handle = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
    handle.getIntegers().write(0, -1);
    handle.getIntegers().write(2, -1);
    handle.getItemModifier().write(0, new ItemStack(Material.AIR));
    sendPacket(player, handle);
  }
}
