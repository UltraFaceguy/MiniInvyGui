package me.faceguy.mini.listeners;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.faceguy.mini.MiniInvyGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeadLoadListener implements Listener {

  private final MiniInvyGui plugin;

  public HeadLoadListener(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onDatabaseLoad(DatabaseLoadEvent e) {
    plugin.getItemManager().regenerateItems();
  }
}
