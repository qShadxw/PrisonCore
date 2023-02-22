package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.objects.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class InteractListener implements Listener {

    public static ArrayList<String> enabledBlocks = new ArrayList<>();
    public static String worldName;

    public InteractListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

        enabledBlocks.addAll(Arrays.asList(plugin.getConfig().getString("absorb-able-blocks").split(";")));
        worldName = plugin.getConfig().getString("mines-world-name");

    }

    @EventHandler
    public void onBlockClickEvent(PlayerInteractEvent event) {
        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BEACON)) { return; }
        Material blocktype = event.getClickedBlock().getType();
        if (Objects.requireNonNull(blocktype) == Material.AIR) { return; }
        if (!enabledBlocks.contains(blocktype.toString().toLowerCase())) { return; }
        if (!event.getClickedBlock().getWorld().getName().equals(worldName)) { return; }
        Profile profile = PrisonCore.playerProfiles.get(event.getPlayer());
        profile.addAbsorbedBlock(blocktype, 1);
        event.getClickedBlock().setType(Material.AIR);
        event.setCancelled(true);
    }

}
