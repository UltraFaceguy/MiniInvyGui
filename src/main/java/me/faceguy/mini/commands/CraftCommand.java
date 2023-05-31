package me.faceguy.mini.commands;

import com.tealcube.minecraft.bukkit.shade.acf.BaseCommand;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandAlias;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.Default;
import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.listeners.IconClickListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

@CommandAlias("craft")
public class CraftCommand extends BaseCommand {

  private final MiniInvyGui plugin;

  public CraftCommand(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @Default()
  public void craftCommand(Player player) {
    IconClickListener.ignoreCraft.add(player.getUniqueId());
    InventoryView view = player.getPlayer().openWorkbench(player.getLocation(), true);
    view.setTitle("");
    IconClickListener.ignoreCraft.remove(player.getUniqueId());
  }
}
