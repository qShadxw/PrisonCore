package uk.co.tmdavies.prisoncore.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Gang;
import uk.co.tmdavies.prisoncore.utils.Utils;

import javax.print.attribute.ResolutionSyntax;
import java.util.List;
import java.util.Objects;

public class GangCommand implements CommandExecutor, TabCompleter {

    public GangCommand(PrisonCore plugin) {

        Objects.requireNonNull(plugin.getCommand("gang")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("gang")).setTabCompleter(this);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String string, @NotNull String[] args) {

        if (!sender.hasPermission("prisoncore.gang")) {

            sender.sendMessage(Utils.Chat("&cYou do not have permission to execute this command."));

            return true;

        }

        boolean isConsole = !(sender instanceof Player);

        // /gang <create|disband|invite|kick|promote|accept> <gangName|player> <rank>

        switch (args[0].toLowerCase()) {

            case "create":
                if (isConsole) {

                    sender.sendMessage(Utils.Chat("&cOnly players may execute this command."));
                    break;

                }
                String gangName = ChatColor.stripColor(args[1]);
                PrisonCore.gangCache.put((OfflinePlayer) sender, new Gang(gangName, (Player) sender));
                sender.sendMessage(Utils.Chat("&aSuccessfully created gang: " + gangName));
                break;

            case "disband":
            case "invite":
            case "kick":
            case "promote":

            default:
                sender.sendMessage(getSyntax());
                break;

        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String string, @NotNull String[] args) {
        return null;
    }

    public String getSyntax() {

        return Utils.Chat("&cUsage: /gang <create|disband|invite|kick|promote> <gangName|player> <rank>");

    }

}
