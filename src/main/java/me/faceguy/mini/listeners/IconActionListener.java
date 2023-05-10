package me.faceguy.mini.listeners;

import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.managers.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public record IconActionListener(MiniInvyGui plugin, PacketManager packetManager) implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onItemPickup(final EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player)) return;
    sendUpdate((Player) event.getEntity());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onRespawn(final PlayerRespawnEvent event) {
    sendUpdate(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInvyClose(final InventoryCloseEvent event) {
    Player player = (Player) event.getPlayer();
    sendUpdate(player);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInvyClick(final InventoryClickEvent event) {
    if (!(event.getInventory().getType() == InventoryType.CRAFTING
        || event.getInventory().getType() == InventoryType.CREATIVE)) {
      return;
    }
    if (event.getRawSlot() > -1 && event.getRawSlot() < 5) event.setCancelled(true);
    new BukkitRunnable() {
      @Override
      public void run() {
        Player observer = (Player) event.getWhoClicked();
        packetManager.sendCraftGridPackets(observer);
        ItemStack cursor = event.getCursor();
        if ((event.getRawSlot() > -1 && event.getRawSlot() < 5) && cursor != null && cursor.getType() == Material.AIR) {
          packetManager.setCursorToAir(observer);
        }
      }
    }.runTaskLater(plugin, 1L);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTeleport(final PlayerTeleportEvent event) {
    sendUpdate(event.getPlayer());
  }

  private void sendUpdate(Player player) {
    if (player.hasMetadata("NPC")) {
      return;
    }
    //Bukkit.getScheduler().runTaskLater(plugin, () ->
    //    plugin.getPacketManager().sendCraftGridPackets(player), 5L);
  }
}
