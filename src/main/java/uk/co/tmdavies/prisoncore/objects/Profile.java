package uk.co.tmdavies.prisoncore.objects;

import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.managers.ChatManager;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private final Player player;
    private List<ChatManager.Channel> activeChannels;

    public Profile(Player player) {

        this.player = player;
        this.activeChannels = new ArrayList<>();

    }

    /**
     *
     * Adds the channel to the player's
     * active channels.
     *
     * @param channel Adding Channel.
     */
    public void addActiveChannel(ChatManager.Channel channel) {

        if (this.activeChannels.contains(channel)) return;

        this.activeChannels.add(channel);

    }

    /**
     *
     * Removes the channel from the player's
     * active channels.
     *
     * @param channel Removing Channel.
     */
    public void removeActiveChannel(ChatManager.Channel channel) {

        if (!this.activeChannels.contains(channel)) return;

        this.activeChannels.remove(channel);

    }

    /**
     *
     * Returns all the player's active channels.
     *
     * @return Active Channels List.
     */
    public List<ChatManager.Channel> getActiveChannels() {

        return this.activeChannels;

    }

}
