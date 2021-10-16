package me.faceguy.mini.listeners;

import me.faceguy.mini.MiniInvyGui;
import me.faceguy.mini.managers.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GameModeListener implements Listener {

    private final MiniInvyGui plugin;
    private final PacketManager packetManager;

    public GameModeListener(MiniInvyGui plugin, PacketManager packetManager) {
        this.plugin = plugin;
        this.packetManager = packetManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameModeChange(final PlayerGameModeChangeEvent event) {
        GameMode gameMode = event.getNewGameMode();
        Player player = event.getPlayer();

        if (gameMode == GameMode.CREATIVE) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> packetManager.setCraftGridToAir(player), 1L);
        }
        else if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> packetManager.sendCraftGridPackets(player), 1L);
        }
    }
}
