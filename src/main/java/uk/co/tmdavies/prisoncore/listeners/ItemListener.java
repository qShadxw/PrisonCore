package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.listeners.custom.PlayerAcquiredItemEvent;
import uk.co.tmdavies.prisoncore.objects.Profile;

import java.util.Map;
import java.util.Objects;

public class ItemListener implements Listener {

    public ItemListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        try {

            switch (event.getItem().getItemStack().getType()) {

                case WOODEN_PICKAXE:
                case STONE_PICKAXE:
                case IRON_PICKAXE:
                case GOLDEN_PICKAXE:
                case DIAMOND_PICKAXE:
                case NETHERITE_PICKAXE:
                    Bukkit.getPluginManager().callEvent(new PlayerAcquiredItemEvent((Player) event.getEntity(), event.getItem().getItemStack()));

            }

        } catch (NullPointerException ignored) {}

    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {

        if (!(event.getInitiator().getHolder() instanceof Player)) return;

        try {

            switch (Objects.requireNonNull(event.getItem().getData()).getItemType()) {

                case WOODEN_PICKAXE:
                case STONE_PICKAXE:
                case IRON_PICKAXE:
                case GOLDEN_PICKAXE:
                case DIAMOND_PICKAXE:
                case NETHERITE_PICKAXE:
                    Bukkit.getPluginManager().callEvent(new PlayerAcquiredItemEvent((Player) event.getInitiator().getHolder(), event.getItem()));

            }

        } catch (NullPointerException ignored) {}

    }

    @EventHandler
    public void onItemAcquired(PlayerAcquiredItemEvent event) {

        // Set Enchantments
        Profile profile = PrisonCore.playerProfiles.get(event.getPlayer());
        ItemStack pickaxe = event.getItem();
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();

        if (pickaxeMeta == null) pickaxeMeta = Bukkit.getItemFactory().getItemMeta(pickaxe.getType());

        for (Map.Entry entry : profile.getCurrentEnchantments().entrySet()) {

            pickaxeMeta.addEnchant((Enchantment) entry.getKey(), (int) entry.getKey(), true);

        }

        pickaxe.setItemMeta(pickaxeMeta);

    }

}
