package me.faceguy.mini;

import com.tealcube.minecraft.bukkit.facecore.plugin.FacePlugin;
import io.pixeloutlaw.minecraft.spigot.config.MasterConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import java.io.File;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.faceguy.mini.commands.MiniCommand;
import me.faceguy.mini.listeners.GameModeListener;
import me.faceguy.mini.listeners.IconActionListener;
import me.faceguy.mini.listeners.IconClickListener;
import me.faceguy.mini.managers.ItemManager;
import me.faceguy.mini.tasks.IconPacketSendTask;
import me.faceguy.mini.managers.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import se.ranzdo.bukkit.methodcommand.CommandHandler;

public class MiniInvyGui extends FacePlugin {

  private MasterConfiguration settings;
  private VersionedSmartYamlConfiguration configYAML;
  private ItemManager itemManager;
  private PacketManager packetManager;
  private IconPacketSendTask packetTask;
  private CommandHandler commandHandler;

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
      getLogger().info("HeadDatbase found. Head support GET!");
    } else {
      HEAD_API = null;
      getLogger().info("HeadDatbase not found. No head support");
    }

    itemManager = new ItemManager(this);
    packetManager = new PacketManager(itemManager);

    packetTask = new IconPacketSendTask(this);

    packetTask.runTaskTimer(this,
        20L * 10, // Start timer after 10s
        20L * 5 // Run it every 5s after
    );

    commandHandler = new CommandHandler(this);
    commandHandler.registerCommands(new MiniCommand(this));

    Bukkit.getPluginManager().registerEvents(new IconActionListener(this), this);
    Bukkit.getPluginManager().registerEvents(new IconClickListener(this), this);
    Bukkit.getPluginManager().registerEvents(new GameModeListener(this), this);
  }

  @Override
  public void disable() {
    HandlerList.unregisterAll(this);
    settings = null;
    itemManager = null;
    packetManager = null;
    packetTask = null;
    configYAML = null;
    commandHandler = null;
  }

  public MasterConfiguration getSettings() {
    return settings;
  }

  public ItemManager getItemManager() {
    return itemManager;
  }

  public PacketManager getPacketManager() {
    return packetManager;
  }
}
