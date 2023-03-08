package uk.co.tmdavies.prisoncore.objects;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Gang {

    private final UUID gangId;
    private String gangName;
    private UUID gangBoss;
    private final List<UUID> gangUnderbosses;
    private final List<UUID> gangMembers;
    private final Cache<Player, Gang> inviteCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(5)
            .build();

    public Gang(UUID gangId) {

        Config config = PrisonCore.gangConfig;
        this.gangUnderbosses = new ArrayList<>();
        this.gangMembers = new ArrayList<>();

        this.gangId = gangId;
        this.gangName = config.getString("Gangs." + this.gangId.toString() + ".Name");
        this.gangBoss = UUID.fromString(config.getString("Gangs." + this.gangId.toString() + ".Boss"));

        config.getStringList("Gangs." + this.gangId.toString() + ".Underbosses")
                .forEach(uuid -> this.gangUnderbosses.add(UUID.fromString(uuid)));

        config.getStringList("Gangs." + this.gangId.toString() + ".Members")
                .forEach(uuid -> this.gangMembers.add(UUID.fromString(uuid)));

    }

    public Gang(String name, Player gangBoss) {

        this.gangId = UUID.randomUUID();
        this.gangName = name;
        this.gangBoss = gangBoss.getUniqueId();
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
    public UUID getGangBoss() {

        return this.gangBoss;

    }

    /**
     *
     * Get Gang Underbosses.
     *
     * @return List < UUID >
     */
    public List<UUID> getGangUnderbosses() {

        return this.gangUnderbosses;

    }

    /**
     *
     * Get Gang Members.
     *
     * @return List < UUID >
     */
    public List<UUID> getGangMembers() {

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

        if (Objects.equals(this.gangBoss.toString(), gangBoss.getUniqueId().toString())) return false;
        if (!this.gangUnderbosses.contains(gangBoss.getUniqueId()) || !this.gangMembers.contains(gangBoss.getUniqueId())) return false;

        this.gangMembers.add(this.gangBoss);
        this.gangBoss = gangBoss.getUniqueId();

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

        if (Objects.equals(this.gangBoss.toString(), underBoss.getUniqueId().toString())) return false;
        if (this.gangUnderbosses.contains(underBoss.getUniqueId()) || !this.gangMembers.contains(underBoss.getUniqueId())) return false;

        return this.gangUnderbosses.add(underBoss.getUniqueId());

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

        return this.gangMembers.add(gangMember.getUniqueId());

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

        componentBuilder.append(Utils.Colour(Objects.requireNonNull(Bukkit.getPlayer(this.gangBoss)).getDisplayName()
                + " &7has invited you to &9" + this.gangName + "."));
        componentBuilder.append(Utils.Colour("&9&lClick Here to accept.")).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/gang accept " + this.gangId.toString()));

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
            this.gangMembers.forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) player.sendMessage(Utils.Chat("&8[&r" + this.gangName + "&8] " + message));
            });

        if (!this.gangUnderbosses.isEmpty())
            this.gangUnderbosses.forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) player.sendMessage(Utils.Chat("&8[&r" + this.gangName + "&8] " + message));
            });

        if (Objects.requireNonNull(Bukkit.getPlayer(this.gangBoss)).isOnline())
            Objects.requireNonNull(Bukkit.getPlayer(this.gangBoss)).sendMessage(Utils.Chat("&8[&r" + this.gangName + "&8] " + message));

    }

    /**
     *
     * Returns true if a member is online.
     *
     * @return Boolean
     */
    public boolean isAnyoneOnline() {

        boolean memberOnline = false;
        List<UUID> members = new ArrayList<>();

        members.addAll(this.gangMembers);
        members.addAll(this.gangUnderbosses);
        members.add(this.gangBoss);

        for (UUID player : members) {

            memberOnline = Objects.requireNonNull(Bukkit.getPlayer(player)).isOnline();

            if (memberOnline) break;

        }

        return memberOnline;

    }

    public String isLastPlayerOnline(Player player) {

        if (!hasMember(player)) return "not-in-gang";

        boolean memberOnline = false;
        List<UUID> members = new ArrayList<>();

        members.addAll(this.gangMembers);
        members.addAll(this.gangUnderbosses);
        members.add(this.gangBoss);

        for (UUID member : members) {

            if (member == player.getUniqueId()) continue;

            memberOnline = Objects.requireNonNull(Bukkit.getPlayer(member)).isOnline();

            if (memberOnline) break;

        }

        if (memberOnline) return "no";
        else return "yes";

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

        return this.gangUnderbosses.contains(player.getUniqueId()) || Objects.equals(this.gangBoss.toString(), player.getUniqueId().toString());

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

        if (Objects.equals(this.gangBoss.toString(), player.getUniqueId().toString())) return true;

        if (!this.gangUnderbosses.isEmpty())
            for (UUID underboss : this.gangUnderbosses)
                if (Objects.equals(underboss.toString(), player.getUniqueId().toString())) return true;

        if (!this.gangMembers.isEmpty())
            for (UUID member : this.gangMembers)
                if (Objects.equals(member.toString(), player.getUniqueId().toString())) return true;

        return false;

    }

    public boolean promoteMember(Player player) {

        if (!hasMember(player)) return false;
        if (this.gangUnderbosses.contains(player.getUniqueId()) && !this.gangMembers.contains(player.getUniqueId())) return false;

        this.gangMembers.remove(player.getUniqueId());

        return this.gangUnderbosses.add(player.getUniqueId());

    }

    public boolean demoteMember(Player player) {

        if (!hasMember(player)) return false;
        if (!this.gangUnderbosses.contains(player.getUniqueId()) && this.gangMembers.contains(player.getUniqueId())) return false;

        this.gangUnderbosses.remove(player.getUniqueId());

        return this.gangMembers.add(player.getUniqueId());

    }

    /**
     *
     * Saves the Gang to the config.
     *
     */
    public void save() {

        Config config = PrisonCore.gangConfig;

        config.set("Gangs." + this.gangId.toString() + ".Name", this.gangName);
        config.set("Gangs." + this.gangId.toString() + ".Boss", this.gangBoss.toString());

        List<String> uuids = new ArrayList<>();
        this.gangUnderbosses.forEach(uuid -> uuids.add(uuid.toString()));
        config.set("Gangs." + this.gangId.toString() + ".Underbosses", uuids);
        uuids.clear();

        this.gangMembers.forEach(uuid -> uuids.add(uuid.toString()));
        config.set("Gangs." + this.gangId.toString() + ".Members", uuids);
        uuids.clear();

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

    @Override
    public String toString() {

        String underbosses = null;
        String members = null;

        if (!this.gangUnderbosses.isEmpty()) {
            StringBuilder underbossesBuilder = new StringBuilder();
            this.gangUnderbosses.forEach(uuid -> underbossesBuilder.append(uuid.toString()).append(", "));
            underbosses = underbossesBuilder.substring(0, underbossesBuilder.length() - 2);
        }

        if (!this.gangMembers.isEmpty()) {
            StringBuilder membersBuilder = new StringBuilder();
            this.gangMembers.forEach(uuid -> membersBuilder.append(uuid.toString()).append(", "));
            members = membersBuilder.substring(0, membersBuilder.length() - 2);
        }

        return "Gang[gangId='" + gangId.toString() +
                "', gangName='" + gangName +
                "', gangBoss='" + this.gangBoss.toString() +
                "', gangUnderbosses='" + (underbosses != null ? underbosses : "") +
                "', gangMembers='" + (members != null ? members : "") +
                "']";

    }

}
