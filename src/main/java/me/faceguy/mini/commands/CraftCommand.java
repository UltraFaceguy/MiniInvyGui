package me.faceguy.mini.commands;

import com.tealcube.minecraft.bukkit.shade.acf.BaseCommand;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandAlias;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.Default;
import me.faceguy.mini.MiniInvyGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@CommandAlias("craft")
public class CraftCommand extends BaseCommand {

  private final MiniInvyGui plugin;

  public CraftCommand(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @Default()
  public void reloadCommand(Player player) {
    Inventory inv = Bukkit.createInventory(player, InventoryType.WORKBENCH, "");
    player.openInventory(inv);
  }
}
