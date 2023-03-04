package uk.co.tmdavies.prisoncore.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.logging.Level;

public class Logger {

    private java.util.logging.Logger logger;

    public Logger() {

        init();

    }

    public void init() {

        if (this.logger == null) {

            this.logger = new java.util.logging.Logger("PrisonCore", null) {
            };

            this.logger.setParent(Bukkit.getLogger());
            this.logger.setLevel(Level.ALL);

        }

    }

    public void log(Reason reason, String message) {

        this.logger.info(Utils.Colour(reason.getColour() + reason.getPrefix() + " " + message));

    }

    public void error(Reason reason, String message) {

        this.logger.info(Reason.ERROR.getColour() + Reason.ERROR.getPrefix() + reason.getPrefix() + Utils.Colour(" " + message));

    }

    public void startUp() {

        log(Reason.GENERIC, "");
        log(Reason.GENERIC, "PrisonCore");
        log(Reason.GENERIC, "Carbonate | DoomFly3215");
        log(Reason.GENERIC, "");

    }

    public enum Reason {

        GENERIC("", ChatColor.WHITE),
        ERROR("[Error] ", ChatColor.RED),
        CONFIG("[Config] ", ChatColor.BLUE),
        SQL("[SQL] ", ChatColor.GOLD),
        KEY("[Key] ", ChatColor.DARK_PURPLE),
        API("[API] ", ChatColor.GREEN),
        ECONOMY("[Eco] ", ChatColor.AQUA);

        private String prefix;
        private ChatColor colour;

        Reason(String prefix, ChatColor colour) {

            this.prefix = prefix;
            this.colour = colour;

        }

        public String getPrefix() {

            return this.prefix;

        }

        public ChatColor getColour() {

            return this.colour;

        }

    }

}
