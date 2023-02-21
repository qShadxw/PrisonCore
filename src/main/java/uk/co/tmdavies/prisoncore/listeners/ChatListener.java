package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.managers.ChatManager;
import uk.co.tmdavies.prisoncore.objects.ChatMessage;

public class ChatListener implements Listener {

    public static String chatFormat;

    public ChatListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

        chatFormat = plugin.getConfig().getString("chat-format");

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        ChatMessage chatMessage = new ChatMessage(event.getPlayer(), chatFormat.replace("%message%", event.getMessage()));

        ChatManager.sendChatMessage(ChatManager.Channel.GENERAL, chatMessage);

    }

}
