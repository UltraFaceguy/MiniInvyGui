package me.faceguy.mini.tasks;

import me.faceguy.mini.MiniInvyGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class IconUpdateTask extends BukkitRunnable {

  private final MiniInvyGui plugin;

  public IconUpdateTask(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      //plugin.getPacketManager().sendCraftGridPackets(player);
    }
  }
}
