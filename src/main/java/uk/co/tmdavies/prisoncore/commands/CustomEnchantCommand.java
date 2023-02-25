package uk.co.tmdavies.prisoncore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Profile;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.Objects;

public class CustomEnchantCommand implements CommandExecutor {

    Plugin plugin;

    public CustomEnchantCommand(PrisonCore plugin) {

        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("ce")).setExecutor(this);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        Profile profile = PrisonCore.playerProfiles.get(player);

        switch (args[0]) {
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

                    player.sendMessage(Utils.Colour(plugin.getConfig().getString("enchants.incorrect-enchant")));

                    return false;

                }

                profile.addEnchantment(enchantment, Integer.parseInt(args[2]));

                player.sendMessage(Utils.Colour(Objects.requireNonNull(plugin.getConfig().getString("enchants.add-enchant"))
                        .replaceAll("%enchantment%", enchantment.getKey().getKey()))
                        .replaceAll("%amount%", args[2]));

                return true;

            case "clear":

                profile.clearEnchantments();

                player.sendMessage(Utils.Colour(plugin.getConfig().getString("enchants.clear-enchant")));

                return true;

        }
        return false;
    }
}
