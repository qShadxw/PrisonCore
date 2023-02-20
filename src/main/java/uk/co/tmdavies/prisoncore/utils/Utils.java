package uk.co.tmdavies.prisoncore.utils;

import org.bukkit.ChatColor;

public class Utils {

    public static String Colour(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

    public static String Chat(String message) {

        return ChatColor.translateAlternateColorCodes('&', "&8[&9Core&8] &7" + message);

    }

}
