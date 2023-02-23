package uk.co.tmdavies.prisoncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import uk.co.tmdavies.prisoncore.PrisonCore;

public class BlockListener implements Listener {

    public BlockListener(PrisonCore plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onBlockBreakEvent(BlockPlaceEvent event) {

    }

}
