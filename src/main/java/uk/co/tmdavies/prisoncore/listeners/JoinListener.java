package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.managers.ChatManager;
import uk.co.tmdavies.prisoncore.objects.ChatMessage;
import uk.co.tmdavies.prisoncore.objects.Profile;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.List;
import java.util.Objects;

public class JoinListener implements Listener {

    public static String joinFormat;
    public static String quitFormat;


    public static ItemStack baItem = new ItemStack(Material.STONE);

    public JoinListener(PrisonCore plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        joinFormat = plugin.getConfig().getString("join-format");
        quitFormat = plugin.getConfig().getString("quit-format");
        baItem = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(plugin.getConfig().getString("ba-item")).toUpperCase())));

        ItemMeta im = baItem.getItemMeta();
        assert im != null;
        im.setDisplayName(Utils.Colour(plugin.getConfig().getString("ba-item-name")));
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        baItem.setItemMeta(im);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("prisoncore.join.vip")) {
            event.setJoinMessage("");
            return;
        }
        double dub = 10.00;
        PrisonCore.econ.depositPlayer(event.getPlayer(), dub);



        if (!event.getPlayer().getInventory().contains(baItem)) { event.getPlayer().getInventory().addItem(baItem); }
        event.setJoinMessage(ChatManager.formatMessagePlayer(new ChatMessage(event.getPlayer(), joinFormat)));
        Profile profile = new Profile(event.getPlayer());
        profile.updatePickaxe();
        PrisonCore.playerProfiles.put(event.getPlayer(), profile);
    }


    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        if (!event.getPlayer().hasPermission("prisoncore.join.vip")) {
            event.setQuitMessage("");
            return;
        }
        event.setQuitMessage(ChatManager.formatMessagePlayer(new ChatMessage(event.getPlayer(), quitFormat)));
        PrisonCore.playerProfiles.get(event.getPlayer()).saveData();
        PrisonCore.playerProfiles.remove(event.getPlayer());
    }
}
