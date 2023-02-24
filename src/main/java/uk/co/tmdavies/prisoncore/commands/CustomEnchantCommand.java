package uk.co.tmdavies.prisoncore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Profile;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.Objects;

public class CustomEnchantCommand implements CommandExecutor {

    public CustomEnchantCommand(PrisonCore plugin) {

        Objects.requireNonNull(plugin.getCommand("ce")).setExecutor(this);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        switch (args[0]) {
            case "add":
                if (!sender.hasPermission("prisoncore.enchants.add")) { return false; }
                if (args.length != 3) {
                    sender.sendMessage(Utils.Colour("&cUsage: /ce add <Enchantment> <Level>"));
                    return false;
                }
                Player player = (Player) sender;
                Profile profile = PrisonCore.playerProfiles.get(player);
                Enchantment enchantment = null;

                for (Enchantment enchant : Enchantment.values()) {

                    if (enchant.getKey().getKey().equalsIgnoreCase(args[0])) enchantment = enchant;

                }

                if (enchantment == null) {

                    player.sendMessage(Utils.Colour("&8[&9CustomEnchants&8] &cIncorrect Enchantment."));

                    return false;

                }

                profile.addEnchantment(enchantment, Integer.parseInt(args[1]));

                player.sendMessage(Utils.Colour("&8[&9CustomEnchants&8] &aSuccessfully added enchant. " + enchantment.getKey().getKey() + ": " + args[1] + "."));

                return true;
        }
        return false;
    }
}
