package uk.co.tmdavies.prisoncore.objects;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
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

        if (this.gangBoss == gangMember) return false;
        if (this.gangUnderbosses.contains(gangBoss) || this.gangMembers.contains(gangBoss)) return false;

        return this.gangMembers.add(gangMember);

    }

    // Functions
    public boolean invitePlayer(Player player) {

        if (!player.isOnline()) return false;

        this.inviteCache.put(player, this);

        ComponentBuilder componentBuilder = new ComponentBuilder();

        componentBuilder.append(Utils.Colour(this.gangBoss.getDisplayName() + " &7has invited you to &9" + this.gangName + "."));
        componentBuilder.append(Utils.Colour("&9&lClick Here to accept.")).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gang accept " + this.gangId.toString()));

        player.spigot().sendMessage(componentBuilder.create());

        return true;

    }

}
