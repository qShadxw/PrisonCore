package uk.co.tmdavies.prisoncore.managers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

        String chatcolour = "&7";
        switch (PrisonCore.perms.getPlayerGroups(player)[0]) {
            case "helper":
            case "mod":
            case "admin":
            case "manager":
            case "owner":
                chatcolour = "&f";
                break;
            default:
                chatcolour = "&7";
                break;
        }

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = mainHand.getItemMeta();
        String itemDisplayName = " ";

        if (itemMeta != null) {
            itemDisplayName = itemMeta.getDisplayName();
        }

        String newMessage = message
                .replace("%player%", player.getDisplayName())
                .replace("%chat-colour%", chatcolour)
                .replace("[item]", itemDisplayName)
                .replace("{item}", itemDisplayName)
                .replace("[i]", itemDisplayName)
                .replace("{i}", itemDisplayName);


        if (PrisonCore.papiEnabled) newMessage = PlaceholderAPI.setPlaceholders(player, newMessage);

        return Utils.Colour(newMessage);

    }


    public enum Channel {

        SYSTEM,
        GENERAL,
        PRIVATE;

    }

}
