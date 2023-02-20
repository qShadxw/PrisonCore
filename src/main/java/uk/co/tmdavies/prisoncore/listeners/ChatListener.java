package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.managers.ChatManager;

public class ChatListener implements Listener {

    public static String chatFormat;

    public ChatListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

        chatFormat = plugin.getConfig().getString("chat-format");

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        String message = ChatManager.formatMessagePlayer(event.getPlayer(),
                chatFormat.replace("%message%", event.getMessage()));

        ChatManager.sendChatMessage(ChatManager.Channel.GENERAL, message);

    }

}
