package uk.co.tmdavies.prisoncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Config;
import uk.co.tmdavies.prisoncore.objects.Gang;

import java.util.Map;
import java.util.Objects;
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

            if (Objects.equals(gang.getGangId().toString(), uuid.toString())) return gang;

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

            if ((Gang) entry.getValue() == gang) PrisonCore.gangCache.remove((OfflinePlayer) entry.getKey());

        }

    }

    public static void loadGangIfNotExists(Player player) {

        if (getGangByMember(player) != null) return;

        Config config = PrisonCore.gangConfig;
        ConfigurationSection section = config.getYML().getConfigurationSection("Gangs");
        String gangPath = null;

        if (section == null) return;

        for (Map.Entry entry : section.getValues(false).entrySet()) {

            MemorySection memorySection = (MemorySection) entry.getValue();
            String path = memorySection.getCurrentPath();

            if (config.getString(path + ".Boss").equals(player.getUniqueId().toString())) gangPath = path;
            if (config.getStringList(path + ".Underbosses").contains(player.getUniqueId().toString())) gangPath = path;
            if (config.getStringList(path + ".Members").contains(player.getUniqueId().toString())) gangPath = path;

            if (gangPath != null) break;

        }

        if (gangPath == null) return;

        Gang gang = new Gang(UUID.fromString(gangPath.split("\\.")[1]));

        PrisonCore.gangCache.put(Bukkit.getOfflinePlayer(gang.getGangBoss()), gang);

    }

}
