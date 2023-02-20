package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.managers.ChatManager;
import uk.co.tmdavies.prisoncore.objects.PrisonPlayer;

public class JoinListener implements Listener {

    public static String joinConfig;
    public static String quitConfig;

    public JoinListener(PrisonCore plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        joinConfig = plugin.getConfig().getString("join-format");
        quitConfig = plugin.getConfig().getString("quit-format");
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("prison.join.vip")) {
            event.setJoinMessage("");
            return;
        }
        event.setJoinMessage(ChatManager.formatMessagePlayer(event.getPlayer(), joinConfig));
        PrisonCore.prisonPlayers.put(event.getPlayer(), new PrisonPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        if (!event.getPlayer().hasPermission("prison.join.vip")) {
            event.setQuitMessage("");
            return;
        }
        event.setQuitMessage(ChatManager.formatMessagePlayer(event.getPlayer(), quitConfig));
        PrisonCore.prisonPlayers.remove(event.getPlayer());
    }
}
