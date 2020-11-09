package me.faceguy.mini.managers;

import com.tealcube.minecraft.bukkit.shade.apache.commons.lang3.StringUtils;
import io.pixeloutlaw.minecraft.spigot.garbage.ListExtensionsKt;
import io.pixeloutlaw.minecraft.spigot.garbage.StringExtensionsKt;
import io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt;
import java.util.List;
import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.objects.InvyItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemManager {

  private final MiniInvyGui plugin;
  private InvyItem topLeft;
  private InvyItem topRight;
  private InvyItem bottomLeft;
  private InvyItem bottomRight;
  private InvyItem auxItem;

  public ItemManager(MiniInvyGui plugin) {
    this.plugin = plugin;
    regenerateItems();
  }

  public InvyItem getTopLeft() {
    return topLeft;
  }

  public InvyItem getTopRight() {
    return topRight;
  }

  public InvyItem getBottomLeft() {
    return bottomLeft;
  }

  public InvyItem getBottomRight() {
    return bottomRight;
  }

  public InvyItem getAuxItem() {
    return auxItem;
  }

  public void regenerateItems() {
    topLeft = generateItem("top-left");
    topRight = generateItem("top-right");
    bottomLeft = generateItem("bottom-left");
    bottomRight = generateItem("bottom-right");
    auxItem = generateItem("aux");
  }

  private InvyItem generateItem(String itemIdentifier) {
    String headString = plugin.getSettings().getString("config.icons." + itemIdentifier + ".head");
    ItemStack item;
    Bukkit.getLogger().info("Loading slot: " + itemIdentifier);
    if ("OWNER_HEAD".equals(headString)) {
      item = new ItemStack(Material.PLAYER_WALL_HEAD);
    } else if (StringUtils.isNotBlank(headString) && MiniInvyGui.HEAD_API != null) {
      try {
        item = MiniInvyGui.HEAD_API.getItemHead(headString);
        Bukkit.getLogger().info(" - Using head: " + headString);
      } catch (Exception e) {
        item = MiniInvyGui.HEAD_API.getRandomHead();
        Bukkit.getLogger().info(" - Head not found. Using random head");
      }
      if (item == null) {
        item = new ItemStack(Material.PLAYER_HEAD);
      }
    } else {
      Material material = Material.valueOf(plugin.getSettings().getString("config.icons." + itemIdentifier + ".icon"));
      Bukkit.getLogger().info(" - Using material: " + material);
      int amount = plugin.getSettings().getInt("config.icons." + itemIdentifier + ".amount", 1);
      item = new ItemStack(material, amount);
    }

    ItemStackExtensionsKt.setDisplayName(item,
        StringExtensionsKt.chatColorize(plugin.getSettings().getString("config.icons." + itemIdentifier + ".name")));

    int customData = plugin.getSettings().getInt("config.icons." + itemIdentifier + ".custom-data", 0);
    if (customData != 0) {
      ItemStackExtensionsKt.setCustomModelData(item, customData);
    }

    List<String> lore = ListExtensionsKt
        .chatColorize(plugin.getSettings().getStringList("config.icons." + itemIdentifier + ".lore"));
    ItemStackExtensionsKt.setLore(item, lore);
    ItemStackExtensionsKt.addItemFlags(item, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS,
        ItemFlag.HIDE_POTION_EFFECTS);

    InvyItem invyItem = new InvyItem();
    invyItem.setItemIcon(item);

    String sound = plugin.getSettings()
        .getString("config.icons." + itemIdentifier + ".sound-effect", null);
    invyItem.setSoundEffect(sound != null ? Sound.valueOf(sound) : null);

    invyItem.setSoundVolume((float) plugin.getSettings()
        .getDouble("config.icons." + itemIdentifier + ".sound-volume", 1));
    invyItem.setSoundPitch((float) plugin.getSettings()
        .getDouble("config.icons." + itemIdentifier + ".sound-pitch", 1));

    invyItem.setPlayerCommandList(
        plugin.getSettings().getStringList("config.icons." + itemIdentifier + ".player-commands"));
    invyItem.setConsoleCommandList(
        plugin.getSettings().getStringList("config.icons." + itemIdentifier + ".console-commands"));

    return invyItem;
  }
}
