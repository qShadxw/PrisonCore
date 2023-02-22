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
import java.util.Objects;

public class InteractListener implements Listener {

    public static ArrayList<String> enabledBlocks = new ArrayList<>();

    public InteractListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

        enabledBlocks.addAll(Arrays.asList(plugin.getConfig().getString("absorb-able-blocks").split(";")));

    }

    @EventHandler
    public void onBlockClickEvent(PlayerInteractEvent event) {
        Material blocktype = event.getClickedBlock().getType();
        if (Objects.requireNonNull(blocktype) == Material.AIR) { return; }
        if (!enabledBlocks.contains(blocktype)) { return; }

    }

}
