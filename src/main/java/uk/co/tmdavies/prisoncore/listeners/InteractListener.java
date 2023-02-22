package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.units.qual.A;
import uk.co.tmdavies.prisoncore.PrisonCore;

import java.util.ArrayList;
import java.util.Objects;

public class InteractListener implements Listener {

    private final ArrayList enabledBlocks = new ArrayList();

    public InteractListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onBlockClickEvent(PlayerInteractEvent event) {
        Material blocktype = event.getClickedBlock().getType();
        if (Objects.requireNonNull(blocktype) == Material.AIR) { return; }
        if (!enabledBlocks.contains(blocktype)) { return; }
        // Add the block list to profile. Tyler I need ya to tell me how the profile works so that I can use it here.
        // I wanna store the blocks they have collected in there so it's easy to store and then we can work on the saving of that.
    }

}
