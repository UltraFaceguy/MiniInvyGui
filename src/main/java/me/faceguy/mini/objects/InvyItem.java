package me.faceguy.mini.objects;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InvyItem {

  private ItemStack itemIcon;
  private Sound soundEffect;
  private float soundVolume;
  private float soundPitch;
  private List<String> playerCommandList;
  private List<String> consoleCommandList;

  public void executePlayerCommands(Player player) {
    for (String s : playerCommandList) {
      s = s.replace("%player%", player.getName());
      Bukkit.getServer().dispatchCommand(player, s);
    }
  }

  public void executeConsoleCommands(Player player) {
    for (String s : consoleCommandList) {
      s = s.replace("%player%", player.getName());
      Bukkit.getServer().dispatchCommand(player.getServer().getConsoleSender(), s);
    }
  }

  public ItemStack getItemIcon() {
    return itemIcon;
  }

  public void setItemIcon(ItemStack itemIcon) {
    this.itemIcon = itemIcon;
  }

  public Sound getSoundEffect() {
    return soundEffect;
  }

  public void setSoundEffect(Sound soundEffect) {
    this.soundEffect = soundEffect;
  }

  public float getSoundVolume() {
    return soundVolume;
  }

  public void setSoundVolume(float soundVolume) {
    this.soundVolume = soundVolume;
  }

  public float getSoundPitch() {
    return soundPitch;
  }

  public void setSoundPitch(float soundPitch) {
    this.soundPitch = soundPitch;
  }

  public List<String> getPlayerCommandList() {
    return playerCommandList;
  }

  public void setPlayerCommandList(List<String> playerCommandList) {
    this.playerCommandList = playerCommandList;
  }

  public List<String> getConsoleCommandList() {
    return consoleCommandList;
  }

  public void setConsoleCommandList(List<String> consoleCommandList) {
    this.consoleCommandList = consoleCommandList;
  }
}
