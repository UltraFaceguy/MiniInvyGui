package me.faceguy.mini.listeners;

import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.objects.InvyItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class IconClickListener implements Listener {

  private final MiniInvyGui plugin;

  public IconClickListener(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInvyClick(final InventoryClickEvent event) {
    if (event.getInventory().getType() != InventoryType.CRAFTING) {
      return;
    }
    Player player = (Player) event.getWhoClicked();
    if (!(player.getGameMode() == GameMode.SURVIVAL
        || player.getGameMode() == GameMode.ADVENTURE)) {
      return;
    }
    if (event.getRawSlot() > 4 || event.getRawSlot() < 0) {
      plugin.getPacketManager().sendCraftGridPackets(player);
      return;
    }
    event.setCancelled(true);
    player.closeInventory();
    switch (event.getRawSlot()) {
      case 0 -> {
        playButtonSound(player, plugin.getItemManager().getAuxItem());
        plugin.getItemManager().getAuxItem().executePlayerCommands(player);
        plugin.getItemManager().getAuxItem().executeConsoleCommands(player);
      }
      case 1 -> {
        playButtonSound(player, plugin.getItemManager().getTopLeft());
        plugin.getItemManager().getTopLeft().executePlayerCommands(player);
        plugin.getItemManager().getTopLeft().executeConsoleCommands(player);
      }
      case 2 -> {
        playButtonSound(player, plugin.getItemManager().getTopRight());
        plugin.getItemManager().getTopRight().executePlayerCommands(player);
        plugin.getItemManager().getTopRight().executeConsoleCommands(player);
      }
      case 3 -> {
        playButtonSound(player, plugin.getItemManager().getBottomLeft());
        plugin.getItemManager().getBottomLeft().executePlayerCommands(player);
        plugin.getItemManager().getBottomLeft().executeConsoleCommands(player);
      }
      case 4 -> {
        playButtonSound(player, plugin.getItemManager().getBottomRight());
        plugin.getItemManager().getBottomRight().executePlayerCommands(player);
        plugin.getItemManager().getBottomRight().executeConsoleCommands(player);
      }
      default -> plugin.getLogger().warning("Something is VERY wrong??");
    }
    Bukkit.getScheduler().runTaskLater(plugin,
        () -> plugin.getPacketManager().sendCraftGridPackets(player), 2L);
  }

  private void playButtonSound(Player player, InvyItem item) {
    if (item.getSoundEffect() == null) {
      return;
    }
    player.playSound(player.getLocation(), item.getSoundEffect(), item.getSoundVolume(),
        item.getSoundPitch());
  }
}
