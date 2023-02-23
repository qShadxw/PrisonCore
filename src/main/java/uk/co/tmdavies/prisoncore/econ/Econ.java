package uk.co.tmdavies.prisoncore.econ;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.listeners.InteractListener;
import uk.co.tmdavies.prisoncore.objects.Profile;
import uk.co.tmdavies.prisoncore.utils.Utils;


public class Econ {

    Plugin plugin;

    public Econ(Plugin plugin) {
        this.plugin = plugin;
        onRun();
    }

    public void onRun() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskTimer(plugin, () -> {
            double totalAmount = 0.00;
            for (Player player : Bukkit.getOnlinePlayers()) {
                Profile pro = PrisonCore.playerProfiles.get(player);
                for (Material material : pro.getAbsorbedBlocks().keySet()) {
                    int amount = pro.getAbsorbedBlocks().get(material);
                    double value = InteractListener.blockValues.get(material);
                    PrisonCore.econ.depositPlayer(player, value * amount);
                    totalAmount = totalAmount + (value * amount);
                }
                player.sendMessage(Utils.Colour(
                        "&aYou've earned &c$" + totalAmount + "&a in the last " + plugin.getConfig().getInt("absorb-able-blocks-interval") + " &aSeconds"));
            }
        }, 0L, (plugin.getConfig().getInt("absorb-able-blocks-interval") * 20L));

    }
}
