package me.faceguy.mini.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PacketManager {

    private final ItemManager itemManager;
    private final Logger logger;
    private final ProtocolManager protocolManager;

    public PacketManager(ItemManager itemManager, Logger logger, ProtocolManager protocolManager) {
        this.itemManager = itemManager;
        this.logger = logger;
        this.protocolManager = protocolManager;
    }

    public void sendCraftGridPackets(Player player) {
        if (!player.isOnline() || player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        try {
            protocolManager.sendServerPacket(player, buildPacket(player, 1, itemManager.getTopLeft().getItemIcon()));
            protocolManager.sendServerPacket(player, buildPacket(player, 2, itemManager.getTopRight().getItemIcon()));
            protocolManager.sendServerPacket(player, buildPacket(player, 3, itemManager.getBottomLeft().getItemIcon()));
            protocolManager.sendServerPacket(player, buildPacket(player, 4, itemManager.getBottomRight().getItemIcon()));
            protocolManager.sendServerPacket(player, buildPacket(player, 0, itemManager.getAuxItem().getItemIcon()));
        } catch (InvocationTargetException exception) {
            logger.log(Level.WARNING, String.format("Unable to send craft grid packets to %s", player.getName()), exception);
        }
    }

    public void sendBlankGridPackets(Player player) {
        try {
            protocolManager.sendServerPacket(player, buildPacket(player, 1, null));
            protocolManager.sendServerPacket(player, buildPacket(player, 2, null));
            protocolManager.sendServerPacket(player, buildPacket(player, 3, null));
            protocolManager.sendServerPacket(player, buildPacket(player, 4, null));
            protocolManager.sendServerPacket(player, buildPacket(player, 0, null));
        } catch (InvocationTargetException exception) {
            logger.log(Level.WARNING, String.format("Unable to send craft grid packets to %s", player.getName()), exception);
        }
    }

    private PacketContainer buildPacket(Player player, int slot, @Nullable ItemStack stack) {
        ItemStack sentStack = null;
        if (stack != null) {
            sentStack = stack.clone();
            List<String> lore = ItemStackExtensionsKt.getLore(sentStack);
            lore = PlaceholderAPI.setPlaceholders(player, lore);
            ItemStackExtensionsKt.setLore(sentStack, lore);
        }
        PacketContainer setSlotPacket = new PacketContainer(PacketType.Play.Server.SET_SLOT);
        setSlotPacket.getIntegers().write(0, 0);
        setSlotPacket.getIntegers().write(1, slot);
        setSlotPacket.getItemModifier().write(0, sentStack);
        return setSlotPacket;
    }
}
