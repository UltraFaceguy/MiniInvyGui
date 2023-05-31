package me.faceguy.mini.listeners;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.managers.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent.Cause;

public class GameModeListener implements Listener {

  private final MiniInvyGui plugin;
  private final PacketManager packetManager;

  private final Set<UUID> cancelModes = new HashSet<>();

  public GameModeListener(MiniInvyGui plugin, PacketManager packetManager) {
    this.plugin = plugin;
    this.packetManager = packetManager;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onGamemodeSwap(final PlayerGameModeChangeEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (cancelModes.contains(event.getPlayer().getUniqueId())) {
      cancelModes.remove(event.getPlayer().getUniqueId());
      return;
    }
    event.setCancelled(true);
    packetManager.sendAir(event.getPlayer());
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      packetManager.sendAir(event.getPlayer());
      cancelModes.add(event.getPlayer().getUniqueId());
      event.getPlayer().setGameMode(event.getNewGameMode());
    }, 0L);
  }
}
