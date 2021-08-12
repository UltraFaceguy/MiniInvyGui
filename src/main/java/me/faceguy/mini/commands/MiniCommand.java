package me.faceguy.mini.commands;

import static com.tealcube.minecraft.bukkit.facecore.utilities.MessageUtils.sendMessage;

import com.tealcube.minecraft.bukkit.shade.acf.BaseCommand;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandAlias;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandPermission;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.Subcommand;
import me.faceguy.mini.MiniInvyGui;
import org.bukkit.command.CommandSender;

@CommandAlias("minigui")
public class MiniCommand extends BaseCommand {

  private final MiniInvyGui plugin;

  public MiniCommand(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @Subcommand("reload")
  @CommandPermission("minigui.reload")
  public void reloadCommand(CommandSender sender) {
    plugin.disable();
    plugin.enable();
    sendMessage(sender, "&aMiniInvyGui has been reloaded!");
  }
}
