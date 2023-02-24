package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Profile;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.util.*;

public class InteractListener implements Listener {

    public static ArrayList<String> enabledBlocks = new ArrayList<>();
    public static ArrayList<String> enabledBlocksValues = new ArrayList<>();
    public static HashMap<Material, Double> blockValues = new HashMap<Material, Double>();
    public static String worldName;
    private final String baItem;
    public static Inventory absorbInventory;

    public InteractListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

        enabledBlocks.addAll(Arrays.asList(Objects.requireNonNull(plugin.getConfig().getString("absorb-able-blocks")).split(";")));
        enabledBlocksValues.addAll(Arrays.asList(Objects.requireNonNull(plugin.getConfig().getString("absorb-able-blocks-values")).split(";")));
        for (int x = 0; x < enabledBlocks.size(); x++) {
            blockValues.put(Material.getMaterial(enabledBlocks.get(x).toUpperCase()), Double.valueOf(enabledBlocksValues.get(x)));
        }
        worldName = plugin.getConfig().getString("mines-world-name");
        baItem = Objects.requireNonNull(plugin.getConfig().getString("ba-item")).toUpperCase();

    }

    @EventHandler
    public void onBlockClickEvent(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.LEFT_CLICK_BLOCK)) { return; }
        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.getMaterial(Objects.requireNonNull(baItem)))) { return; }
        if (event.getClickedBlock() == null) { return; }
        if (event.getClickedBlock().getType() == Material.AIR) { return; }
        Material blocktype = Objects.requireNonNull(event.getClickedBlock()).getType();
        if (!enabledBlocks.contains(blocktype.toString().toLowerCase())) { return; }
        if (!event.getClickedBlock().getWorld().getName().equals(worldName)) { return; }
        Profile profile = PrisonCore.playerProfiles.get(event.getPlayer());
        profile.addAbsorbedBlock(blocktype, 1);
        event.getClickedBlock().setType(Material.AIR);
        event.setCancelled(true);
    }

    @EventHandler
    public void onRightClickEvent(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.getMaterial(Objects.requireNonNull(baItem)))) { return; }
        ItemStack i = event.getPlayer().getInventory().getItemInMainHand();
        if (!i.equals(JoinListener.baItem)) { return; }
        absorbInventory = Bukkit.createInventory(null, 27, Utils.Colour(Objects.requireNonNull(JoinListener.baItem.getItemMeta()).getDisplayName()));
        Profile pro = PrisonCore.playerProfiles.get(event.getPlayer());
        for (Material material : pro.getAbsorbedBlocks().keySet()) {
            ItemStack item = new ItemStack(material);
            ItemMeta itemM = item.getItemMeta();
            assert itemM != null;
            itemM.setDisplayName(Utils.Colour("&7&l" + item.getType().toString().toUpperCase()));
            itemM.setLore(Collections.singletonList(Utils.Colour("&a&lx" + pro.getAbsorbedBlocks().get(material))));
            item.setItemMeta(itemM);
            absorbInventory.addItem(item);
        }
        event.getPlayer().openInventory(absorbInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Add the ability to stop players taking items out of inventories.
    }

}
