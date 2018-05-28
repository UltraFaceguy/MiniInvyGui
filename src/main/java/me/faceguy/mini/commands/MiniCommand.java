package me.faceguy.mini.commands;

import static com.tealcube.minecraft.bukkit.facecore.utilities.MessageUtils.sendMessage;

import me.faceguy.mini.MiniInvyGui;
import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.Command;

public class MiniCommand {

  private MiniInvyGui plugin;

  public MiniCommand(MiniInvyGui plugin) {
    this.plugin = plugin;
  }

  @Command(identifier = "minigui reload", permissions = "mini.invy.admin", onlyPlayers = false)
  public void reloadCommand(CommandSender sender) {
    plugin.disable();
    plugin.enable();
    sendMessage(sender, "&aMiniInvyGui has been reloaded!");
  }
}
