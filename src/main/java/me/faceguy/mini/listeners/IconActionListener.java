package me.faceguy.mini.listeners;

import me.faceguy.mini.MiniInvyGui;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class IconActionListener implements Listener {

  private final MiniInvyGui plugin;

  public IconActionListener(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onItemPickup(final EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    sendUpdate((Player) event.getEntity());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onRespawn(final PlayerRespawnEvent event) {
    sendUpdate(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInvyClose(final InventoryCloseEvent event) {
    sendUpdate((Player) event.getPlayer());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInvyClick(final InventoryClickEvent event) {
    if (!(event.getInventory().getHolder() instanceof Player)) {
      return;
    }
    Player player = (Player) event.getInventory().getHolder();
    sendUpdate(player);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTeleport(final PlayerTeleportEvent event) {
    sendUpdate(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onJoin(final PlayerJoinEvent event) {
    sendUpdate(event.getPlayer());
  }

  private void sendUpdate(Player player) {
    if (player.hasMetadata("NPC")) {
      return;
    }
    if (!(player.getGameMode() == GameMode.SURVIVAL
        || player.getGameMode() == GameMode.ADVENTURE)) {
      return;
    }
    Bukkit.getScheduler()
        .runTaskLater(plugin, () -> plugin.getPacketManager().sendCraftGridPackets(player), 2L);
  }
}
