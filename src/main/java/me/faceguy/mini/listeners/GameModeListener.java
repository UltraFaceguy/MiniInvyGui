package me.faceguy.mini.listeners;

import me.faceguy.mini.MiniInvyGui;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GameModeListener implements Listener {

  private MiniInvyGui plugin;

  public GameModeListener(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onGameModeChange(final PlayerGameModeChangeEvent event) {
    if (event.getNewGameMode() == GameMode.CREATIVE) {
      plugin.getPacketUtil().sendBlankGridPackets(event.getPlayer());
    }
  }
}
