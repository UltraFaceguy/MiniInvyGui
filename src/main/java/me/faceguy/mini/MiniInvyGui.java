package me.faceguy.mini;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.tealcube.minecraft.bukkit.facecore.plugin.FacePlugin;
import com.tealcube.minecraft.bukkit.shade.acf.PaperCommandManager;
import io.pixeloutlaw.minecraft.spigot.config.MasterConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import java.io.File;
import lombok.Getter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.faceguy.mini.commands.CraftCommand;
import me.faceguy.mini.commands.MiniCommand;
import me.faceguy.mini.listeners.*;
import me.faceguy.mini.managers.ItemManager;
import me.faceguy.mini.managers.PacketManager;
import me.faceguy.mini.tasks.IconUpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class MiniInvyGui extends FacePlugin {

  private MasterConfiguration settings;
  private VersionedSmartYamlConfiguration configYAML;
  @Getter
  private ItemManager itemManager;
  @Getter
  private PacketManager packetManager;
  private IconUpdateTask updateTask;
  private ProtocolManager protocolManager;

  public static HeadDatabaseAPI HEAD_API;

  @Override
  public void enable() {
    configYAML = new VersionedSmartYamlConfiguration(new File(getDataFolder(), "config.yml"),
        getResource("config.yml"), VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (configYAML.update()) {
      getLogger().info("Updating config.yml");
    }
    settings = MasterConfiguration.loadFromFiles(configYAML);

    if (Bukkit.getPluginManager().getPlugin("HeadDatabase") != null) {
      HEAD_API = new HeadDatabaseAPI();
      getLogger().info("HeadDatabase found. Head support GET!");
    } else {
      HEAD_API = null;
      getLogger().info("HeadDatabase not found. No head support");
    }

    itemManager = new ItemManager(this);
    protocolManager = ProtocolLibrary.getProtocolManager();
    packetManager = new PacketManager(itemManager, getLogger(), protocolManager);

    updateTask = new IconUpdateTask(this);
    updateTask.runTaskTimer(this,
        20L * 10, // Start timer after 10s
        20L * 5 // Run it every 5s after
    );

    PaperCommandManager commandManager = new PaperCommandManager(this);
    commandManager.registerCommand(new MiniCommand(this));
    commandManager.registerCommand(new CraftCommand(this));

    Bukkit.getPluginManager().registerEvents(new IconActionListener(this, packetManager), this);
    Bukkit.getPluginManager().registerEvents(new IconClickListener(this), this);
    Bukkit.getPluginManager().registerEvents(new GameModeListener(this, packetManager), this);
    new InventoryPacketListener(this, packetManager, protocolManager);

    /*
    if (Bukkit.getPluginManager().getPlugin("HeadDatabase") != null) {
      Bukkit.getPluginManager().registerEvents(new HeadLoadListener(this), this);
    }
    */
  }

  @Override
  public void disable() {
    HandlerList.unregisterAll(this);
    protocolManager.removePacketListeners(this);
    settings = null;
    itemManager = null;
    packetManager = null;
    updateTask = null;
    configYAML = null;
  }

  public MasterConfiguration getSettings() {
    return settings;
  }
}
