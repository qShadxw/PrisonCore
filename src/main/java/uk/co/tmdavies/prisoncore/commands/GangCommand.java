package uk.co.tmdavies.prisoncore.commands;

import org.bukkit.Bukkit;
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
import uk.co.tmdavies.prisoncore.utils.GangUtils;
import uk.co.tmdavies.prisoncore.utils.Utils;

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
        Player player;
        Player target;
        Gang gang;

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
                // /gang disband name
                if (args.length == 2) {

                    if (!sender.hasPermission("prisoncore.gang.admin")) {

                        sender.sendMessage(Utils.Chat("&cYou do not have permission to execute this command."));
                        break;

                    }

                    gang = GangUtils.getGangByName(args[1]);

                    if (gang == null) {

                        sender.sendMessage(Utils.Chat("&cInvalid Gang."));
                        break;

                    }

                    gang.disband();

                    GangUtils.removeGang(gang);

                    sender.sendMessage(Utils.Chat("&aGang has been disbanded."));

                    break;

                }
                // /gang disband
                if (isConsole) {

                    sender.sendMessage(Utils.Chat("&cOnly players may execute this command."));
                    break;

                }

                player = (Player) sender;
                gang = GangUtils.getGangByMember(player);

                if (gang == null) {

                    player.sendMessage(Utils.Chat("&cYou are not part of a gang."));
                    break;

                }

                gang.disband();

                GangUtils.removeGang(gang);

                break;

            case "invite":

                if (isConsole) {

                    sender.sendMessage(Utils.Chat("&cOnly players may execute this command."));
                    break;

                }

                if (args.length != 2) {

                    sender.sendMessage(getSyntax());
                    break;

                }

                player = (Player) sender;
                gang = GangUtils.getGangByMember(player);

                if (gang == null) {

                    player.sendMessage(Utils.Chat("&cYou are not part of a gang."));
                    break;

                }

                if (!gang.hasHighPermissions(player)) {

                    player.sendMessage(Utils.Chat("&cYou do not have the permission to invite players to your gang."));
                    break;

                }

                target = Bukkit.getPlayer(args[1]);

                if (target == null || !target.isOnline()) {

                    player.sendMessage(Utils.Chat("&c" + args[1] + " is offline."));
                    break;

                }

                gang.invitePlayer(target);

                player.sendMessage(Utils.Chat("&aSuccessfully invited " + args[1]));

                break;

            case "kick":

                if (isConsole) {

                    sender.sendMessage(Utils.Chat("&cOnly players may execute this command."));
                    break;

                }

                if (args.length != 2) {

                    sender.sendMessage(getSyntax());
                    break;

                }

                player = (Player) sender;
                gang = GangUtils.getGangByMember(player);

                if (gang == null) {

                    player.sendMessage(Utils.Chat("&cYou are not part of a gang."));
                    break;

                }

                if (!gang.hasHighPermissions(player)) {

                    player.sendMessage(Utils.Chat("&cYou do not have the permission to invite players to your gang."));
                    break;

                }

                target = Bukkit.getPlayer(args[1]);

                if (target == null || !target.isOnline()) {

                    player.sendMessage(Utils.Chat("&c" + args[1] + " is offline."));
                    break;

                }

                if (!gang.hasMember(target)) {

                    player.sendMessage(Utils.Chat("&c" + args[1] + " is not in the gang."));
                    break;

                }

                gang.removeGangMember(target);

                gang.sendGangMessage("&a" + args[1] + " has been removed from " + gang.getGangName());

                break;

            case "promote":

                if (isConsole) {

                    sender.sendMessage(Utils.Chat("&cOnly players may execute this command."));
                    break;

                }

                if (args.length != 2) {

                    sender.sendMessage(getSyntax());
                    break;

                }

                player = (Player) sender;
                gang = GangUtils.getGangByMember(player);

                if (gang == null) {

                    player.sendMessage(Utils.Chat("&cYou are not part of a gang."));
                    break;

                }

                if (!gang.hasHighPermissions(player)) {

                    player.sendMessage(Utils.Chat("&cYou do not have the permission to invite players to your gang."));
                    break;

                }

                target = Bukkit.getPlayer(args[1]);

                if (target == null || !target.isOnline()) {

                    player.sendMessage(Utils.Chat("&c" + args[1] + " is offline."));
                    break;

                }

                if (!gang.hasMember(target)) {

                    player.sendMessage(Utils.Chat("&c" + args[1] + " is not in the gang."));
                    break;

                }

                gang.promoteMember(target);



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
