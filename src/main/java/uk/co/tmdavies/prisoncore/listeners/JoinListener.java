package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.managers.ChatManager;
import uk.co.tmdavies.prisoncore.objects.ChatMessage;
import uk.co.tmdavies.prisoncore.objects.Profile;

public class JoinListener implements Listener {

    public static String joinFormat;
    public static String quitFormat;

    public JoinListener(PrisonCore plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        joinFormat = plugin.getConfig().getString("join-format");
        quitFormat = plugin.getConfig().getString("quit-format");
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("prison.join.vip")) {
            event.setJoinMessage("");
            return;
        }

        event.setJoinMessage(ChatManager.formatMessagePlayer(new ChatMessage(event.getPlayer(), joinFormat)));
        PrisonCore.playerProfiles.put(event.getPlayer(), new Profile(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        if (!event.getPlayer().hasPermission("prison.join.vip")) {
            event.setQuitMessage("");
            return;
        }
        event.setQuitMessage(ChatManager.formatMessagePlayer(new ChatMessage(event.getPlayer(), quitFormat)));
        PrisonCore.playerProfiles.remove(event.getPlayer());
    }
}
