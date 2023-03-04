package uk.co.tmdavies.prisoncore.objects;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Gang {

    private final UUID gangId;
    private String gangName;
    private Player gangBoss;
    private List<Player> gangUnderbosses;
    private List<Player> gangMembers;
    private Cache<Player, Gang> inviteCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(5)
            .build();

    public Gang(UUID gangId) {

        // Load from Config
        this.gangId = gangId;

    }

    public Gang(String name, Player gangBoss) {

        this.gangId = UUID.randomUUID();
        this.gangName = name;
        this.gangBoss = gangBoss;
        this.gangUnderbosses = new ArrayList<>();
        this.gangMembers = new ArrayList<>();

    }

    // Getters

    /**
     *
     * Gets Gang ID.
     *
     * @return UUID
     */
    public UUID getGangId() {

        return this.gangId;

    }

    /**
     *
     * Gets the Gang name.
     *
     * @return String
     */
    public String getGangName() {

        return this.gangName;

    }

    /**
     *
     * Gets the Gang Boss.
     *
     * @return Player
     */
    public Player getGangBoss() {

        return this.gangBoss;

    }

    /**
     *
     * Get Gang Underbosses.
     *
     * @return List < Player >
     */
    public List<Player> getGangUnderbosses() {

        return this.gangUnderbosses;

    }

    /**
     *
     * Get Gang Members.
     *
     * @return List < Player >
     */
    public List<Player> getGangMembers() {

        return this.gangMembers;

    }

    /**
     *
     * Get Gang Invites.
     *
     * @return Cache < Player, Gang >
     */
    public Cache<Player, Gang> getInvites() {

        return this.inviteCache;

    }

    // Setters

    /**
     *
     * Sets the Gang name.
     *
     * @param gangName String
     */
    public void setGangName(String gangName) {

        this.gangName = gangName;

    }

    /**
     *
     * Sets the Gang Boss and putting the old
     * boss inside the members.
     *
     * @param gangBoss Player
     * @return Boolean
     */
    public boolean setGangBoss(Player gangBoss) {

        if (this.gangBoss == gangBoss) return false;
        if (!this.gangUnderbosses.contains(gangBoss) || !this.gangMembers.contains(gangBoss)) return false;

        this.gangMembers.add(this.gangBoss);
        this.gangBoss = gangBoss;

        return true;

    }

    // Adders

    /**
     *
     * Adds a Moderator (Underboss) to the Gang.
     *
     * @param underBoss Player
     * @return Boolean
     */
    public boolean addGangUnderboss(Player underBoss) {

        if (this.gangBoss == underBoss) return false;
        if (this.gangUnderbosses.contains(gangBoss) || !this.gangMembers.contains(gangBoss)) return false;

        return this.gangUnderbosses.add(underBoss);

    }

    /**
     *
     * Adds a player to the Gang.
     *
     * @param gangMember Player
     * @return Boolean
     */
    public boolean addGangMember(Player gangMember) {

        if (hasMember(gangMember)) return false;

        return this.gangMembers.add(gangMember);

    }

    // Removers

    /**
     *
     * Removes a Moderator (Underboss) from the gang.
     *
     * @param underBoss Player
     * @return Boolean
     */
    public boolean removeGangUnderboss(Player underBoss) {

        if (!hasMember(underBoss)) return false;

        return this.gangUnderbosses.remove(underBoss);

    }

    /**
     *
     * Removes a player from the gang.
     *
     * @param gangMember Player
     * @return Boolean
     */
    public boolean removeGangMember(Player gangMember) {

        if (!hasMember(gangMember) || !this.gangMembers.contains(gangMember)) return false;

        return this.gangMembers.remove(gangBoss);

    }

    // Functions

    /**
     *
     * Invites the player and stores in a cache,
     * if 30 seconds is up then the invite gets removed.
     *
     * @param player Player
     * @return Boolean
     */
    public boolean invitePlayer(Player player) {

        if (hasMember(player)) return false;
        if (!player.isOnline()) return false;

        this.inviteCache.put(player, this);

        ComponentBuilder componentBuilder = new ComponentBuilder();

        componentBuilder.append(Utils.Colour(this.gangBoss.getDisplayName() + " &7has invited you to &9" + this.gangName + "."));
        componentBuilder.append(Utils.Colour("&9&lClick Here to accept.")).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gang accept " + this.gangId.toString()));

        player.spigot().sendMessage(componentBuilder.create());

        return true;

    }

    /**
     *
     * Sends a message to all the Gang.
     *
     * @param message String.
     */
    public void sendGangMessage(String message) {

        if (!this.gangMembers.isEmpty())
            this.gangMembers.forEach(player -> {
                if (player.isOnline()) player.sendMessage(Utils.Chat("&8[&r" + this.gangName + "&8] " + message));
            });

        if (!this.gangUnderbosses.isEmpty())
            this.gangUnderbosses.forEach(player -> {
                if (player.isOnline()) player.sendMessage(Utils.Chat("&8[&r" + this.gangName + "&8] " + message));
            });

        if (this.gangBoss.isOnline())
            this.gangBoss.sendMessage(Utils.Chat("&8[&r" + this.gangName + "&8] " + message));

    }

    /**
     *
     * Returns true if a member is online.
     *
     * @return Boolean
     */
    public boolean isAnyoneOnline() {

        boolean memberOnline = false;

        List<Player> members = new ArrayList<>();

        members.addAll(this.gangMembers);
        members.addAll(this.gangUnderbosses);
        members.add(this.gangBoss);

        for (Player player : members) {

            memberOnline = player.isOnline();

            if (memberOnline) break;

        }

        return memberOnline;

    }

    /**
     *
     * Checks if the player is high
     * enough in the gang to be allowed to
     * send invites.
     *
     * @param player Player
     * @return Boolean
     */
    public boolean hasHighPermissions(Player player) {

        return this.gangUnderbosses.contains(player) || this.gangBoss == player;

    }

    /**
     *
     * Checks if the given player is in the gang,
     * in any way.
     *
     * @param player Player
     * @return Boolean
     */
    public boolean hasMember(Player player) {

        if (this.gangBoss == player) return true;

        if (!this.gangUnderbosses.isEmpty())
            for (Player underboss : this.gangUnderbosses)
                if (underboss == player) return true;

        if (!this.gangMembers.isEmpty())
            for (Player member : this.gangMembers)
                if (member == player) return true;

        return false;

    }

    public boolean promoteMember(Player player) {

        if (!hasMember(player)) return false;
        if (this.gangBoss == player) return false;
        if (this.gangUnderbosses.contains(player) && !this.gangMembers.contains(player)) return false;

        this.gangMembers.remove(player);

        return this.gangUnderbosses.add(player);

    }

    public boolean demoteMember(Player player) {

        if (!hasMember(player)) return false;
        if (this.gangBoss == player) return false;
        if (!this.gangUnderbosses.contains(player) && this.gangMembers.contains(player)) return false;

        this.gangUnderbosses.remove(player);

        return this.gangMembers.add(player);

    }

    /**
     *
     * Saves the Gang to the config.
     *
     */
    public void save() {

        Config config = PrisonCore.gangConfig;

        config.add("Gangs." + this.gangId.toString() + ".Name", this.gangName);
        config.add("Gangs." + this.gangId.toString() + ".Leader", this.gangBoss.getUniqueId().toString());
        config.add("Gangs." + this.gangId.toString() + ".Underbosses", this.gangUnderbosses);
        config.add("Gangs." + this.gangId.toString() + ".Members", this.gangMembers);

        config.reload();

    }

    /**
     *
     * Deletes the Gang.
     *
     */
    public void disband() {

        sendGangMessage(Utils.Chat("&c" + this.gangName + " has been disbanded."));

        Config config = PrisonCore.gangConfig;

        config.set("Gangs." + this.gangId.toString(), null);

        config.reload();

    }

}
