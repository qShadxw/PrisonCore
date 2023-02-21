package uk.co.tmdavies.prisoncore.objects;

import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.managers.ChatManager;

public class ChatMessage {

    private final Player sender;
    private final String rawMessage;
    private String formattedMessage;

    public ChatMessage(Player sender, String rawMessage) {

        this.sender = sender;
        this.rawMessage = rawMessage;
        this.formattedMessage = ChatManager.formatMessagePlayer(this);

    }

    public Player getSender() {

        return this.sender;

    }

    public String getRawMessage() {

        return this.rawMessage;

    }

    public Profile getProfile() {

        return PrisonCore.playerProfiles.get(this.sender);

    }

    public String getFormattedMessage() {

        return this.formattedMessage;

    }

    public void setFormattedMessage(String formattedMessage) {

        this.formattedMessage = formattedMessage;

    }

}
