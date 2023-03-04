package uk.co.tmdavies.prisoncore.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Gang;

import java.util.Map;
import java.util.UUID;

public class GangUtils {

    /**
     *
     * Grabs the Gang the player specified
     * is in.
     *
     * @param player Player
     * @return Gang | Null
     */
    public static Gang getGangByMember(Player player) {

        for (Gang gang : PrisonCore.gangCache.values()) {

            if (gang.hasMember(player)) return gang;

        }

        return null;

    }

    /**
     *
     * Grabs the Gang by name.
     *
     * @param name String
     * @return Gang | Null
     */
    public static Gang getGangByName(String name) {

        for (Gang gang : PrisonCore.gangCache.values()) {

            if (gang.getGangName().equalsIgnoreCase(name)) return gang;

        }

        return null;

    }

    /**
     *
     * Grabs the Gang by UUID.
     *
     * @param uuid UUID
     * @return Gang | Null
     */
    public static Gang getGangById(UUID uuid) {

        for (Gang gang : PrisonCore.gangCache.values()) {

            if (gang.getGangId() == uuid) return gang;

        }

        return null;

    }

    /**
     *
     * Removes Gang from Cache
     *
     */
    public static void removeGang(Gang gang) {

        for (Map.Entry entry : PrisonCore.gangCache.entrySet()) {

            if ((Gang) entry.getValue() == gang) PrisonCore.gangCache.remove((OfflinePlayer) entry.getValue());

        }

    }

}
