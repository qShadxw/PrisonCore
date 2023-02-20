package uk.co.tmdavies.prisoncore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.listeners.ChatListener;
import uk.co.tmdavies.prisoncore.listeners.JoinListener;
import uk.co.tmdavies.prisoncore.utils.Utils;

public class CoreCommand implements CommandExecutor {

    public CoreCommand(PrisonCore plugin) {

        plugin.getCommand("core").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        if (!sender.hasPermission("prisoncore.admin")) {

            sender.sendMessage(Utils.Chat("&cYou do not have permission to execute this command."));

            return false;

        }

        if (args.length != 1) {

            sender.sendMessage(Utils.Colour("&cUsage: /core reload"));

            return false;

        }

        if (args[0].equalsIgnoreCase("reload")) {

            PrisonCore plugin = JavaPlugin.getPlugin(PrisonCore.class);

            plugin.reloadConfig();

            ChatListener.chatFormat = plugin.getConfig().getString("chat-format");
            JoinListener.joinConfig = plugin.getConfig().getString("join-format");
            JoinListener.quitConfig = plugin.getConfig().getString("quit-format");

            sender.sendMessage(Utils.Chat("&aSuccessfully reloaded config."));

        }

        return true;

    }

}
