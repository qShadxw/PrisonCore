package uk.co.tmdavies.prisoncore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Profile;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomEnchantCommand implements CommandExecutor, TabCompleter {

    Plugin plugin;
    public static String incorrectEnchantmentMessage;
    public static String addEnchantmentMessage;
    public static String clearEnchantmentMessage;

    public CustomEnchantCommand(PrisonCore plugin) {

        this.plugin = plugin;
        incorrectEnchantmentMessage = plugin.getConfig().getString("enchants.incorrect-enchant");
        addEnchantmentMessage = plugin.getConfig().getString("enchants.add-enchant");
        clearEnchantmentMessage = plugin.getConfig().getString("enchants.clear-enchant");
        Objects.requireNonNull(plugin.getCommand("ce")).setExecutor(this);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        Profile profile = PrisonCore.playerProfiles.get(player);

        switch (args[0].toLowerCase()) {
            case "add":
                if (!sender.hasPermission("prisoncore.enchants.add")) { return false; }
                if (args.length != 3) {
                    sender.sendMessage(Utils.Colour("&cUsage: /ce add <Enchantment> <Level>"));
                    return false;
                }

                Enchantment enchantment = null;

                for (Enchantment enchant : Enchantment.values()) {

                    if (enchant.getKey().getKey().equalsIgnoreCase(args[1])) enchantment = enchant;

                }

                if (enchantment == null) {

                    player.sendMessage(Utils.Colour(incorrectEnchantmentMessage));

                    return false;

                }

                profile.addEnchantment(enchantment, Integer.parseInt(args[2]));

                player.sendMessage(Utils.Colour(Objects.requireNonNull(addEnchantmentMessage)
                        .replaceAll("%enchantment%", enchantment.getKey().getKey()))
                        .replaceAll("%amount%", args[2]));

                return true;

            case "clear":

                profile.clearEnchantments();

                player.sendMessage(Utils.Colour(clearEnchantmentMessage));

                return true;

        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String string, @NotNull String[] args) {

        if (args.length == 1) return List.of("add", "clear");
        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("add")) {

                List<String> toReturn = new ArrayList<>();

                for (Enchantment enchantment : Enchantment.values()) toReturn.add(enchantment.getKey().getKey());

                return toReturn;

            }

        }
        if (args.length == 3 && args[0].equalsIgnoreCase("add")) {

            List<String> toReturn = new ArrayList<>();

            for (int i = 0; i < 101; i++) toReturn.add(String.valueOf(i));

            return toReturn;

        }

        return new ArrayList<>();
    }
}
