package me.faceguy.mini.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.managers.PacketManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class InventoryPacketListener {

    private final MiniInvyGui plugin;
    private final PacketManager packetManager;
    private final ProtocolManager protocolManager;

    public InventoryPacketListener(MiniInvyGui plugin, PacketManager packetManager, ProtocolManager protocolManager) {
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
                        if (isNotValid(event.getPlayer())) return;

                        PacketContainer handle = event.getPacket();
                        if (isNotPlayerInventory(handle.getIntegers().read(0))) return;
                        packetManager.modifyPacket(event.getPlayer(), handle);
                    }
                });
    }

    public void registerAutoRecipeListener() {
        protocolManager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.AUTO_RECIPE) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (isNotValid(event.getPlayer())) return;

                        PacketContainer handle = event.getPacket();
                        if (isNotPlayerInventory(handle.getIntegers().read(0))) return;
                        event.setCancelled(true);
                    }
                });

        protocolManager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.RECIPE_UPDATE) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (isNotValid(event.getPlayer())) return;

                        PacketContainer handle = event.getPacket();
                        if (isNotPlayerInventory(handle.getIntegers().read(0))) return;
                        event.setCancelled(true);
                    }
                });
    }

    private boolean isNotValid(Player player) {
        return !player.isOnline() || !(player.getGameMode() == GameMode.SURVIVAL
                || player.getGameMode() == GameMode.ADVENTURE);
    }

    private boolean isNotPlayerInventory(int val) {
        return val != 0;
    }
}
