package uk.co.tmdavies.prisoncore.econ;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.listeners.InteractListener;
import uk.co.tmdavies.prisoncore.objects.Profile;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.awt.*;
import java.util.Map;


public class Econ extends BukkitRunnable {

    private final PrisonCore plugin;

    public Econ(PrisonCore plugin) {

        this.plugin = plugin;

    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        double totalAmount = 0.00;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = PrisonCore.playerProfiles.get(player);
            for (Map.Entry entry : profile.getAbsorbedBlocks().entrySet()) {
                Material material = (Material) entry.getKey();
                int amount = (int) entry.getValue();
                double value = InteractListener.blockValues.get(material);
                profile.giveMoney(value * amount);
                totalAmount = totalAmount + (value * amount);
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Utils.Colour("&a&l+&c&l$" + totalAmount)));
        }
    }
}