package me.faceguy.mini;

import com.tealcube.minecraft.bukkit.facecore.plugin.FacePlugin;
import io.pixeloutlaw.minecraft.spigot.config.MasterConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import java.io.File;
import me.faceguy.mini.commands.MiniCommand;
import me.faceguy.mini.listeners.GameModeListener;
import me.faceguy.mini.listeners.IconActionListener;
import me.faceguy.mini.listeners.IconClickListener;
import me.faceguy.mini.managers.ItemManager;
import me.faceguy.mini.tasks.IconPacketSendTask;
import me.faceguy.mini.utils.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import se.ranzdo.bukkit.methodcommand.CommandHandler;

public class MiniInvyGui extends FacePlugin {

  private MasterConfiguration settings;
  private VersionedSmartYamlConfiguration configYAML;
  private ItemManager itemManager;
  private PacketUtil packetUtil;
  private IconPacketSendTask packetTask;
  private CommandHandler commandHandler;

  @Override
  public void enable() {
    configYAML = new VersionedSmartYamlConfiguration(new File(getDataFolder(), "config.yml"),
        getResource("config.yml"), VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (configYAML.update()) {
      getLogger().info("Updating config.yml");
    }
    settings = MasterConfiguration.loadFromFiles(configYAML);

    itemManager = new ItemManager(this);
    packetUtil = new PacketUtil(this);

    packetTask = new IconPacketSendTask(this);

    packetTask.runTaskTimer(this,
        20L * 5, // Start timer after 5s
        20L * 4 // Run it every 4s after
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
    packetUtil = null;
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

  public PacketUtil getPacketUtil() {
    return packetUtil;
  }
}
