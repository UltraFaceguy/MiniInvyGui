package me.faceguy.mini.tasks;

import me.faceguy.mini.MiniInvyGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class IconPacketSendTask extends BukkitRunnable {

  private MiniInvyGui plugin;

  public IconPacketSendTask(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    for (Player p : Bukkit.getOnlinePlayers()) {
      plugin.getPacketUtil().sendCraftGridPackets(p);
    }
  }
}

