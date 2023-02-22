package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import uk.co.tmdavies.prisoncore.PrisonCore;

public class BlockListener implements Listener {

    public BlockListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onBlockBreakEvent(BlockPlaceEvent event) {
        if (event.getBlock().getType().equals(JoinListener.baItem.getType())) { event.setCancelled(true); }
    }

}
