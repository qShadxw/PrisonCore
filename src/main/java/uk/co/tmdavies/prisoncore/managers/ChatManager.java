package uk.co.tmdavies.prisoncore.managers;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.ChatMessage;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.Objects;

public class ChatManager {

    /**
     *
     * Broadcasts a public message to everyone with a
     * given channel.
     *
     * @param channel Channel which the message is sent to.
     * @param chatMessage Chat Message.
     */
    public static void sendChatMessage(Channel channel, ChatMessage chatMessage) {

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.spigot().sendMessage(handleChatItem(chatMessage.getSender(), chatMessage.getFormattedMessage()));

        }

    }

    /**
     *
     * Sends a message to a player to the given
     * channel.
     *
     * @param channel Channel which the message is sent to.
     * @param chatMessage Chat Message.
     */
    public static void sendPlayerMessage(Channel channel, ChatMessage chatMessage) {

        // Check if they have channel enabled.
        chatMessage.getSender().spigot().sendMessage(handleChatItem(chatMessage.getSender(), chatMessage.getFormattedMessage()));

    }

    /**
     *
     * Quick and simple replacing for uses everywhere
     * in the project.
     *
     * @param chatMessage Chat Message.
     * @return Formatted String.
     */
    public static String formatMessagePlayer(ChatMessage chatMessage) {

        Player player = chatMessage.getSender();
        String message = chatMessage.getRawMessage();

        // Chat Colour
        String chatColour = "&7";

        if (!PrisonCore.perms.getPlayerGroups(player)[0].equalsIgnoreCase("default")) chatColour = "&f";

        // Placeholders
        String newMessage = message
                .replace("%player%", player.getDisplayName())
                .replace("%chat-colour%", chatColour);


        if (PrisonCore.papiEnabled) newMessage = PlaceholderAPI.setPlaceholders(player, newMessage);

        return Utils.Colour(newMessage);

    }

    /**
     *
     * Handles converting message into a BaseComponent.
     * This allows us to do hover text for the item
     * placeholder.
     *
     * @param player Player.
     * @param message Message.
     * @return BaseComponent Array.
     */
    private static BaseComponent[] handleChatItem(Player player, String message) {

        if (!message.contains("[item]")) return new BaseComponent[] { new TextComponent(Utils.Colour(message)) };

        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = handItem.getItemMeta();

        if (handItem == null || Objects.requireNonNull(handItem.getData()).getItemType() == Material.AIR)
            return new BaseComponent[] { new TextComponent(Utils.Colour(message.replace("[item]", "Hand"))) };
        if (itemMeta == null)
            return new BaseComponent[] { new TextComponent(Utils.Colour(message.replace("[item]", "Hand"))) };
        if (itemMeta.getLore() == null)
            return new BaseComponent[] { new TextComponent(Utils.Colour(message.replace("[item]", itemMeta.getDisplayName()))) };

        StringBuilder builder = new StringBuilder();

        builder.append(itemMeta.getDisplayName()).append("\n");

        for (String string : itemMeta.getLore()) builder.append(Utils.Colour(string)).append("\n");

        String lore = builder.substring(0, builder.toString().length() - 1);
        ComponentBuilder componentBuilder = new ComponentBuilder();
        String[] messageParts = message.split(" ");

        for (String string : messageParts) {
            if (Objects.equals(ChatColor.stripColor(string), "[item]")) {
                componentBuilder.append(itemMeta.getDisplayName() + " ").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(lore).create()));
                continue;
            }
            componentBuilder.append(Utils.Colour(string + " ")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().create()));
        }

        return componentBuilder.create();

    }

    public enum Channel {

        SYSTEM(0),
        GENERAL(1),
        PRIVATE(2);

        private final int id;

        Channel(int id) {

            this.id = id;

        }

        public int getId() {

            return this.id;

        }

        public static Channel getById(int id) {

            for (Channel channel : Channel.values())
                if (channel.getId() == id) return channel;

            return null;

        }

    }

}
