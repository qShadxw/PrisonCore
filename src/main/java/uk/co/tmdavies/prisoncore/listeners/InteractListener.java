package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class InteractListener implements Listener {

    public static ArrayList<String> enabledBlocks = new ArrayList<>();
    public static ArrayList<String> enabledBlocksValues = new ArrayList<>();
    public static HashMap<Material, Double> blockValues = new HashMap<Material, Double>();
    public static String worldName;
    private final String baItem;

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

}
