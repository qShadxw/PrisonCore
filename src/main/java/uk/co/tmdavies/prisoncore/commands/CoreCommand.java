package uk.co.tmdavies.prisoncore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.listeners.ChatListener;
import uk.co.tmdavies.prisoncore.listeners.InteractListener;
import uk.co.tmdavies.prisoncore.listeners.JoinListener;
import uk.co.tmdavies.prisoncore.objects.Profile;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class CoreCommand implements CommandExecutor {

    public CoreCommand(PrisonCore plugin) {

        Objects.requireNonNull(plugin.getCommand("core")).setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String string, String[] args) {

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

            InteractListener.enabledBlocksValues.clear();
            InteractListener.enabledBlocks.clear();

            ChatListener.chatFormat = plugin.getConfig().getString("chat-format");
            JoinListener.joinFormat = plugin.getConfig().getString("join-format");
            JoinListener.quitFormat = plugin.getConfig().getString("quit-format");
            InteractListener.enabledBlocks.addAll(Arrays.asList(Objects.requireNonNull(
                    plugin.getConfig().getString("absorb-able-blocks")).split(";")));
            InteractListener.enabledBlocksValues.addAll(Arrays.asList(Objects.requireNonNull(
                    plugin.getConfig().getString("absorb-able-blocks-values")).split(";")));
            InteractListener.worldName = plugin.getConfig().getString("mines-world-name");

            JoinListener.baItem = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(
                    Objects.requireNonNull(plugin.getConfig().getString("ba-item")).toUpperCase()))));

            ItemMeta im = JoinListener.baItem.getItemMeta();
            assert im != null;
            im.setDisplayName(Utils.Colour(plugin.getConfig().getString("ba-item-name")));
            im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            JoinListener.baItem.setItemMeta(im);

            sender.sendMessage(Utils.Chat("&aSuccessfully reloaded config."));

        }

        if (args[0].equalsIgnoreCase("debug")) {

            if (!(sender instanceof Player)) return true;

            //Bukkit.getServer().broadcastMessage(PrisonCore.playerProfiles.get((Player) sender).getAbsorbedBlocks().toString());

            Player player = (Player) sender;
            Profile profile = PrisonCore.playerProfiles.get(player);

            player.sendMessage(profile.getCurrentEnchantments().toString());

        }

        return true;

    }

}
