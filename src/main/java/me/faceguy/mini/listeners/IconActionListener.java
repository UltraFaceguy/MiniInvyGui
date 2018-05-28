package me.faceguy.mini.listeners;

import me.faceguy.mini.MiniInvyGui;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class IconActionListener implements Listener {

  private MiniInvyGui plugin;

  public IconActionListener(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onItemPickup(final EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player && ((Player) event.getEntity()).getGameMode() == GameMode.SURVIVAL) {
      plugin.getPacketUtil().sendCraftGridPackets((Player) event.getEntity());
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onRespawn(final PlayerRespawnEvent event) {
    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
      @Override
      public void run() {
        plugin.getPacketUtil().sendCraftGridPackets(event.getPlayer());
      }
    }, 2L);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInvyClose(final InventoryCloseEvent event) {
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
      @Override
      public void run() {
        plugin.getPacketUtil().sendCraftGridPackets((Player) event.getPlayer());
      }
    }, 2L);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTeleport(final PlayerTeleportEvent event) {
    if (event.getPlayer().hasMetadata("NPC") || event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
      @Override
      public void run() {
        plugin.getPacketUtil().sendCraftGridPackets(event.getPlayer());
      }
    }, 2L);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onJoin(final PlayerJoinEvent event) {
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
      @Override
      public void run() {
        plugin.getPacketUtil().sendCraftGridPackets(event.getPlayer());
      }
    }, 2L);
  }
}
