package me.faceguy.mini.listeners;

import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.managers.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent.Cause;
import org.bukkit.inventory.Inventory;

public class GameModeListener implements Listener {

  private final MiniInvyGui plugin;
  private final PacketManager packetManager;

  public GameModeListener(MiniInvyGui plugin, PacketManager packetManager) {
    this.plugin = plugin;
    this.packetManager = packetManager;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onCraftOpen(final InventoryOpenEvent event) {
    if (event.isCancelled() || event.getInventory().getType() != InventoryType.WORKBENCH) {
      return;
    }
    if (event.getInventory().getLocation() == null) {
      return;
    }
    event.setCancelled(true);
    Inventory inv = Bukkit.createInventory(event.getPlayer(), InventoryType.WORKBENCH, "");
    event.getPlayer().openInventory(inv);
  }
}
