package me.faceguy.mini.managers;

import com.tealcube.minecraft.bukkit.TextUtils;
import io.pixeloutlaw.minecraft.spigot.hilt.HiltItemStack;
import java.util.List;
import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.objects.InvyItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {

  private final MiniInvyGui plugin;
  private InvyItem topLeft;
  private InvyItem topRight;
  private InvyItem bottomLeft;
  private InvyItem bottomRight;
  private InvyItem auxItem;

  public ItemManager(MiniInvyGui plugin) {
    this.plugin = plugin;
    this.topLeft = generateItem("top-left");
    this.topRight = generateItem("top-right");
    this.bottomLeft = generateItem("bottom-left");
    this.bottomRight = generateItem("bottom-right");
    this.auxItem = generateItem("aux");
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

  private InvyItem generateItem(String itemIdentifier) {
    Material material = Material.getMaterial(plugin.getSettings().getString("config.icons." + itemIdentifier + ".icon"));
    int amount = plugin.getSettings().getInt("config.icons." + itemIdentifier + ".amount", 1);
    HiltItemStack item = new HiltItemStack(material, amount);
    item.setName(TextUtils.color(plugin.getSettings().getString("config.icons." + itemIdentifier + ".name")));

    List<String> lore = TextUtils.color(plugin.getSettings().getStringList("config.icons." + itemIdentifier + ".lore"));
    item.setLore(lore);

    ItemMeta meta = item.getItemMeta();
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
    item.setItemMeta(meta);

    InvyItem invyItem = new InvyItem();
    invyItem.setItemIcon(item);

    String sound = plugin.getSettings().getString("config.icons." + itemIdentifier + ".sound-effect", null);
    invyItem.setSoundEffect(sound != null ? Sound.valueOf(sound) : null);

    invyItem.setSoundVolume((float) plugin.getSettings().getDouble("config.icons." + itemIdentifier + ".sound-volume", 1));
    invyItem.setSoundPitch((float) plugin.getSettings().getDouble("config.icons." + itemIdentifier + ".sound-pitch", 1));

    invyItem.setPlayerCommandList(plugin.getSettings().getStringList("config.icons." + itemIdentifier + ".player-commands"));
    invyItem.setConsoleCommandList(plugin.getSettings().getStringList("config.icons." + itemIdentifier + ".console-commands"));

    return invyItem;
  }
}
