package uk.co.tmdavies.prisoncore.managers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.utils.Utils;

public class ChatManager {

    /**
     *
     * Broadcasts a public message to everyone with a
     * given channel.
     *
     * @param channel Channel which the message is sent to.
     * @param message Message that is sent.
     */
    public static void sendChatMessage(Channel channel, String message) {

        Bukkit.broadcastMessage(Utils.Colour(message));

    }

    /**
     *
     * Sends a message to a player to the given
     * channel.
     *
     * @param channel Channel which the message is sent to.
     * @param player Player which the message is sent to.
     * @param message Message that is sent.
     */
    public static void sendPlayerMessage(Channel channel, Player player, String message) {

        // Check if they have channel enabled.
        player.sendMessage(Utils.Colour(message));

    }

    /**
     *
     * Quick and simple replacing for uses everywhere
     * in the project.
     *
     * @param player Given Player.
     * @param message Given Message.
     * @return Returns formatted string.
     */
    public static String formatMessagePlayer(Player player, String message) {

        String newMessage = message.replace("%player%", player.getDisplayName());

        System.out.println(PrisonCore.papiEnabled);

        if (PrisonCore.papiEnabled) newMessage = PlaceholderAPI.setPlaceholders(player, newMessage);

        return Utils.Colour(newMessage);

    }


    public enum Channel {

        SYSTEM,
        GENERAL,
        PRIVATE;

    }

}
