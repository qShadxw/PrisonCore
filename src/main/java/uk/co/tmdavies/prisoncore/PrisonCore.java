package uk.co.tmdavies.prisoncore;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.prisoncore.commands.CoreCommand;
import uk.co.tmdavies.prisoncore.listeners.ChatListener;
import uk.co.tmdavies.prisoncore.listeners.JoinListener;
import uk.co.tmdavies.prisoncore.objects.PrisonPlayer;

import java.util.HashMap;

public final class PrisonCore extends JavaPlugin {

    public static HashMap<Player, PrisonPlayer> prisonPlayers;
    public static boolean papiEnabled;
    public static Permission perms;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        prisonPlayers = new HashMap<>();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        new CoreCommand(this);
        new ChatListener(this);
        new JoinListener(this);

        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        assert rsp != null;
        perms = rsp.getProvider();

        papiEnabled = (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null);
    }

}
