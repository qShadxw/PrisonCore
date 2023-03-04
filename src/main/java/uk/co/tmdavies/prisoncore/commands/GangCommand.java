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
import uk.co.tmdavies.prisoncore.objects.Logger;
import uk.co.tmdavies.prisoncore.utils.GangUtils;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

        if (args.length == 0) {

            sender.sendMessage(getSyntax());
            return true;

        }

        boolean isConsole = !(sender instanceof Player);
        Player player;
        Player target;
        Gang gang;

        // /gang <create|disband|invite|kick|promote|accept> <gangName|player> <rank>

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (isConsole) {

                    sender.sendMessage(Utils.Chat("&cOnly players may execute this command."));
                    break;

                }
                String gangName = ChatColor.stripColor(args[1]);
                PrisonCore.gangCache.put((OfflinePlayer) sender, new Gang(gangName, (Player) sender));
                sender.sendMessage(Utils.Chat("&aSuccessfully created gang: " + gangName));
            }
            case "disband" -> {
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
            }
            case "status" -> {
                if (isConsole) {

                    sender.sendMessage(Utils.Chat("&cOnly players may execute this command."));
                    break;

                }
                player = (Player) sender;
                gang = GangUtils.getGangByMember(player);

                if (gang == null) {

                    sender.sendMessage(Utils.Chat("&cYou are not in a gang."));
                    break;

                }

                OfflinePlayer temp = Bukkit.getOfflinePlayer(gang.getGangBoss());

                String boss = temp.isOnline() ? temp.getPlayer().getDisplayName() : temp.getName();

                StringBuilder underbosses = new StringBuilder();
                gang.getGangUnderbosses().forEach(uuid -> {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    underbosses.append(offlinePlayer.isOnline() ? offlinePlayer.getPlayer().getDisplayName() : offlinePlayer.getName()).append(", ");
                });

                StringBuilder members = new StringBuilder();
                gang.getGangMembers().forEach(uuid -> {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    members.append(offlinePlayer.isOnline() ? offlinePlayer.getPlayer().getDisplayName() : offlinePlayer.getName()).append(", ");
                });

                player.sendMessage(Utils.Colour("&8+--------+ &9" + gang.getGangName() + " &8+--------+"));
                player.sendMessage(Utils.Colour("&7Gang Boss: " + boss));
                player.sendMessage(Utils.Colour("&7Gang Under Bosses: " + (underbosses.length() > 0 ? (underbosses.substring(0, underbosses.length() - 2)) : "")));
                player.sendMessage(Utils.Colour("&7Gang Members: " + (members.length() > 0 ? (members.substring(0, members.length() - 2)) : "")));

            }
            case "invite" -> {
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
                if (gang.hasMember(target)) {

                    player.sendMessage(Utils.Chat("&cPlayer is already in " + gang.getGangName() + "."));
                    break;

                }
                gang.invitePlayer(target);
                player.sendMessage(Utils.Chat("&aSuccessfully invited " + args[1]));
            }
            case "kick" -> {
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
            }
            case "promote" -> {
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
                gang.sendGangMessage("&a" + args[1] + " has been promoted in " + gang.getGangName() + ".");
            }
            case "accept" -> {
                // Not usually available to normal use (only use on invite click message)
                if (isConsole) {

                    sender.sendMessage(Utils.Chat("&cOnly players may execute this command."));
                    break;

                }
                if (args.length != 2) {

                    PrisonCore.logger.error(Logger.Reason.GENERIC, "Args");
                    sender.sendMessage(getSyntax());
                    break;

                }
                UUID gangId;
                try {

                    gangId = UUID.fromString(args[1]);

                } catch (IllegalArgumentException exception) {

                    // Disguise Command.
                    sender.sendMessage(getSyntax());
                    PrisonCore.logger.error(Logger.Reason.GENERIC, exception.toString());
                    break;

                }
                player = (Player) sender;
                gang = GangUtils.getGangById(gangId);
                if (gang == null) {

                    PrisonCore.logger.error(Logger.Reason.GENERIC, "Gang Null");
                    player.sendMessage(getSyntax());
                    break;

                }
                if (!gang.getInvites().asMap().containsKey(player)) {

                    // Disguise Command.
                    PrisonCore.logger.error(Logger.Reason.GENERIC, "Cache asMap");
                    sender.sendMessage(getSyntax());
                    break;

                }
                gang.addGangMember(player);
                gang.sendGangMessage("&a" + player.getDisplayName() + " &ahas joined " + gang.getGangName());
            }
            default -> sender.sendMessage(getSyntax());
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String string, @NotNull String[] args) {
        switch (args.length) {
            case 1 -> {
                return List.of("create", "disband", "invite", "kick", "promote", "status");
            }
            case 2 -> {
                List<String> toReturn = new ArrayList<>();
                switch (args[0].toLowerCase()) {
                    case "invite" -> {
                        Bukkit.getOnlinePlayers().forEach(player -> toReturn.add(player.getName()));
                        return toReturn;
                    }
                    case "kick", "promote" -> {
                        if (!(sender instanceof Player player)) return new ArrayList<>();

                        Gang gang = GangUtils.getGangByMember(player);

                        if (gang == null) return new ArrayList<>();

                        gang.getGangMembers().forEach(member -> toReturn.add(Objects.requireNonNull(Bukkit.getPlayer(member)).getName()));
                        gang.getGangUnderbosses().forEach(underboss -> toReturn.add(Objects.requireNonNull(Bukkit.getPlayer(underboss)).getName()));

                        return toReturn;
                    }
                    default -> {
                        return new ArrayList<>();
                    }
                }
            }
            default -> {
                return new ArrayList<>();
            }
        }

    }

    public String getSyntax() {

        return Utils.Chat("&cUsage: /gang <create|disband|invite|kick|promote> <gangName|player>");

    }

}
