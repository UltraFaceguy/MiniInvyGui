package me.faceguy.mini.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.managers.PacketManager;

public class InventoryPacketListener {

  private final MiniInvyGui plugin;
  private final PacketManager packetManager;
  private final ProtocolManager protocolManager;

  public InventoryPacketListener(MiniInvyGui plugin, PacketManager packetManager,
      ProtocolManager protocolManager) {
    this.plugin = plugin;
    this.packetManager = packetManager;
    this.protocolManager = protocolManager;
    registerWindowItemsPacketListener();
    registerAutoRecipeListener();
  }

  public void registerWindowItemsPacketListener() {
    protocolManager.addPacketListener(
        new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.WINDOW_ITEMS) {
          @Override
          public void onPacketSending(PacketEvent event) {
            PacketContainer handle = event.getPacket();
            if (isNotPlayerInventory(handle.getIntegers().read(0))) {
              return;
            }
            packetManager.modifyWindowPacket(event.getPlayer(), handle);
          }
        });
  }

  public void registerAutoRecipeListener() {
    protocolManager.addPacketListener(
        new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.AUTO_RECIPE) {
          @Override
          public void onPacketSending(PacketEvent event) {
            PacketContainer handle = event.getPacket();
            if (isNotPlayerInventory(handle.getIntegers().read(0))) {
              return;
            }
            event.setCancelled(true);
          }
        });

    protocolManager.addPacketListener(
        new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.RECIPE_UPDATE) {
          @Override
          public void onPacketSending(PacketEvent event) {
            PacketContainer handle = event.getPacket();
            if (isNotPlayerInventory(handle.getIntegers().read(0))) {
              return;
            }
            event.setCancelled(true);
          }
        });
  }

  private boolean isNotPlayerInventory(int val) {
    return val != 0;
  }
}
